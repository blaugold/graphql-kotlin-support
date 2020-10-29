package com.gabrielterwesten.graphql.support.globalid

import graphql.schema.DataFetchingEnvironment
import graphql.schema.PropertyDataFetcher

class GlobalIdPropertyDataFetcher(
    propertyName: String,
    private val globalIdConverter: GlobalIdConverter,
) : PropertyDataFetcher<Any?>(propertyName) {
  override fun get(environment: DataFetchingEnvironment?): Any? =
      when (val result = super.get(environment)
      ) {
        is GlobalId<*> -> globalIdConverter.encode(result)
        else -> result
      }
}
