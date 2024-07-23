package com.kcl.binding

import java.lang.reflect.Type

class BoundLiteralExpression(val value : Any) : BoundExpression{
    override var kind: BoundNodeKind
        get() = BoundNodeKind.LiteralExpression
        set(value) {}
    override var type: Type
        get() = value::class.java
        set(value) {}
}