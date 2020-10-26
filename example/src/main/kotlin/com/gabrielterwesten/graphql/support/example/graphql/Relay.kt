package com.gabrielterwesten.graphql.support.example.graphql

import com.gabrielterwesten.graphql.support.globalid.GlobalId

open class NodeId<T>(id: T) : GlobalId<T>(id)

interface Node {
  val id: NodeId<*>
}

interface MutationInput {
  val clientRequestId: String?
}

interface MutationPayload {
  val clientRequestId: String?
}
