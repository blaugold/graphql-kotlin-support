# graphql-kotlin-support

A library which provides some commonly needed functionality for the development of GraphQL APIs, specifically with
`graphql-kotlin`.

- Efficient and ergonomic `GlobalId` implementation
- `AttributeException`: Flexible abstraction for exposing error attributes to clients
- Data fetching exception handling including exception resolution, logging and transformation of
  `AttributeException`s into `GraphQLError`s.

## TODO

- Readme

## Development

### Formatting

The project uses `spotless` to enforce a consistent code style. Formatting is configured for the most common files
types, namely **kotlin** and **markdown**.

Before committing run `./gradlew spotlessCheck` to find formatting errors and `./gradlew spotlessApply` to fix them.
