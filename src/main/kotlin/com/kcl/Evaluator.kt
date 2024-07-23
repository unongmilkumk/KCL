package com.kcl

import com.kcl.binding.*

class Evaluator(private val _root : BoundExpression) {
    fun Any?.asInt(): Int {
        return when (this) {
            is Boolean -> if (this) 1 else 0
            is Number -> this.toInt()
            is String -> this.toIntOrNull() ?: 0
            else -> 0
        }
    }

    fun Any?.asBool() : Boolean {
        return when (this) {
            is Boolean -> this
            is Number -> this.toInt() != 0
            is String -> this.toBoolean()
            else -> false
        }
    }

    fun evaluate() : Any {
        return evaluateExpression(_root)
    }

    private fun evaluateExpression(node : BoundExpression): Any {
        when (node) {
            is BoundLiteralExpression -> return node.value
            is BoundUnaryExpression -> {
                val operand = evaluateExpression(node.operand)
                return when (node.operator.kind) {
                    BoundUnaryOperatorKind.IDENTITY -> {
                        operand.asInt()
                    }

                    BoundUnaryOperatorKind.NEGATION -> {
                        -operand.asInt()
                    }

                    BoundUnaryOperatorKind.LOGICAL_NEGATION -> {
                        !operand.asBool()
                    }

                    else -> throw Exception("Unexpected Unary operator ${node.operator}")
                }
            }
            is BoundBinaryExpression -> {
                val left = evaluateExpression(node.left)
                val right = evaluateExpression(node.right)

                return when (node.operator.kind) {
                    BoundBinaryOperatorKind.ADDICTION -> left.asInt() + right.asInt()
                    BoundBinaryOperatorKind.SUBTRACTION -> left.asInt() - right.asInt()
                    BoundBinaryOperatorKind.MULTIPLICATION -> left.asInt() * right.asInt()
                    BoundBinaryOperatorKind.DIVISION -> left.asInt() / right.asInt()
                    BoundBinaryOperatorKind.LOGICAL_AND -> left.asBool() && right.asBool()
                    BoundBinaryOperatorKind.LOGICAL_OR -> left.asBool() || right.asBool()
                    BoundBinaryOperatorKind.EQUALS -> left == right
                    BoundBinaryOperatorKind.NOT_EQUALS -> left != right
                    else -> throw Exception("Unexpected binary operator ${node.operator}")
                }
            }
            else -> return 0
        }
    }
}