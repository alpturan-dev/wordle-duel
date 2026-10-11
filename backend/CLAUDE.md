# Backend (Spring Boot)

Read the root CLAUDE.md first; mentor mode applies here too.

- Java 21. Use records for DTOs and messages.
- Keep `match` and `word` packages free of Spring web/WebSocket types so
  they can be unit tested without a Spring context.
- GuessEvaluator must have tests for every case in docs/game-spec.md before
  it is used anywhere.
- Validate every incoming STOMP message against the match state; reply on
  `/user/queue/errors` instead of throwing into the void.
- Before publishing to `/topic/rooms/{code}`, the payload must not include a
  secret word unless the state is ROUND_OVER. Suggest a test that asserts
  this.
- Case conversion always with `Locale.forLanguageTag("tr-TR")`; keep the
  locale in one constant. NFC-normalise with `java.text.Normalizer`.
- The normalisation tests in docs/game-spec.md ("istanbul" -> "İSTANBUL")
  should exist alongside the colouring tests.
- Tests: JUnit 5; Testcontainers for anything touching PostgreSQL.
