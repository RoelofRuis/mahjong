package state

import model.Node

case class Transition(run: Node => (Node, Option[Transition]))
