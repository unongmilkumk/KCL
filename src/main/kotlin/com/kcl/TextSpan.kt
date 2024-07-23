package com.kcl

class TextSpan(val start : Int, val length : Int) {
    val end : Int
        get() = start + length
}