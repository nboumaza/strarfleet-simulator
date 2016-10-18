package driver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.List;

import api.IFieldController;
import api.IScoreEvaluator;
import api.IScriptParser;
import service.FieldController;
import service.ScoreEvaluator;
import service.ScriptParser;

/**
 * 
 * Main driver for the simulation
 * Takes as input:
 *   	1. FieldController layout map file
 *      2. Commands file
 *
 */
public class Simulator {

	//TODO cleanup the extra stuff
	private static final int INCORRECT_USAGE = 1;
	private static final int INVALID_FIELD_FILE = 2;
	//private static final int INVALID_FIELD_LAYOUT = 3;
	private static final int INVALID_SCRIPT_FILE = 4;
	private static final int IO_EXCEPTION = 5;
	//private static final int INVALID_STRATEGY = 6;
	//private static final int UNSUPORTED_STRATEGY = 7;
	private static final int PARSE_EXECEPTION = 8;

	
	public static void main(String[] args) {

		if (args.length != 2) {
			// TODO log
			System.out.println("usage:  java" + "Simulator <fieldFilePathName> <scriptFilePathName>");
			System.exit(INCORRECT_USAGE);
		}

		File fieldFile = new File(args[0]);
		if (!fieldFile.isFile()) {
			System.out.println("could not find fieldController file: " + fieldFile);
			System.exit(INVALID_FIELD_FILE);
		}

		File scriptFile = new File(args[1]);
		if (!scriptFile.isFile()) {
			// TODO log
			System.out.println("could not find scriptParser file: " + scriptFile);
			System.exit(INVALID_SCRIPT_FILE);
		}

		List<String> fieldLines = null;
		List<String> scriptLines = null;

		try {
			fieldLines = Files.readAllLines(fieldFile.toPath());
			scriptLines = Files.readAllLines(scriptFile.toPath());
		} catch (IOException e) {
			//TODO log
			System.exit(IO_EXCEPTION);
		}

	    IScriptParser scriptParser = null;
		IScoreEvaluator se = null;
	    IFieldController fieldController = null;

		try {
			fieldController = new FieldController();		
			fieldController.parseField(fieldLines);
			scriptParser = new ScriptParser();
			scriptParser.parseScript(scriptLines);
			
		} catch (ParseException e) {
				//TODO log e.printStackTrace();
			   System.exit(PARSE_EXECEPTION);
		}
		
		se = new ScoreEvaluator(fieldController, scriptParser);
		se.run();

	}
}
