package com.gabrielterwesten.graphql.support.errors

/** A resolver which should translate an exception to a more a appropriate one if possible. */
interface ExceptionResolver {
  fun resolveException(exception: Throwable): Throwable?
}
