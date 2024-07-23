package com.kcl.binding

import com.kcl.syntax.SyntaxType
import java.lang.reflect.Type

class BoundBinaryOperator(val syntaxType: SyntaxType, val kind : BoundBinaryOperatorKind, val leftType: Type, val rightType : Type, val resultType : Type) {
    constructor(syntaxType: SyntaxType, kind: BoundBinaryOperatorKind, type: Type): this(syntaxType, kind, type, type, type)
    constructor(syntaxType: SyntaxType, kind: BoundBinaryOperatorKind, type: Type, resultType: Type): this(syntaxType, kind, type, type, resultType)

    companion object {
        val _operators = arrayListOf(
            BoundBinaryOperator(SyntaxType.PLUS_TOKEN, BoundBinaryOperatorKind.ADDICTION, java.lang.Integer::class.java),
            BoundBinaryOperator(SyntaxType.MINUS_TOKEN, BoundBinaryOperatorKind.SUBTRACTION, java.lang.Integer::class.java),
            BoundBinaryOperator(SyntaxType.STAR_TOKEN, BoundBinaryOperatorKind.MULTIPLICATION, java.lang.Integer::class.java),
            BoundBinaryOperator(SyntaxType.SLASH_TOKEN, BoundBinaryOperatorKind.DIVISION, java.lang.Integer::class.java),
            BoundBinaryOperator(SyntaxType.EQUALS_EQUALS_TOKEN, BoundBinaryOperatorKind.EQUALS, java.lang.Integer::class.java, java.lang.Boolean::class.java),
            BoundBinaryOperator(SyntaxType.BANG_EQUALS_TOKEN, BoundBinaryOperatorKind.NOT_EQUALS, java.lang.Integer::class.java, java.lang.Boolean::class.java),

            BoundBinaryOperator(SyntaxType.AMPERSAND_AMPERSAND_TOKEN, BoundBinaryOperatorKind.LOGICAL_AND, java.lang.Boolean::class.java),
            BoundBinaryOperator(SyntaxType.PIPE_PIPE_TOKEN, BoundBinaryOperatorKind.LOGICAL_OR, java.lang.Boolean::class.java),
            BoundBinaryOperator(SyntaxType.EQUALS_EQUALS_TOKEN, BoundBinaryOperatorKind.EQUALS, java.lang.Boolean::class.java),
            BoundBinaryOperator(SyntaxType.BANG_EQUALS_TOKEN, BoundBinaryOperatorKind.NOT_EQUALS, java.lang.Boolean::class.java))


        fun bind(syntaxType: SyntaxType, leftType: Type, rightType: Type) : BoundBinaryOperator? {
            _operators.forEach {
                if (it.syntaxType == syntaxType && it.leftType == leftType && it.rightType == rightType) return it
            }
            return null
        }
    }
}