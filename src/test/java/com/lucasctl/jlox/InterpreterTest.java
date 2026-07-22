package com.lucasctl.jlox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class InterpreterTest {
  private final PrintStream standardError = System.err;

  @AfterEach
  void resetErrorState() {
    System.setErr(standardError);
    Jlox.hadError = false;
  }

  @Test
  void printsExpression() {
    assertEquals("(+ 1.0 (* 2.0 3.0))", new Interpreter().run("1 + 2 * 3"));
  }

  @Test
  void printsCommaExpression() {
    assertEquals("(, (, 1.0 (+ 2.0 3.0)) 4.0)", new Interpreter().run("1, 2 + 3, 4"));
  }

  @Test
  void printsNestedConditionalExpression() {
    assertEquals(
        "(?: true 1.0 (?: false 2.0 3.0))", new Interpreter().run("true ? 1 : false ? 2 : 3"));
  }

  @Test
  void reportsBinaryOperatorWithoutLeftOperand() {
    ByteArrayOutputStream errors = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errors, true, StandardCharsets.UTF_8));

    assertNull(new Interpreter().run("* 2"));
    assertEquals(
        "[line 1] Error at '*': Missing left-hand operand." + System.lineSeparator(),
        errors.toString(StandardCharsets.UTF_8));
  }
}
