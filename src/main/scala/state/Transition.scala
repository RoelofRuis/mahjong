package state

import model.Table

case class Transition(run: Table => (Table, Option[Transition]))
