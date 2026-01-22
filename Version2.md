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