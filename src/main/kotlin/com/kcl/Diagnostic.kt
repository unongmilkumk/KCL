package com.kcl

class Diagnostic(val span : TextSpan, val message : String) {
    override fun toString(): String = message
}