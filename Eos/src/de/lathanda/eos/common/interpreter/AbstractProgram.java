package de.lathanda.eos.common.interpreter;

import java.util.LinkedList;

/**
 * Schnittstelle für Syntaxbaum/Parser.
 * @author Peter (Lathanda) Schneider
 *
 */
public interface AbstractProgram {
	/**
	 * Programm übersetzen (Scanner, Parser)
	 * @param path Relativer Pfad des Programms für Bibliotheken
	 * @throws ParserException Fehler
	 */
	void parse(String path) throws TranslationException;
	/**
	 * Syntaxbaum in Programm übersetzen.
	 * @param machine
	 * @throws TranslationException
	 */
	void compile() throws TranslationException;
	/**
	 * Fehlerlist abfragen.
	 * @return
	 */
	LinkedList<ErrorInformation> getErrors();
	/**
	 * Tokenliste abfragen.
	 * @return
	 */
	LinkedList<InfoToken> getTokenList();
	/**
	 * Quellcode abfragen
	 * @return
	 */
	String getSource();
	/**
	 * Interpreter abfragen.
	 * @return
	 */
	AbstractMachine getMachine();
	/**
	 * Datentyp an eine Position bestimmen.
	 * @param pos
	 * @return
	 */
	AbstractType seekType(int pos);
	/**
	 * Quellcode Layouten.
	 * @return
	 */
	String prettyPrint();
	/**
	 * Hauptprogramm für Visualisierungen abfragen.
	 * @return
	 */
	ProgramSequence getProgram();
	/**
	 * Unterprogramme für Visualisierung abfragen.
	 * @return
	 */
	LinkedList<ProgramUnit> getSubPrograms();
	/**
	 * Liefert zu einer Position die Zeilennummer
	 * @param pos
	 * @return
	 */
	int getLine(int pos);
}