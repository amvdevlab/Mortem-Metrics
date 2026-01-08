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
