package com.kcl.binding

import com.kcl.syntax.SyntaxType
import java.lang.reflect.Type

class BoundUnaryOperator(val syntaxType: SyntaxType, val kind : BoundUnaryOperatorKind, val operandType: Type, val resultType : Type) {
    constructor(syntaxType: SyntaxType, kind: BoundUnaryOperatorKind, operandType: Type ): this(syntaxType, kind, operandType, operandType)

    companion object {
        val _operators = arrayListOf(BoundUnaryOperator(SyntaxType.BANG_TOKEN, BoundUnaryOperatorKind.LOGICAL_NEGATION, java.lang.Boolean::class.java),
            BoundUnaryOperator(SyntaxType.PLUS_TOKEN, BoundUnaryOperatorKind.IDENTITY, java.lang.Integer::class.java),
            BoundUnaryOperator(SyntaxType.MINUS_TOKEN, BoundUnaryOperatorKind.NEGATION, java.lang.Integer::class.java))

        fun bind(syntaxType: SyntaxType, operandType: Type) : BoundUnaryOperator? {
            _operators.forEach {
                if (it.syntaxType == syntaxType && it.operandType == operandType) return it
            }
            return null
        }
    }
}