package com.gabrielterwesten.graphql.support.schemagen

import com.expediagroup.graphql.execution.GraphQLContext
import reactor.util.context.Context

open class ReactorGraphQlContext(val context: Context?) : GraphQLContext
