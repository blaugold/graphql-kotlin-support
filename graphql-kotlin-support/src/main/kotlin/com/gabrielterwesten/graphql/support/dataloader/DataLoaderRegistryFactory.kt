package com.gabrielterwesten.graphql.support.dataloader

import org.dataloader.DataLoaderRegistry

interface DataLoaderRegistryFactory {
  suspend fun generate(): DataLoaderRegistry
}
