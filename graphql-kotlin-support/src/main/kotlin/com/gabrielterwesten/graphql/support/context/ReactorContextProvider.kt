package com.gabrielterwesten.graphql.support.context

import reactor.util.context.Context

interface ReactorContextProvider {
  val reactorContext: Context
}
