package com.lucasctl.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

class GenerateAst {
  private static final String BASE_NAME = "Expr";

  static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    String outputDir = args[0];

    defineAst(
        outputDir,
        Arrays.asList(
            "Binary   : Expr left, Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal  : Object value",
            "Unary    : Token operator, Expr right",
            "Conditional: Expr condition, Expr thenBranch, Expr elseBranch"));
  }

  private static void defineAst(String outputDir, List<String> types) throws IOException {
    String path = outputDir + "/" + BASE_NAME + ".java";
    PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

    writer.println("package com.lucasctl.jlox;");
    writer.println();
    writer.println("abstract class " + BASE_NAME + " {");
    defineVisitor(writer, types);
    // The base accept() method.
    writer.println();
    writer.println("  abstract <R> R accept(Visitor<R> visitor);");

    for (String type : types) {
      String[] parts = type.split(":");
      String className = parts[0].trim();
      String fields = parts[1].trim();
      defineType(writer, className, fields);
    }
    writer.println("}");
    writer.close();
  }

  private static void defineType(PrintWriter writer, String className, String fieldList) {
    writer.println("  static class " + className + " extends " + BASE_NAME + " {");

    // Constructor.
    writer.println("    " + className + "(" + fieldList + ") {");

    // Store parameters in fields.
    String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("  }");

    // Visitor pattern.
    writer.println();
    writer.println("    @Override");
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" + className + BASE_NAME + "(this);");
    writer.println("    }");

    // Fields.
    writer.println();
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println("  }");
  }

  private static void defineVisitor(PrintWriter writer, List<String> types) {
    writer.println("  interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println(
          "    R visit"
              + typeName
              + BASE_NAME
              + "("
              + typeName
              + " "
              + BASE_NAME.toLowerCase()
              + ");");
    }

    writer.println("  }");
  }
}
