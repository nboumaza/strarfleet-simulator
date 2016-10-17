package domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Script {

	private List<List<String>> steps = new ArrayList<List<String>>();
	
	/**
	 * 
	 * @param scriptLines
	 */
	public void processScriptLines(List<String> scriptLines) {
		
		scriptLines.stream()
	  			   .forEach(line -> 
	  			   				{
	  			   					List<String> commands = new ArrayList<String>();
	  			   					commands =  Arrays.asList(line.split("//s+"));
	  			   					processScriptLine(commands);
	  			   				}
	  			   				);
           	
		 System.out.println();
	}

	private void processScriptLine(List<String> commands) {
		  steps.add(commands);
	}


	/**
	 * 
	 * @return
	 */
	public int getStepsCount() {
		return steps.size();
	}

	/**
	 * 
	 * @param stepNum
	 * @return
	 */
	public List<String> getStepCommands(int stepNum) {
		return steps.get(stepNum);
	}

}
