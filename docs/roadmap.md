# Roadmap

Each milestone should be playable or testable on its own.

- [ ] M0 Skeleton — backend and frontend boot, Postgres in Docker, health
      endpoint, frontend can call it.
- [ ] M1 Rooms — create room, get code, open link in a second tab, both
      tabs see each other's nickname over WebSocket.
- [ ] M2 Choosing words — Turkish word lists loaded and normalised, word
      length enforced, dictionary validation,
      "has chosen" status, transition to PLAYING when both are ready.
- [ ] M3 Guessing — server-side colouring with the duplicate-letter tests
      from game-spec.md passing, own grid renders, guess limit.
- [ ] M4 Live opponent — opponent's grid updates in real time.
- [ ] M5 Results — winner by guesses then time, draw, early end, reveal,
      rematch in the same room.
- [ ] M6 Resilience — player token, reconnect with snapshot, grace period,
      forfeit, round time limit, room expiry.
- [ ] M7 History — persist finished matches in PostgreSQL.
- [ ] M8 Polish and deploy — Turkish on-screen keyboard colouring, animations,
      mobile layout, deployment.

Later (do not start yet): sabotage mechanics, quick match, ranked,
English and other languages, more than 2 players.
