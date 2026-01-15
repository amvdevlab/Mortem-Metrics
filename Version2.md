# Upgrades to be Completed

## Database

### Object Reference

```
private Integer createdByUserId
```

**Improvement**:
Convert this to a `@ManytoOne` relationship with a User entity.
This allows the ability to pull the user's name or email directly from the incident without a second database query.

### Log Level

Change `logLevel` from a `String` to an `Enum`.
This ensures no one saves "Potato" as a logLevel.

### Optimized Query

Add indexes to improve query performances. For the @Table annotation, add indexes for frequently queried fields within the entity directory.

### Parser

Add a `config/` directory within the parser package to externalize parsing rules and patterns.

**Purpose**:

- **Flexibility**: Support multiple log formats without code changes
- **Multi-tenancy**: Different clients can have different parsing rules
- **Maintainability**: Non-developers can update regex patterns via configuration files
- **Scalability**: Add new log formats by editing `application.properties` instead of redeploying code

**Implementation**:

- `ParserConfiguration.java` - Spring `@ConfigurationProperties` class
- Bind to `application.properties` with prefix `parser.*`

**Example Configuration**:

# CSV column mappings

parser.csv.column-mapping.timestamp=timestamp
parser.csv.column-mapping.log-level=level
parser.csv.column-mapping.message=message

# Log parsing regex patterns (supports multiple formats)

parser.log.patterns[0]=\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})\\] (\\w+): (.+)
parser.log.patterns[1]=^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}Z) \\[(\\w+)\\] (.+)

# File size limits

parser.max-file-size-mb=50
parser.strict-mode=false
