package com.lucasctl.jlox;

class AstRpnPrinter implements Expr.Visitor<String> {

  String print(Expr expr) {
    return expr.accept(this);
  }

  @Override
  public String visitBinaryExpr(Expr.Binary expr) {
    return print(expr.left) + " " + print(expr.right) + " " + expr.operator.lexeme;
  }

  @Override
  public String visitGroupingExpr(Expr.Grouping expr) {
    return print(expr.expression);
  }

  @Override
  public String visitLiteralExpr(Expr.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }

  @Override
  public String visitUnaryExpr(Expr.Unary expr) {
    return print(expr.right) + " " + expr.operator.lexeme;
  }

  @Override
  public String visitConditionalExpr(Expr.Conditional expr) {
    return print(expr.condition)
        + " "
        + print(expr.thenBranch)
        + " "
        + print(expr.elseBranch)
        + " ?:";
  }
}
