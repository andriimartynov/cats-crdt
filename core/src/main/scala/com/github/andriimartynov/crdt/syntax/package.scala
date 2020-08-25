package com.github.andriimartynov.crdt

package object syntax {
  object all      extends AllSyntax
  object counter  extends CounterSyntax with CounterSyntaxBinCompat0
  object kvs      extends KeyValueStoreSyntax
  object register extends RegisterSyntax
  object set      extends SetSyntax with SetSyntaxBinCompat0

}
