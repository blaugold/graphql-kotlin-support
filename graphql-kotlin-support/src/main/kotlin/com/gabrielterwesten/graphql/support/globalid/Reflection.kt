package com.gabrielterwesten.graphql.support.globalid

import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.full.allSupertypes
import kotlin.reflect.full.isSuperclassOf

/** Whether this type is a [GlobalId] or a subclass. */
val KType.isGlobalId: Boolean
  get() = (classifier as? KClass<*>)?.let(GlobalId::class::isSuperclassOf) ?: false

/** The type of the id wrapped by this class of [GlobalId]. */
val KClass<out GlobalId<*>>.wrappedIdType: KType
  get() = allSupertypes.find { it.classifier == GlobalId::class }!!.arguments.first().type!!
