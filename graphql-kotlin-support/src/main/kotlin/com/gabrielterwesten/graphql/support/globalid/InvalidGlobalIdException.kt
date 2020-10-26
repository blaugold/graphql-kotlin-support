package com.gabrielterwesten.graphql.support.globalid

/** Exception which thrown when a value is not a correctly encoded [GlobalId]. */
class InvalidGlobalIdException(
    /** The id that could not be decoded. */
    val encodedId: String,
    override val message: String,
    override val cause: Throwable? = null,
) : RuntimeException()
