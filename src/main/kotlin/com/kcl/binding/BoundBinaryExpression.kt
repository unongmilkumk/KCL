package com.kcl.binding

import java.lang.reflect.Type

class BoundBinaryExpression(val left : BoundExpression, val operator: BoundBinaryOperator, val right : BoundExpression) : BoundExpression{
    override var kind: BoundNodeKind
        get() = BoundNodeKind.UnaryExpression
        set(value) {}
    override var type: Type
        get() = operator.resultType
        set(value) {}
}