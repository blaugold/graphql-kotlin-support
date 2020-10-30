package com.gabrielterwesten.graphql.support.dataloader

import graphql.schema.DataFetchingEnvironment
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.BatchLoaderEnvironment
import org.dataloader.DataLoader
import org.dataloader.DataLoaderOptions
import org.dataloader.DataLoaderRegistry

/**
 * Key which allows typesafe registration and retrieval of a data loader.
 *
 * [K] is the type of the keys the data loader expects and [V] the type of the results.
 *
 * A data loader will be registered in the [DataLoaderRegistry] under [key].
 */
@Suppress("unused")
data class DataLoaderKey<K, V>(val key: String)

/** Retrieve a data loader from the environment which has be registered under [key]. */
fun <K, V> DataFetchingEnvironment.dataLoader(key: DataLoaderKey<K, V>): DataLoader<K, V> =
    getDataLoader(key.key)

/**
 * Function which is expected to load values given a list of keys.
 *
 * The result should be order such that the index of a key in the input list matches the index of
 * the corresponding value. Values which could not be loaded can be replaced with `null`, bust must
 * still be included in the result.
 */
typealias DataLoaderFn<K, V> = suspend (List<K>) -> List<V?>

/**
 * Function which is expected to load values given a set of keys.
 *
 * The result should be a map where each key is maps to the corresponding value. The map does not
 * have to contain a key if its value could not be loaded.
 */
typealias MappedDataLoaderFn<K, V> = suspend (Set<K>) -> Map<K, V?>

/**
 * Builder for a [DataLoaderRegistry] which supports a kotlin idiomatic syntax and suspending data
 * loaders.
 */
fun kotlinDataLoaderRegistry(
    graphQlContext: Any? = null,
    builder: KotlinDataLoaderRegistryDsl.() -> Unit,
): DataLoaderRegistry {
  val registry = DataLoaderRegistry()
  val dsl = KotlinDataLoaderRegistryDsl(registry, graphQlContext)
  dsl.builder()
  return registry
}

class KotlinDataLoaderRegistryDsl
    internal constructor(
        private val registry: DataLoaderRegistry,
        private val graphQlContext: Any?,
    ) {

  /** Register a data loader function under this key. */
  operator fun <K, V> DataLoaderKey<K, V>.invoke(
      dataLoaderFn: DataLoaderFn<K, V>,
  ) {
    registry.register(
        key,
        DataLoader.newDataLoader(
            { ids: List<K>, env ->
              GlobalScope.future(env.coroutineContext()) { dataLoaderFn(ids) }
            },
            dataLoaderOptions(),
        ))
  }

  /** Register a mapped data loader function under this kye. */
  infix fun <K, V> DataLoaderKey<K, V>.mapped(
      dataLoaderFn: MappedDataLoaderFn<K, V>,
  ) {
    registry.register(
        key,
        DataLoader.newMappedDataLoader(
            { ids: Set<K>, env ->
              GlobalScope.future(env.coroutineContext()) { dataLoaderFn(ids) }
            },
            dataLoaderOptions(),
        ))
  }

  private fun dataLoaderOptions() =
      DataLoaderOptions.newOptions().setBatchLoaderContextProvider { graphQlContext }!!

  private fun BatchLoaderEnvironment.coroutineContext() =
      (getContext<Any?>() as? CoroutineScope)?.coroutineContext ?: EmptyCoroutineContext
}
