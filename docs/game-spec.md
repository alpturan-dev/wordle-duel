# Game spec

## Language

Turkish only. The alphabet is the 29 Turkish letters:
A B C Ç D E F G Ğ H I İ J K L M N O Ö P R S Ş T U Ü V Y Z
(no Q, W, X). Words, guesses and the on-screen keyboard use exactly these.

- Only dictionary (root) forms count: "kitap" yes, "kitaplar" no. No proper
  nouns, abbreviations or multi-word entries.
- Each Turkish letter is one tile. C and Ç, I and İ, etc. are distinct.
- Circumflexes (â, î, û) are stripped: "kâr" is stored and typed as "kar"
  (see decisions.md).

## Room

- A player creates a room and picks a word length: 4, 5, 6 or 7.
  This is the only setting.
- The server returns a short, unguessable room code. The share link is
  `/r/{code}`.
- Players join with a nickname only (no accounts).
- Exactly 2 players. A third player trying to join gets a "room full" error.
- Idle rooms expire (see decisions.md for the timeout).

## Round flow

1. Waiting for opponent — host is alone in the room.
2. Choosing words — each player types a secret word of the room's length.
   The word must be in the secret-word list (common words). The opponent
   only sees "has chosen" / "still choosing", never the word.
3. Playing — starts when both have chosen. Each player now guesses the word
   the other chose.
   - Max guesses: word length + 1 (6 for 5 letters).
   - Every guess must be exactly the right length and in the allowed-guess
     list (larger than the secret list). Invalid guesses are rejected and do
     not count.
   - Each player sees their own grid, and the opponent's grid with full
     letters and colours, updated live. This leaks nothing: the opponent is
     guessing a word you already know.
4. Round over — when both players have solved or run out of guesses (or the
   round time limit hits). Both secret words are revealed.
5. Rematch — either player can request; when both accept, go back to step 2
   in the same room.

## Winning

- Solving beats not solving.
- If both solve: fewer guesses wins.
- Same number of guesses: lower solve time wins (server-measured, from round
  start to receipt of the solving guess).
- Neither solves: draw.
- The round may end early once the result is decided: e.g. A solved in 3,
  B has used 3 guesses without solving, so B cannot win or tie.

## Colouring rule

For each guess, per letter: GREEN (right letter, right spot), YELLOW (in
the word, other spot), GRAY (not available).

Duplicates are the tricky part. Assign greens first. Then, for the remaining
positions left to right, a letter is YELLOW only if an unmatched copy of it
is still left in the secret; otherwise GRAY.

Test cases (secret / guess -> result):

| Secret | Guess | Result                          | Tests            |
|--------|-------|---------------------------------|------------------|
| KALEM  | KELAM | GREEN YELLOW GREEN YELLOW GREEN | basic yellows    |
| ELMAS  | ELEME | GREEN GREEN GRAY YELLOW GRAY    | duplicate E      |
| KAYAK  | AKRAN | YELLOW YELLOW GRAY GREEN GRAY   | duplicate A, K   |
| KILIÇ  | KİLİM | GREEN GRAY GREEN GRAY GRAY      | I and İ differ   |
| AĞAÇ   | ACAR  | GREEN GRAY GREEN GRAY           | C and Ç differ   |
| KALEM  | KALEM | all GREEN                       | solved           |

Input normalisation tests (after NFC + Turkish upper-casing):
"istanbul" -> "İSTANBUL", "ılık" -> "ILIK", "kâr" -> "KAR".

## Disconnects

- A dropped player has a grace period to reconnect (see decisions.md).
- On reconnect the server sends a full snapshot: room state, both grids,
  remaining time.
- If they don't return in time, they forfeit the round.
