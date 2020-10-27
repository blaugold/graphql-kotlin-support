package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.hooks.SchemaGeneratorHooks
import com.gabrielterwesten.graphql.support.globalid.isGlobalId
import graphql.Scalars
import graphql.schema.GraphQLType
import kotlin.reflect.KType
import reactor.core.publisher.Mono

class ReactorGlobalIdSchemaGenerationHooks : SchemaGeneratorHooks {
  override fun willResolveMonad(type: KType): KType =
      when (type.classifier) {
        Mono::class -> type.arguments.firstOrNull()?.type
        else -> type
      }
          ?: type

  override fun willGenerateGraphQLType(type: KType): GraphQLType? =
      when {
        type.isGlobalId -> Scalars.GraphQLID
        else -> null
      }
}
