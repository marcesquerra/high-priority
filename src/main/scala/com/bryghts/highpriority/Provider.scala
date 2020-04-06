package com.bryghts.highpriority

final case class Provider[P /* <: Priority*/, T](provide: T)
