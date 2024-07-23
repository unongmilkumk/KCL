package com.kcl.binding

import java.lang.reflect.Type

class BoundUnaryExpression(val operator : BoundUnaryOperator, val operand : BoundExpression) : BoundExpression{
    override var kind: BoundNodeKind
        get() = BoundNodeKind.UnaryExpression
        set(value) {}
    override var type: Type
        get() = operator.resultType
        set(value) {}
}