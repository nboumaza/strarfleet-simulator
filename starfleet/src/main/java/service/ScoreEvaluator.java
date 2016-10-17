package service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import domain.Field;
import domain.Script;
import domain.Ship;


public class ScoreEvaluator {

	private static final char DOT_CELL = '.';
	private static final char MISSED_MINE = '*';

	private Field field;
	private Ship ship;
	private Script script;
	List<String> commands;
	private int stepNum = 0;

	public ScoreEvaluator(List<String> fieldLines, List<String> scriptLines) {
		field = new Field();
		field.processGridLines(fieldLines);
		script = new Script();
		script.processScriptLines(scriptLines);
		ship = new Ship(field);
		stepNum += 1;
	}

	public void run() {
		// while we have yet to perform all commands and there are still active
		// mines and no mines have been missed

		do {
			System.out.println("Step " + stepNum);
			printFieldGrid();
			System.out.println();
			commands = script.getStepCommands(stepNum - 1);
			printCommandLine(commands);
			ship.processStepCommands(commands);
			System.out.println("");
			printFieldGrid();
			System.out.println();
			stepNum++;
		}while(moreToProcess());
		printScore();
	}

	private void printFieldGrid() {
	/*	// determine the minimum size of the grid 
		// the ship is positioned in the center
		int sizeX = 0, sizeY = 0;
		for (int m = 0; m < field.getMinesX().size(); m++) {
			if (Math.abs(field.getMinesX().get(m)) > sizeX) {
				sizeX = Math.abs(field.getMinesX().get(m) - ship.getPositionX());
			}
			if (Math.abs(field.getMinesY().get(m)) > sizeY) {
				sizeY = Math.abs(field.getMinesY().get(m) - ship.getPositionY());
			}
		}
		char[][] mineField = new char[2 * sizeY + 1][2 * sizeX + 1];
		for (int r = 0; r < mineField.length; r++) {
			for (int c = 0; c < mineField[0].length; c++) {
				mineField[r][c] = DOT_CELL;
			}
		}
		
		int relY, relX;
		for (int m = 0; m < field.getMinesX().size(); m++) {
			relY = transformY(field.getMinesY().get(m), sizeY);
			relX = transformX(field.getMinesX().get(m), sizeX);
			mineField[relY][relX] = distanceToChar(ship.getPositionZ() - field.getMinesZ().get(m));
		}
		*/
		char[][] mineField = this.createGridRepresentation();
		for (int r = 0; r < mineField.length; r++) {
			System.out.println(new String(mineField[r]));
		}

	}

	
	 /**
     * Produces a two-dimensional array representing the cuboid
     * by creating an array and then updating the array with
     * the locations of the remaining mines
     * @return
     */
    private char[][] createGridRepresentation() {
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        // determine the current bounds of the cuboid relative to the center
        
        ArrayList<Integer> minesX = field.getMinesX();
        ArrayList<Integer> minesY = field.getMinesY();
        ArrayList<Integer> minesZ = field.getMinesZ();
        
        for(Integer i:minesX) {
            if(minesY.get(i) < minY)
                minY = minesY.get(i);
            if(minesY.get(i) > maxY)
                maxY = minesY.get(i);
            if(minesX.get(i) < minX)
                minX = minesX.get(i);
            if(minesX.get(i) > maxX)
                maxX = minesX.get(i);
        }

        // infer the x and y bounds of the cuboid
        int width = 1 + 2 * Math.max(Math.abs(minX), Math.abs(maxX));
        int height = 1 + 2 * Math.max(Math.abs(minY), Math.abs(maxY));
        // make certain that the bounds are renderable with a center
        if(width % 2 == 0)
            width++;
        if(height % 2 == 0)
            height++;

        // fill the representation of the cuboid with the default empty value
        char[][] gridRepresentation = new char[height][width];
        for(int i = 0; i < gridRepresentation.length; i++) {
            for(int j = 0; j < gridRepresentation[i].length; j++) {
                gridRepresentation[i][j] = DOT_CELL;
            }
        }

        // update the cells with a mine occupying them with their representations
        for(Integer i: minesX) {
            gridRepresentation[minesY.get(i) + height / 2][minesX.get(i) + width / 2] =                 
            distanceToChar(ship.getPositionZ() - minesZ.get(i));
        }
        return gridRepresentation;
    }
	
	/**
	 * Convert the Z distance between the ship and the mine into a character
	 * 
	 * @param d
	 * @return char matching [a-zA-Z] or "*"
	 */
	private char distanceToChar(int d) {
		if (d <= 26 && d >= 1) {
			// [a-z]
			return (char) (d + 96);
		} else if (d > 26 && d <= 52) {
			// [A - Z]
			return (char) (d + 38);
		} else {
			return MISSED_MINE;
		}
	}

	private boolean moreToProcess(){
		return (
				stepNum <= this.script.getStepsCount()
				&&
				field.getRemaingMineCount() > 0
				&&
				! field.passedMine(ship.getPositionZ())			
				);			
	}
	private boolean done() {
		//TDOD clean
		boolean b1= stepNum > this.script.getStepsCount();
		boolean b2 = field.getRemaingMineCount() == 0;
		boolean b3 = field.passedMine(ship.getPositionZ());		
		//(stepNum <= steps.length && activeMinesX.size() > 0 && !hasPassedMine());
		return b1 || b2 || b3;
		
		/*return (stepNum > this.script.getStepsCount() || field.getRemaingMineCount() == 0
				|| field.passedMine(ship.getPositionZ()));*/
	}

	private void printScore() {
		if (field.passedMine(ship.getPositionZ()))
			System.out.println("fail (0)");
		else if (field.getRemaingMineCount() > 0 && stepNum == script.getStepsCount() + 1)
			System.out.println("fail (0)");
		else if (field.getRemaingMineCount() == 0 && stepNum == script.getStepsCount())
			System.out.println("pass (1)");

		else {
			int score = field.getDeployedMineCount() * 10;
			score = score - Math.min(5 * field.getDeployedMineCount(), 5 * ship.getShotsCount());
			score = score - Math.min(2 * ship.getMovesCount(), 3 * field.getDeployedMineCount());
			System.out.println("pass (" + score + ")");
		}

	}

	/**
	 * Transforms a point in (0,0) coordinate system to one used by
	 * printMineField() function.
	 * 
	 * @param y
	 * @param maxY
	 * @return
	 */
	private int transformY(int y, int maxY) {
		return y - (ship.getPositionY() - maxY);
	}

	/**
	 * Transforms a point in (0,0) coordinate system to one used by
	 * printMineField() function.
	 * 
	 * @param x
	 * @param maxX
	 * @return
	 */
	private int transformX(int x, int maxX) {
		return x - (ship.getPositionX() - maxX);
	}

	private void printCommandLine(List<String> commands) {
		System.out.println(commands.stream().collect(Collectors.joining(" ")));
	}

}
