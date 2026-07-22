package com.lucasctl.jlox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ParserTest {

  @Test
  void parsesExpression() {
    List<Token> tokens =
        List.of(
            new Token(TokenType.BANG, "!", null, 1),
            new Token(TokenType.LEFT_PAREN, "(", null, 1),
            new Token(TokenType.NUMBER, "1", 1.0, 1),
            new Token(TokenType.MINUS, "-", null, 1),
            new Token(TokenType.NUMBER, "2", 2.0, 1),
            new Token(TokenType.MINUS, "-", null, 1),
            new Token(TokenType.NUMBER, "3", 3.0, 1),
            new Token(TokenType.PLUS, "+", null, 1),
            new Token(TokenType.NUMBER, "4", 4.0, 1),
            new Token(TokenType.STAR, "*", null, 1),
            new Token(TokenType.NUMBER, "5", 5.0, 1),
            new Token(TokenType.RIGHT_PAREN, ")", null, 1),
            new Token(TokenType.EOF, "", null, 1));

    Expr expression = new Parser(tokens).parse();

    assertEquals(
        "(! (group (+ (- (- 1.0 2.0) 3.0) (* 4.0 5.0))))", new AstPrinter().print(expression));
  }
}
