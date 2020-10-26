package com.gabrielterwesten.graphql.support.example

import com.fasterxml.jackson.databind.JsonMappingException
import com.gabrielterwesten.graphql.support.attributeexception.AttributeException
import com.gabrielterwesten.graphql.support.attributeexception.AttributeExceptionConfig
import com.gabrielterwesten.graphql.support.errors.ExceptionResolver
import com.gabrielterwesten.graphql.support.globalid.InvalidGlobalIdException
import org.springframework.stereotype.Component

/** An error code which allows clients to classify an error. */
enum class ApiErrorCode {
  InternalError,
  NotFound,
  UserNameAlreadyTaken,
}

/** Base class for all exceptions the api exposes. */
abstract class ApiException : AttributeException() {

  /** The code of this exception, which is added to [createAttributes] under `code`. */
  abstract val code: ApiErrorCode

  override fun createAttributes(config: AttributeExceptionConfig): Map<String, Any?> =
      super.createAttributes(config) + mapOf("code" to code)
}

/** Exception which is thrown when a unexpected exception is caught. */
class InternalErrorException(
    override val message: String = "An internal error occurred.",
    override val cause: Throwable? = null,
) : ApiException() {
  override val code: ApiErrorCode = ApiErrorCode.InternalError
}

/** Exception which is thrown when an entity could not be found. */
class NotFoundException(
    override val message: String = "Object could not be found.",
    override val cause: Throwable? = null,
) : ApiException() {
  override val code: ApiErrorCode = ApiErrorCode.NotFound
}

class UserNameAlreadyTakenException(
    val userName: String,
    override val cause: Throwable? = null,
) : ApiException() {

  override val message: String = "The user name \"$userName\" is already taken."

  override val code: ApiErrorCode = ApiErrorCode.UserNameAlreadyTaken

  override fun createAttributes(config: AttributeExceptionConfig): Map<String, Any?> =
      super.createAttributes(config) + mapOf("userName" to userName)
}

@Component
class ApiExceptionResolver : ExceptionResolver {
  override fun resolveException(exception: Throwable): Throwable? =
      when (exception) {
        is ApiException -> exception
        is IllegalArgumentException -> resolveException(exception)
        else -> null
      }
          ?: InternalErrorException(cause = exception)

  private fun resolveException(exception: IllegalArgumentException): Throwable? =
      when (val cause = exception.cause
      ) {
        is JsonMappingException -> resolveException(cause)
        else -> null
      }

  private fun resolveException(exception: JsonMappingException): Throwable? =
      when (val cause = exception.cause
      ) {
        is InvalidGlobalIdException -> NotFoundException(cause = cause)
        else -> null
      }
}
