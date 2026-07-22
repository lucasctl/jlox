package com.lucasctl.jlox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class InterpreterTest {

  @AfterEach
  void resetErrorState() {
    Jlox.hadError = false;
  }

  @Test
  void scansParsesAndPrintsSource() {
    assertEquals("(+ 1.0 (* 2.0 3.0))", new Interpreter().run("1 + 2 * 3"));
  }
}
