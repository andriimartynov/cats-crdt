package com.github.andriimartynov.crdt.kernel.sets

import com.github.andriimartynov.crdt.kernel.CRDT

trait SetCRDT[T] extends CRDT[Set[T], T]
