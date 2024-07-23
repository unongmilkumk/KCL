package com.kcl

import com.kcl.binding.Binder
import com.kcl.syntax.SyntaxTree

class Compilation(val syntax: SyntaxTree) {
    fun evaluate() : EvaluationResult {
        val binder = Binder()
        val boundExpression = binder.bindExpression(syntax.root)

        val diagnostics = syntax.diagnostics + binder.diagnostics
        if (diagnostics.any()) {
            return EvaluationResult(diagnostics, null)
        }

        val evaluator = Evaluator(boundExpression)
        val value = evaluator.evaluate()

        return EvaluationResult(arrayListOf<Diagnostic>().asIterable(), value)
    }
}