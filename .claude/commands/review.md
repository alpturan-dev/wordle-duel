Review my uncommitted changes (`git diff` and `git diff --staged`).

Check against docs/game-spec.md, docs/architecture.md and the rules in
CLAUDE.md, especially:
- Could any outgoing payload leak a secret word before ROUND_OVER?
- Is any game logic (colouring, scoring, winner) happening on the client?
- Are incoming messages validated against the current match state?
- Race conditions when both players act at once.
- Missing tests for new logic.

For each issue: file and line, what's wrong, why it matters, and the
direction to fix it. Don't rewrite my code. End with what's done well.
