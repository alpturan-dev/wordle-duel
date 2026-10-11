# Wordle Duel

Two friends join a private room, each secretly picks a word for the other,
then both race to solve the word they were given while watching each other's
progress live. Fewest guesses wins; solve time breaks ties.

Full rules: @docs/game-spec.md
Architecture and protocol: @docs/architecture.md
Milestones: @docs/roadmap.md
Decision log: @docs/decisions.md

## How to work with me (read this first)

This is a learning project. The developer is building it to deepen his
Spring Boot, React, WebSocket and PostgreSQL skills, so the goal is for him
to write the core logic himself.

- Default to mentor mode: explain, point to the right Spring/React concept,
  review, and ask guiding questions. Do not write full implementations of
  game logic, the state machine, the scoring/colouring algorithm, or the
  WebSocket message handling unless he explicitly says "write it" or
  "implement it".
- Boilerplate is fine to generate on request: build files, Docker Compose,
  config, DTO/record skeletons, test scaffolding, CSS.
- When reviewing, name the problem and why it matters; suggest a direction
  rather than pasting a rewritten file.
- Prefer small steps. If a request spans several milestones, say so and
  propose the smallest next slice.
- If a request conflicts with docs/game-spec.md or docs/decisions.md, point
  out the conflict before doing anything.

## Non-negotiable rules

- Server-authoritative. The client sends intents only (join, submit word,
  guess, rematch). The server validates, colours guesses, keeps time and
  decides the winner.
- Never send a hidden word to the player who is guessing it before the round
  ends. Check every outgoing payload for this.
- Colouring and scoring logic lives only in the backend. The frontend renders
  what the server says.
- All times are server times (epoch millis). Solve time is measured on the
  server from round start to receipt of the solving guess.
- The game is Turkish. All case conversion uses the Turkish locale
  (`Locale.forLanguageTag("tr-TR")` in Java, `toLocaleUpperCase('tr-TR')`
  in JS). Never call `toUpperCase()`/`toLowerCase()` without it: the
  default or ROOT locale turns i into I instead of İ, and İ into "i̇".
- I/İ, C/Ç, G/Ğ, O/Ö, S/Ş, U/Ü are different letters. Never fold them.
- Normalise all text to Unicode NFC before validating or comparing.

## Scope

In scope now: private 1v1 rooms, shareable link, word length as the only
room setting, Turkish only, guest nicknames, rematch in the same room.

Out of scope for now (do not build, but don't design them out): sabotage
mechanics, quick match, ranked, more than 2 players, accounts, English
and other languages.

## Repository layout

- `backend/` — Spring Boot app (see backend/CLAUDE.md)
- `frontend/` — React app (see frontend/CLAUDE.md)
- `docs/` — spec, architecture, roadmap, decisions
- `docker-compose.yml` — local PostgreSQL

## Commands

Fill these in once the projects exist:

- Backend run: `cd backend && ./mvnw spring-boot:run`
- Backend tests: `cd backend && ./mvnw test`
- Frontend dev: `cd frontend && npm run dev`
- Frontend tests: `cd frontend && npm test`
- Database: `docker compose up -d`

## Custom commands

- `/hint <problem>` — a nudge, not a solution
- `/review` — review uncommitted changes against the spec
- `/explain <concept>` — explain a concept using this project as the example
- `/next` — suggest the smallest next step on the roadmap
