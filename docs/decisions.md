# Decisions

## Decided

- 1v1 only for now.
- Word length is the only room setting (4-7).
- Winner: fewest guesses; ties broken by server-measured solve time;
  neither solves = draw.
- Opponent's grid shows full letters and colours.
- Server-authoritative; secret words never sent to the guesser mid-round.
- Turkish only for the first version.
- Turkish letter pairs (I/İ, C/Ç, G/Ğ, O/Ö, S/Ş, U/Ü) are distinct letters.
- Only root dictionary forms are valid words.
- Guests with nicknames; no accounts yet.
- 2026-10-11: Backend on Java 25 and Spring Boot 4 (instead of Java 21 / Boot 3).

## Open (decide and move to "Decided" with a date)

- Max guesses: word length + 1, or always 6?
- Round time limit: none, fixed (e.g. 5 min), or inactivity-based?
- Reconnect grace period length (suggested 30 s).
- Idle room expiry (suggested 30 min).
- TypeScript or plain JavaScript on the frontend.
- Match concurrency: lock per match vs single-threaded executor per match.
- Circumflexes: strip them (current assumption) or reject such words?
- Turkish word list source + licence (e.g. Hunspell tr, Zemberek lexicon,
  existing Turkish Wordle lists) and a frequency list for the common-word
  secret list. Check every licence before committing files.
