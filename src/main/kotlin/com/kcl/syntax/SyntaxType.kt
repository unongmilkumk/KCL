package com.kcl.syntax

enum class SyntaxType {
    NUMBER_TOKEN,
    WHITESPACE_TOKEN,
    PLUS_TOKEN,
    MINUS_TOKEN,
    STAR_TOKEN,
    SLASH_TOKEN,
    BANG_TOKEN,
    AMPERSAND_AMPERSAND_TOKEN,
    PIPE_PIPE_TOKEN,
    EQUALS_EQUALS_TOKEN,
    BANG_EQUALS_TOKEN,
    OPEN_PAREN_TOKEN,
    CLOSE_PAREN_TOKEN,
    ERROR_TOKEN,
    ENDING_TOKEN,
    IDENTIFIER_TOKEN,

    NUMBER_EXPRESSION,
    BINARY_EXPRESSION,
    PAREN_EXPRESSION,
    UNARY_EXPRESSION,

    TRUE_KEYWORD,
    FALSE_KEYWORD
}