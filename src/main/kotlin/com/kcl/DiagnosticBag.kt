package com.kcl

import com.kcl.syntax.SyntaxType
import java.lang.reflect.Type

class DiagnosticBag : Iterable<Diagnostic> {
    val _diagnostics = arrayListOf<Diagnostic>()

    fun addAll(diagnostics: DiagnosticBag) {
        _diagnostics.addAll(diagnostics._diagnostics)
    }

    override fun iterator(): Iterator<Diagnostic> {
        return _diagnostics.iterator()
    }

    fun report(span : TextSpan, message : String) {
        _diagnostics.add(Diagnostic(span, message))
    }

    fun reportInvalidNumber(textSpan: TextSpan, text: String, type : Type) {
        report(textSpan, "The number $text isn't valid $type")
    }

    fun reportBadCharacter(position: Int, current: Char) {
        report(TextSpan(position, 1), "Error : Bad Character Input : $current")
    }

    fun reportUnexpectedToken(span: TextSpan, type: SyntaxType, type1: SyntaxType) {
        report(span, "Error : Unexpected Token $type, expected $type1")
    }

    fun reportUndefinedUnaryOperator(span: TextSpan, text: String?, type: Type) {
        report(span, "Unary Operator $text is not defined for type $type")
    }

    fun reportUndefinedBinaryOperator(span: TextSpan, text: String?, type: Type, type1: Type) {
        report(span, "Binary Operator $text is not defined for types $type and $type1")
    }
}