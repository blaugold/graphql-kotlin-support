package com.gabrielterwesten.graphql.support.exceptions

/** A resolver which should translate an exception to a more a appropriate one if possible. */
interface ExceptionResolver {
  suspend fun resolveException(exception: Throwable): Throwable?
}
