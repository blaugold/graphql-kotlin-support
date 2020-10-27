package com.gabrielterwesten.graphql.support.schemagen

import com.gabrielterwesten.graphql.support.globalid.GlobalId
import com.gabrielterwesten.graphql.support.globalid.GlobalIdConverter
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
