# VERSION2 

This document is for future improvements, maintenance, and bug fixes.
### Database

#### Timeline.java

Add an `updated_at` attribute for editable markdown timelines and storage.   
**Why**: Support for timeline markdown re-generation feature.

#### TimelineRepository.java

- Add database index on `created_at` column for optimized sorting queries
- **Why**: Improve performance of `findAllByOrderByCreatedAtDesc()` as dataset grows
- **Implementation**: Add `@Index` annotation to `Timeline.java` entity

### Parser

#### ParseError & ParseMetadata (parser/dto)

- **ParseError.java** and **ParseMetadata.java** are currently empty stubs and unused.
- **Why**: Enable structured parse results—e.g. multiple errors with line/column, or metadata (encoding, line count, parse duration) for analytics or UI.
- **Implementation**: Define fields (e.g. ParseError: message, line, column, severity; ParseMetadata: encoding, lineCount, durationMs). Integrate into `ParseResult` (e.g. `List<ParseError>`, optional `ParseMetadata`) and have parsers populate them. Remove or repurpose the current single `errorMessage` on `ParseResult` as needed.

#### CsvParser – structural parsing

- **Current behavior**: CsvParser returns the entire file as a single UTF-8 string (no row/column structure).
- **Why**: Support features that need CSV structure (e.g. header row, rows as records, validation per cell).
- **Implementation**: Introduce a CSV-specific result type or extend ParseResult with optional structured data (e.g. headers + `List<String[]>` rows). Use a library (e.g. OpenCSV) or implement RFC 4180–style parsing; handle quoted fields and escaping. Keep backward compatibility or document breaking change.
