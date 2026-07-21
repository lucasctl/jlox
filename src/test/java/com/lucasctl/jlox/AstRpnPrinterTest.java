package com.lucasctl.jlox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AstRpnPrinterTest {

  @Test
  void printsExpressionInReversePolishNotation() {
    Expr expression =
        new Expr.Binary(
            new Expr.Unary(new Token(TokenType.MINUS, "-", null, 1), new Expr.Literal(123)),
            new Token(TokenType.STAR, "*", null, 1),
            new Expr.Grouping(new Expr.Literal(45.67)));

    assertEquals("123 - 45.67 *", new AstRpnPrinter().print(expression));
  }

  @Test
  void printsNullLiteralAsNil() {
    assertEquals("nil", new AstRpnPrinter().print(new Expr.Literal(null)));
  }
}
