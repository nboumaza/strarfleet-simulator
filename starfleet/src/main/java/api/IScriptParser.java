package api;

import java.text.ParseException;
import java.util.List;

public interface IScriptParser {

	/**
	 * Updates commands[] array to reflect contents of scriptParser file.
	 * 
	 * @param scriptLines.get
	 * @throws ParseException
	 */
	void parseScript(List<String> scriptLines);

	List<List<String>> getSteps();

}