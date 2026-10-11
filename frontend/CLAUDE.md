# Frontend (React)

Read the root CLAUDE.md first; mentor mode applies here too.

- The UI renders server snapshots. Do not compute colours, scores or the
  winner on the client.
- Keep the STOMP connection in one place (a hook or context), not scattered
  across components.
- Store the player token so a page refresh resumes the seat.
- Countdown = server deadline minus (local now + server clock offset).
- The on-screen keyboard colour for a letter is the best colour that letter
  has received so far (GREEN > YELLOW > GRAY), derived from the grid.
- Mobile first: the two grids must fit on a phone screen.
- Set `<html lang="tr">` so CSS `text-transform: uppercase` renders i as İ.
  In JS use `toLocaleUpperCase('tr-TR')`, never `toUpperCase()`.
- The on-screen keyboard is a Turkish Q layout with all 29 letters (Ç Ğ I İ
  Ö Ş Ü included; no Q W X), since many physical keyboards lack them.
