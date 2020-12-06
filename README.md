![CI](https://github.com/blaugold/graphql-kotlin-support/workflows/CI/badge.svg)
[![Download](https://api.bintray.com/packages/gabriel-terwesten-oss/maven/graphql-kotlin-support/images/download.svg) ](https://bintray.com/gabriel-terwesten-oss/maven/graphql-kotlin-support/_latestVersion)

# graphql-kotlin-support

A library which provides some commonly needed functionality for the development of GraphQL APIs, specifically with
[graphql-kotlin].

- Efficient and ergonomic `GlobalId` implementation
- `AttributeException`: Flexible abstraction for exposing error attributes to clients
- Data fetching exception handling including exception resolution, logging and transformation of
  `AttributeException`s into `GraphQLError`s.
- Handling of function returning `Mono`
- Propagation of request reactor `Context` to coroutines and returned `Mono`s.
- Data loading declarations with idiomatic Kotlin

## 📦 Modules

- [example](./example) - Example project which uses `graphql-kotlin-support-starter` and `graphql-kotlin`
- [graphql-kotlin-support](./graphql-kotlin-support) - Contains the support functionality for global ids, exception
  handling, data loading and execution context
- [graphql-kotlin-support-spring](./graphql-kotlin-support-spring) - Spring specific support functionality, including
  for integration with [Project Reactor](https://projectreactor.io/) and [graphql-kotlin]
- [graphql-kotlin-support-autoconfigure](./graphql-kotlin-support-autoconfigure) - Autoconfiguration for Spring Boot
- [graphql-kotlin-support-starter](./graphql-kotlin-support-starter) - Spring Boot Starter

# Install

## Repository

All the modules are available through `jcenter`.

## Spring & graphql-kotlin

To quickly get started, when you are using Spring and [graphql-kotlin], add a dependency to the Spring Boot Starter to
your project:

With maven:

```xml
<dependency>
	<groupId>com.gabrielterwesten</groupId>
	<artifactId>graphql-kotlin-support-starter</artifactId>
	<version>0.3.0</version>
</dependency>
```

With gradle:

```kotlin
implementation("com.gabrielterwesten:graphql-kotlin-support-starter:0.3.0")
```

### Auto configuration

With the autoconfiguration applied you will be able to return `Mono`s from your resolvers. Further you will be able to
use subclasses of `GlobalId` in place of `com.expediagroup.graphql.scalars.ID`, in your schema.

The reactor context, from the server request, will be propagated to coroutines and `Mono`s which are returned from
resolvers.

Coroutines in resolvers will be started with `CoroutineStart.UNDISPATCHED`, meaning they will start to execute
immediately. This is necessary to allow `DataLoaderDispatcherInstrumentation` to work properly.

If you have a `GlobalIdRegistrationRepository` configured, this repository will be used to encode/decode `GlobalId`s.

The attributes of any caught `AttributeException` will be added to the `extensions` property of the corresponding
`GraphQLError`. When the Spring Boot Devtools properties are active, the stack trace of the caught exception will
also be included in `extensions`.

For more information on how to customize the configuration, take a look a the [example](./example) project.

## Development

### Formatting

The project uses `spotless` to enforce a consistent code style. Formatting is configured for the most common files
types, namely **kotlin** and **markdown**.

Before committing run `./gradlew spotlessCheck` to find formatting errors and `./gradlew spotlessApply` to fix them.

[graphql-kotlin]: https://expediagroup.github.io/graphql-kotlin/docs/getting-started.html
