package com.lucasctl.jlox;

import java.util.List;
import java.util.function.Supplier;

class Parser {
  private static class ParseError extends RuntimeException {}

  private final List<Token> tokens;
  private int current = 0;

  Parser(List<Token> tokens) {
    this.tokens = tokens;
  }

  Expr parse() {
    try {
      return expression();
    } catch (ParseError error) {
      return null;
    }
  }

  private Expr expression() {
    return comma();
  }

  private Expr comma() {
    missingLeftOperand(this::conditional, TokenType.COMMA);
    return binary(this::conditional, TokenType.COMMA);
  }

  private Expr conditional() {
    Expr expr = equality();
    if (match(TokenType.QUESTION)) {
      Expr thenBranch = expression();
      consume(TokenType.COLON, "Expect ':' after then branch of conditional expression.");
      Expr elseBranch = conditional();
      return new Expr.Conditional(expr, thenBranch, elseBranch);
    }
    return expr;
  }

  private Expr equality() {
    missingLeftOperand(this::comparison, TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL);
    return binary(this::comparison, TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL);
  }

  private Expr comparison() {
    missingLeftOperand(
        this::term,
        TokenType.GREATER,
        TokenType.GREATER_EQUAL,
        TokenType.LESS,
        TokenType.LESS_EQUAL);
    return binary(
        this::term,
        TokenType.GREATER,
        TokenType.GREATER_EQUAL,
        TokenType.LESS,
        TokenType.LESS_EQUAL);
  }

  private Expr term() {
    missingLeftOperand(this::factor, TokenType.PLUS);
    return binary(this::factor, TokenType.MINUS, TokenType.PLUS);
  }

  private Expr factor() {
    missingLeftOperand(this::unary, TokenType.SLASH, TokenType.STAR);
    return binary(this::unary, TokenType.SLASH, TokenType.STAR);
  }

  private void missingLeftOperand(Supplier<Expr> rightOperand, TokenType... operators) {
    if (match(operators)) {
      Token operator = previous();
      rightOperand.get();
      throw error(operator, "Missing left-hand operand.");
    }
  }

  private Expr binary(Supplier<Expr> operand, TokenType... operators) {
    Expr expr = operand.get();

    while (match(operators)) {
      Token operator = previous();
      Expr right = operand.get();
      expr = new Expr.Binary(expr, operator, right);
    }

    return expr;
  }

  private Expr unary() {
    if (match(TokenType.BANG, TokenType.MINUS)) {
      Token operator = previous();
      Expr right = unary();
      return new Expr.Unary(operator, right);
    }

    return primary();
  }

  private Expr primary() {
    if (match(TokenType.FALSE)) return new Expr.Literal(false);
    if (match(TokenType.TRUE)) return new Expr.Literal(true);
    if (match(TokenType.NIL)) return new Expr.Literal(null);

    if (match(TokenType.NUMBER, TokenType.STRING)) {
      return new Expr.Literal(previous().literal);
    }

    if (match(TokenType.LEFT_PAREN)) {
      Expr expr = expression();
      consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
      return new Expr.Grouping(expr);
    }

    throw error(peek(), "Expect expression.");
  }

  private Token consume(TokenType type, String message) {
    if (check(type)) return advance();

    throw error(peek(), message);
  }

  private ParseError error(Token token, String message) {
    Jlox.error(token, message);
    return new ParseError();
  }

  private void synchronize() {
    advance();

    while (!isAtEnd()) {
      if (previous().type == TokenType.SEMICOLON) return;

      switch (peek().type) {
        case CLASS:
        case FUN:
        case VAR:
        case FOR:
        case IF:
        case WHILE:
        case PRINT:
        case RETURN:
          return;
      }

      advance();
    }
  }

  private boolean match(TokenType... types) {
    for (TokenType type : types) {
      if (check(type)) {
        advance();
        return true;
      }
    }

    return false;
  }

  private boolean check(TokenType type) {
    return peek().type == type;
  }

  private Token advance() {
    if (!isAtEnd()) current++;
    return previous();
  }

  private boolean isAtEnd() {
    return peek().type == TokenType.EOF;
  }

  private Token peek() {
    return tokens.get(current);
  }

  private Token previous() {
    return tokens.get(current - 1);
  }
}
