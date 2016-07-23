/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */
package de.lathanda.assembler.interpreter.javacc;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int DATA = 1;
  /** RegularExpression Id. */
  int CODE = 2;
  /** RegularExpression Id. */
  int LPAREN = 3;
  /** RegularExpression Id. */
  int RPAREN = 4;
  /** RegularExpression Id. */
  int COLON = 5;
  /** RegularExpression Id. */
  int SEPARATOR = 6;
  /** RegularExpression Id. */
  int NUMBER = 7;
  /** RegularExpression Id. */
  int ADDRESS = 8;
  /** RegularExpression Id. */
  int INC = 9;
  /** RegularExpression Id. */
  int DEC = 10;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 16;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 17;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT2 = 18;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 19;
  /** RegularExpression Id. */
  int DECIMAL_LITERAL = 20;
  /** RegularExpression Id. */
  int HEXADECIMAL_LITERAL = 21;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 22;
  /** RegularExpression Id. */
  int EXPONENT = 23;
  /** RegularExpression Id. */
  int STRING_LITERAL = 24;
  /** RegularExpression Id. */
  int IDENTIFIER = 25;
  /** RegularExpression Id. */
  int LETTER = 26;
  /** RegularExpression Id. */
  int DIGIT = 27;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"data\"",
    "\"code\"",
    "\"(\"",
    "\")\"",
    "\":\"",
    "\",\"",
    "\"#\"",
    "\"$\"",
    "\"+\"",
    "\"-\"",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<SINGLE_LINE_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "<MULTI_LINE_COMMENT2>",
    "<INTEGER_LITERAL>",
    "<DECIMAL_LITERAL>",
    "<HEXADECIMAL_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<STRING_LITERAL>",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
  };

}