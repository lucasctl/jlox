package com.lucasctl.jlox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class InterpreterTest {
  @Test
  void evaluatesExpression() {
    assertInterpretsTo("1 + 2 * 3", 7.0);
  }

  @Test
  void evaluatesConditionalExpression() {
    assertInterpretsTo("false ? 1 : false ? 2 : 3", 3.0);
  }

  private void assertInterpretsTo(String source, Object expected) {
    Expr expression = new Parser(new Scanner(source).scanTokens()).parse();
    assertEquals(expected, new Interpreter().interpret(expression));
  }
}
