package service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import api.IFieldController;
import api.IScoreEvaluator;
import api.IScriptParser;
import api.IShipController;

public class ScoreEvaluator implements IScoreEvaluator {

	int stepNum;
	private List<List<String>> steps;
	
	IShipController shipController;
	IFieldController fieldController;
	//IScriptParser scriptParser;
	int maxX;
	int maxY;

	/**
	 * The Evaluator service is primary responsible for the following tasks:
	 * 
	 * 1. Render the fieldController map representation for each step pre-step execution  
	 * 2. Print the commands of the step
	 * 3. For each current step being processed it invokes the ShipController service
	 *    to execute the step commands 
	 * 4. Render the fieldController map representation  post-step execution
	 * 5. Print the final score resulting from the execution of all steps
	 * 
	 * @param fieldController   fieldController service
	 * @param scriptParser  scriptParser reader service
	 */
	public ScoreEvaluator(IFieldController fieldController, IScriptParser scriptParser) {
		this.fieldController = fieldController;
		//this.scriptParser = scriptParser; 
		this.steps = scriptParser.getSteps();
		shipController = new ShipController(fieldController);
		maxX = fieldController.getWidth();
		maxY = fieldController.getHeight();
		stepNum = 1;

	}

	
	/* (non-Javadoc)
	 * @see service.IScoreEvaluator#run()
	 */
	@Override
	public void run() {
		do {
			System.out.println("Step " + stepNum);
			System.out.println("");
			printMineField();
			System.out.println("");
			printCommand(stepNum);
			executeStepCommands(stepNum);
			System.out.println("");
			printMineField();
			System.out.println("");
			stepNum++;
		} while (stillProcessing());
	
		printScore();
	}
	

	boolean stillProcessing(){
		return (
				stepNum <= steps.size() 
				&& 
				fieldController.getActiveMinesX().size() > 0
				&& 
				!fieldController.missedMine(shipController.getOffsetZ())
				);
	}
	
	/**
	 * 
	 */
	private void printCommand(int StepNum) {
		
		System.out.println(steps.get(stepNum-1).stream().collect(Collectors.joining(" ")));
	}

	/**
	 * 
	 */
	private void printScore() {
		if (fieldController.missedMine(shipController.getOffsetZ())) {
			System.out.println("fail (0)");
		} 
		else 
			if (fieldController.getActiveMinesX().size() > 0 && stepNum == steps.size() + 1) {
				System.out.println("fail (0)");
			} 
			else if (fieldController.getActiveMinesX().size() == 0 && stepNum == steps.size()) {
				System.out.println("pass (1)");
			} 
			else {
				int score = fieldController.getNumMines() * 10;
				score = score - Math.min(5 * fieldController.getNumMines(), 5 * shipController.getShotsFired());
				score = score - Math.min(2 * shipController.getShotsFired(), 3 * fieldController.getNumMines());
				System.out.println("pass (" + score + ")");
		}

	}

	/**
	 * Executes the command(s) read by the scriptParser service 
	 * according to to their respective type: fire or move
	 * 
	 * @param stepNum current step being processed
	 */
	private void executeStepCommands(int stepNum) {
		
		for (String c : steps.get(stepNum-1)) {
			
			if(shipController.isMoveCommand(c))
	     		 shipController.move(c);
			else 
				if(shipController.isFireCommand(c))
					shipController.fire(c);			 
		}

	}
	
	
	/**
	 * Convert the distance to the mine into a character per strafleet-miner instructions
	 * 
	 * @param d
	 * @return
	 */
	private char intToChar(int d) {
		if (d <= 26 && d >= 1) {
			// in range a - z
			return (char) (d + 96);
		} else if (d > 26 && d <= 52) {
			// in range A - Z
			return (char) (d + 38);
		} else {
			return '*';
		}
	}

	/**
	 * Render the field representation for the current step being processed
	 * The ship must be in center
	 */
	private void printMineField() {
		
		//find dimensions of this map layout
		int sizeX = 0, sizeY = 0;
		for (int m = 0; m < fieldController.getActiveMinesX().size(); m++) {
			if (Math.abs(fieldController.getActiveMinesX().get(m)) > sizeX) {
				sizeX = Math.abs(fieldController.getActiveMinesX().get(m) - shipController.getOffsetX());
			}
			if (Math.abs(fieldController.getActiveMinesY().get(m)) > sizeY) {
				sizeY = Math.abs(fieldController.getActiveMinesY().get(m) - shipController.getOffsetY());
			}
		}
		char[][] mineField = new char[2 * sizeY + 1][2 * sizeX + 1];
		
		//initialize with empty cells
		for (int r = 0; r < mineField.length; r++) {
			for (int c = 0; c < mineField[0].length; c++) {
				mineField[r][c] = '.';
			}
		}
		//render mines on Z axis
		int x,y,z=0;
	    
		for (int m = 0; m < fieldController.getActiveMinesX().size(); m++) {
			y= convertY(fieldController.getActiveMinesY().get(m), sizeY);
		    x= convertX(fieldController.getActiveMinesX().get(m),sizeX );
		    z= shipController.getOffsetZ() - fieldController.getActiveMinesZ().get(m);	
		    mineField[y][x] = intToChar(z);
		}
		
		Arrays.asList(mineField).stream().forEach(System.out::println);
	}

	/**
	 * converts a point in (0,0) coordinate system to grid X axis representation
	 * printMineField() function.
	 * 
	 * @param x
	 * @param width
	 * @return
	 */
	private int convertX(int x, int width) {
		return x - (shipController.getOffsetX() - width);
	}

	/**
	 * converts a point in (0,0) coordinate system to grid Y axis representation.
	 *  
	 * @param y
	 * @param height
	 * @return
	 */
	private int convertY(int y, int height) {
		return y - (shipController.getOffsetY() - height);
	}

}