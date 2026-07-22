package com.lucasctl.jlox;

final class Interpreter {
  String run(String source) {
    Expr expression = new Parser(new Scanner(source).scanTokens()).parse();
    if (Jlox.hadError) return null;
    return new AstPrinter().print(expression);
  }
}
