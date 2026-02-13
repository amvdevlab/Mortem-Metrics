# Build Context - Mortem Metrics

<details>
<summary>TEMPLATE</summary>

**TEMPLATE for each work session. Agent, do not remove. Populate with necessary information only; omitting verboseness. This file is reverse ordered (most recent at the top).**

> **TEMPLATE START**

<details>
<summary><strong>Session title and date MM.DD.YYYY</strong></summary>

Category tags. Choose one or more that best describes the work within this session.

- BUILD
- REFACTOR
- FEATURE
- CHORE
- DOCS
- STYLE
- OPTIMIZE
- TEST
- CI

## Summary

Provide a paragraph summary of the implemented code, architecture, and decisions in a concise and clear manner. This is for one working session.

## Technical Bullets

What was implemented with file names [code only] (changes, fixes, refactors, builds, etc.)

## Decisions & Rationale

Key decisions made and why (architecture choices, library selections, approach changes, trade-offs)

## Dependencies Added/Removed

- Added: `package@version` - reason
- Removed: `package@version` - reason

## Breaking Changes

Any changes that affect existing functionality or require updates elsewhere

## Known Issues / Tech Debt

Problems deferred, workarounds implemented, or areas needing future attention

## Next Steps

Immediate follow-ups or planned work for subsequent sessions

## Context for AI

Special patterns, conventions, or constraints an agent should know when continuing this work (e.g., "always use composition over inheritance in /components", "API calls must include retry logic")

</details>

> **TEMPLATE END**

</details>

<details>
<summary><strong>TextReducerService + tests (compression, chunking, Gemini context) — 02.05.2026</strong></summary>

FEATURE | TEST | CHORE

## Summary

Implemented TextReducerService for postmortem text compression and chunking aligned to Gemini context limits. Service supports approximate token counting (~4 chars/token), configurable compression level (none / light / aggressive), whitespace collapse and consecutive-line deduplication, and chunking when content exceeds max-tokens (split on paragraph `\n\n`, then by line `\n` for oversize paragraphs). Added properties in application.properties and application-test.properties (including max-tokens=20 for tests). Test suite: one flattened main test class (TextReducerServiceTEST) and two separate top-level classes for compression-level overrides (TextReducerServiceCompressNoneTEST, TextReducerServiceCompressAggressiveTEST) to keep @TestPropertySource on classes only and avoid @SuppressWarnings("unused") on nested classes.

## Technical Bullets

- **TextReducerService.java** — `compress()`, `approximateTokenCount()`, `chunkIfNeeded()`, `chunkByLines()`; `collapseWhitespaceLight` / `collapseWhitespaceAggressive`, `deduplicateLines()`; `@Value` for compression-level and max-tokens
- **application.properties** — `postmortem.text-reducer.compression-level=light`, `postmortem.text-reducer.max-tokens=900000`
- **application-dev.properties.template** — same two properties
- **application-test.properties** — same two properties; `max-tokens=20` so chunking tests run with small limit
- **TextReducerServiceTEST.java** — flattened, no @Nested; 11 tests (compress null/empty/light, token count, chunkIfNeeded null/empty/under/over limit, large input >1MB)
- **TextReducerServiceCompressNoneTEST.java** — `@TestPropertySource(compression-level=none)`, one test
- **TextReducerServiceCompressAggressiveTEST.java** — `@TestPropertySource(compression-level=aggressive)`, one test

## Decisions & Rationale

- **Approximate token count (length/4)** — Keeps service offline and fast; can swap for real tokenizer later if needed.
- **Chunk split on `\n\n` then `\n`** — Original use of `\n{3,}` never split after light/aggressive compression (no triple newlines). Paragraph then line split ensures chunking works for all compression levels.
- **Compression level in properties** — Enables env-specific behavior without code changes.
- **Flattened tests + separate classes for overrides** — Avoids @TestPropertySource on methods (not allowed) and avoids @SuppressWarnings("unused") on @Nested classes; matches project style (e.g. JsonParserTest).
- **Test class suffix TEST (all caps)** — Matches filenames and improves scan-ability.

## Dependencies Added/Removed

- None

## Breaking Changes

- None

## Known Issues / Tech Debt

- **TimelineRepositoryTest** — H2 DDL warning (`set client_min_messages = WARNING`) when using PostgreSQL dialect; pre-existing, not from this session.
- **application-test.properties** — Duplicate `spring.jpa.hibernate.ddl-auto=create-drop` (optional cleanup).

## Next Steps

- Wire TextReducerService into parse/upload or analysis flow when needed.
- Optionally add real tokenizer (e.g. Gemini API or local model) later for stricter context limits.

## Context for AI

- Test classes in this project use **TEST** (all caps) suffix for class and file names (e.g. TextReducerServiceTEST.java).
- Text-reducer config lives in application.properties and profile-specific files: `postmortem.text-reducer.compression-level`, `postmortem.text-reducer.max-tokens`.
- Chunking uses PARAGRAPH_BREAK (`\n\n`) first; oversize paragraphs are split by single newline in `chunkByLines()`. Do not rely on `\n{3,}` in compressed output.

</details>
