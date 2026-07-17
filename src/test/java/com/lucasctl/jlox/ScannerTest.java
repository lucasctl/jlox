package com.lucasctl.jlox;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ScannerTest {

    @Test
    void emptySourceProducesOnlyEof() {
        List<Token> tokens = new Scanner("").scanTokens();

        assertEquals(1, tokens.size());
        assertToken(tokens.getFirst(), TokenType.EOF, "", null, 1);
    }

    @Test
    void scansPunctuationAndOperators() {
        List<Token> tokens = new Scanner("(){},.-+;*/ != == <= >=").scanTokens();

        assertEquals(List.of(
                        TokenType.LEFT_PAREN,
                        TokenType.RIGHT_PAREN,
                        TokenType.LEFT_BRACE,
                        TokenType.RIGHT_BRACE,
                        TokenType.COMMA,
                        TokenType.DOT,
                        TokenType.MINUS,
                        TokenType.PLUS,
                        TokenType.SEMICOLON,
                        TokenType.STAR,
                        TokenType.SLASH,
                        TokenType.BANG_EQUAL,
                        TokenType.EQUAL_EQUAL,
                        TokenType.LESS_EQUAL,
                        TokenType.GREATER_EQUAL,
                        TokenType.EOF),
                tokens.stream().map(token -> token.type).toList());
    }

    @Test
    void ignoresCommentsAndTracksLines() {
        List<Token> tokens = new Scanner("+ // ignored\n-").scanTokens();

        assertAll(
                () -> assertToken(tokens.getFirst(), TokenType.PLUS, "+", null, 1),
                () -> assertToken(tokens.get(1), TokenType.MINUS, "-", null, 2),
                () -> assertToken(tokens.get(2), TokenType.EOF, "", null, 2)
        );
    }

    @Test
    void scansStringLiteral() {
        List<Token> tokens = new Scanner("\"hello world\"").scanTokens();

        assertAll(
                () -> assertToken(tokens.getFirst(), TokenType.STRING,
                        "\"hello world\"", "hello world", 1),
                () -> assertEquals(TokenType.EOF, tokens.get(1).type)
        );
    }

    private static void assertToken(
            Token token, TokenType type, String lexeme, Object literal, int line) {
        assertAll(
                () -> assertEquals(type, token.type),
                () -> assertEquals(lexeme, token.lexeme),
                () -> {
                    if (literal == null) {
                        assertNull(token.literal);
                    } else {
                        assertEquals(literal, token.literal);
                    }
                },
                () -> assertEquals(line, token.line)
        );
    }
}
