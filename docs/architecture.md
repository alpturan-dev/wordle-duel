# Architecture

## Stack

- Backend: Java 21, Spring Boot 3, Spring WebSocket with STOMP, Maven
- Frontend: React + Vite (see decisions.md re TypeScript), @stomp/stompjs
- Database: PostgreSQL (match history later; not needed for live games)
- Local dev: Docker Compose for PostgreSQL

## Core idea: separate "how players meet" from "how a match runs"

- `room` layer: creating rooms, codes, joining, nicknames, rematch.
- `match` layer: one round between two players. Knows nothing about rooms
  or links. Takes two player ids + word length, runs the state machine.

Quick match later = a different way of creating the same match.

## Suggested backend packages

- `room` — Room, RoomService, RoomController (REST for create/lookup)
- `match` — Match, MatchState, PlayerProgress, the state machine
- `word` — Dictionary (loads word lists at startup), GuessEvaluator
  (colouring), scoring/result comparison
- `ws` — STOMP config, message controllers, outgoing DTOs
- `player` — guest identity, session tracking, reconnect grace timers

Active rooms and matches live in memory (ConcurrentHashMap keyed by room
code). Single instance is fine for now; multi-instance needs sticky routing
or an external store later.

## Match state machine

Room level:
WAITING_FOR_OPPONENT -> CHOOSING_WORDS -> PLAYING -> ROUND_OVER
ROUND_OVER -> CHOOSING_WORDS (rematch accepted)
any -> CLOSED (expired, or both left)

Per player during PLAYING: GUESSING -> SOLVED | FAILED

Every incoming message is checked against the current state first. A guess
during CHOOSING_WORDS is an error, not an edge case to handle.

Concurrency: two players act at the same time on the same match. Decide
how a match serialises its updates (lock per match, or a single-threaded
executor per match) — see decisions.md.

## Identity

- On join the server issues a player token (UUID), stored by the client.
- The token, not the WebSocket session, identifies the player, so a
  reconnect with the same token resumes their seat.

## Protocol (draft)

REST:
- `POST /api/rooms` body `{ wordLength }` -> `{ code }`
- `GET /api/rooms/{code}` -> exists / full / wordLength

STOMP, client -> server (`/app/...`):
- `rooms/{code}/join` `{ nickname, playerToken? }`
- `rooms/{code}/word` `{ word }`
- `rooms/{code}/guess` `{ guess }`
- `rooms/{code}/rematch`

STOMP, server -> client:
- `/topic/rooms/{code}` — public room snapshot: players, states, both
  grids (letters + colours), round deadline, server time, result.
  Must never contain a secret word before ROUND_OVER.
- `/user/queue/errors` — rejected actions (invalid word, wrong state).
- `/user/queue/private` — anything only this player may see (e.g. their
  own chosen word, for display).

Send snapshots, not diffs, at first: simpler, and reconnect comes for free.

## Timers

- Deadlines are sent as absolute server epoch millis plus current server
  time; the client computes its offset and renders its own countdown.
- Server-side timers (round limit, reconnect grace) use a scheduler and must
  be cancelled when the state changes.

## Words

- Two lists per word length in `backend/src/main/resources/words/`:
  `secret-{n}.txt` (common words, for picking) and `guess-{n}.txt`
  (everything acceptable as a guess). Secret list must be a subset.
- Loaded into `Set<String>` at startup: NFC-normalise, strip circumflexes,
  upper-case with the Turkish locale, drop anything with a character outside
  the 29-letter alphabet.
- One `WordNormalizer` does this for word lists, chosen words and guesses
  alike, so the three can never disagree.
- Root forms only, so no morphological analyser (Zemberek) is needed.
