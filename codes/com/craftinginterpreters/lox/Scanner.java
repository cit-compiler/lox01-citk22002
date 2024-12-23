package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.craftinginterpreters.lox.Lox;

import static com.craftinginterpreters.lox.TokenType.*; 

class Scanner {
  private final String source;
  private final List<Token> tokens = new ArrayList<>();
  private int start = 0;
  private int current = 0;
  private int line = 1;
  private static final Map<String, TokenType> keywords;

  static {
    keywords = new HashMap<>();
    keywords.put("and",    AND);
    keywords.put("class",  CLASS);
    keywords.put("else",   ELSE);
    keywords.put("false",  FALSE);
    keywords.put("for",    FOR);
    keywords.put("fun",    FUN);
    keywords.put("if",     IF);
    keywords.put("nil",    NIL);
    keywords.put("or",     OR);
    keywords.put("print",  PRINT);
    keywords.put("return", RETURN);
    keywords.put("super",  SUPER);
    keywords.put("this",   THIS);
    keywords.put("true",   TRUE);
    keywords.put("var",    VAR);
    keywords.put("while",  WHILE);
  }


  Scanner(String source) {
    this.source = source;
  }
  List<Token> scanTokens() {
    while (!isAtEnd()) {
      // 次の字句の先頭から始める
      start = current;
      scanToken();
      private void scanToken() {
        char c = advance();
        switch (c) {
          case '(': addToken(LEFT_PAREN); break;
          case ')': addToken(RIGHT_PAREN); break;
          case '{': addToken(LEFT_BRACE); break;
          case '}': addToken(RIGHT_BRACE); break;
          case ',': addToken(COMMA); break;
          case '.': addToken(DOT); break;
          case '-': addToken(MINUS); break;
          case '+': addToken(PLUS); break;
          case ';': addToken(SEMICOLON); break;
          case '*': addToken(STAR); break; 
          case '!':
          addToken(match('=') ? BANG_EQUAL : BANG);
          break;
        case '=':
          addToken(match('=') ? EQUAL_EQUAL : EQUAL);
          break;
        case '<':
          addToken(match('=') ? LESS_EQUAL : LESS);
          break;
        case '>':
          addToken(match('=') ? GREATER_EQUAL : GREATER);
          break;
          case '/':
        if (match('/')) {
          // コメントは行末まで続く
          while (peek() != '\n' && !isAtEnd()) advance();
          private char peek() {
            if (isAtEnd()) return '\0';
            return source.charAt(current);
          }
          private boolean isDigit(char c) {
            return c >= '0' && c <= '9';
          } 
          private char peekNext() {
            if (current + 1 >= source.length()) return '\0';
            return source.charAt(current + 1);
          } 
          private boolean isAlpha(char c) {
            return (c >= 'a' && c <= 'z') ||
                   (c >= 'A' && c <= 'Z') ||
                    c == '_';
          }
        
          private boolean isAlphaNumeric(char c) {
            return isAlpha(c) || isDigit(c);
          }
        
        } else {
          addToken(SLASH);
        }
        break;
        
        case ' ':
        case '\r':
        case '\t':
          // 空白を無視する
          break;
  
        case '\n':
          line++;
          break;

          case '"': string(); break;

          default:
          if (isDigit(c)) {
            number();
          } else if (isAlpha(c)) {
            identifier();
          } else {
            Lox.error(line, "Unexpected character.");
          }
        break;
        }
        
        private boolean match(char expected) {
          if (isAtEnd()) return false;
          if (source.charAt(current) != expected) return false;
      
          current++;
          return true;
        }
        private void number() {
          while (isDigit(peek())) advance();

          String text = source.substring(start, current);
          TokenType type = keywords.get(text);
          if (type == null) type = IDENTIFIER;
          addToken(type);

        }
          // 少数部を探して
          if (peek() == '.' && isDigit(peekNext())) {
            // 小数点を消費
            advance();
      
            while (isDigit(peek())) advance();
          }
          private void identifier() {
            while (isAlphaNumeric(peek())) advance();
        
            addToken(IDENTIFIER);
          }
      
          addToken(NUMBER,
              Double.parseDouble(source.substring(start, current)));
        
      }
      private void string() {
        while (peek() != '"' && !isAtEnd()) {
          if (peek() == '\n') line++;
          advance();
        }
    
        if (isAtEnd()) {
          Lox.error(line, "Unterminated string.");
          return;
        }
    
        // The closing ".
        advance();
    
        // Trim the surrounding quotes.
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
      }
    }
    private boolean isAtEnd() {
      return current >= source.length();
    }

    tokens.add(new Token(EOF, "", null, line));
    private char advance() {
      return source.charAt(current++);
    }
  
    private void addToken(TokenType type) {
      addToken(type, null);
    }
  
    private void addToken(TokenType type, Object literal) {
      String text = source.substring(start, current);
      tokens.add(new Token(type, text, literal, line));
    }
  
    return tokens;
  }
}