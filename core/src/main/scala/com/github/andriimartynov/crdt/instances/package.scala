package com.github.andriimartynov.crdt

package object instances {
  object all      extends AllInstances
  object counter  extends CounterInstances with CounterInstancesBinCompat0
  object int      extends IntInstances
  object map      extends MapInstances
  object register extends RegisterInstances with RegisterInstancesBinCompat0
  object set      extends SetInstances

}
