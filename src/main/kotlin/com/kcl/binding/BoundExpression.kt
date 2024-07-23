package com.kcl.binding

import com.kcl.syntax.SyntaxType
import java.lang.reflect.Type

interface BoundExpression : BoundNode {
    var type : Type
}