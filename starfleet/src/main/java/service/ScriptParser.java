package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import api.IScriptParser;

public class ScriptParser implements IScriptParser {

	private List<List<String>> steps = new ArrayList<List<String>>();
	
	
	public ScriptParser(){		
	}
	
	/* (non-Javadoc)
	 * @see service.IScriptParser#parseScript(java.util.List)
	 */
	@Override
	public void parseScript(List<String> scriptLines) {
		
		scriptLines.stream()
		   .forEach(line -> 
		   				{
		   					List<String> commands = new ArrayList<String>();
		   					commands =  Arrays.asList(line.split("//s+"));
		   					processStepComands(commands);
		   				}
		   				);
	}
	
	private void processStepComands(List<String> commands) {
		  //TODO add predicates to validate the read commands 
		  steps.add(commands);
	}

	/* (non-Javadoc)
	 * @see service.IScriptParser#getSteps()
	 */
	@Override
	public List<List<String>> getSteps() {
		return steps;
	}
	

}
