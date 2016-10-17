package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import driver.Simulator;

public class Field {

	//number of deployed mine per the field file initial input
	private int deployedMineCount = 0;
	//number of remaining active mines- will be updated through out the simulation when a shot is fired
	private int remainingMineCount = 0;
	//field (x,y) Axis dimensions
	private int dimensionX = 0;
	private int dimensionY = 0;
	// represents axis coordinates of remaining active mines
	private ArrayList<Integer> minesX = new ArrayList<Integer>();
	private ArrayList<Integer> minesY = new ArrayList<Integer>();
	private ArrayList<Integer> minesZ = new ArrayList<Integer>();

	public Field() {

	}

	private void initializeDimensions(List<String> rows) {	
		dimensionY = rows.size();
		dimensionX = rows.get(0).length();

		if (dimensionY % 2 == 0 || dimensionX % 2 == 0) {
			// log "Invalid Field dimension Could not determine center of
			// FieldFile", 0);
			System.exit(Simulator.INVALID_FIELD_LAYOUT);
		}
	}

	/**
	 * 
	 * @param rows
	 *            all read lines from an field input file
	 */
	public void processGridLines(List<String> rows) {
		
		initializeDimensions(rows);
		int yAxis = -1;
		rows.stream().forEach(row -> processGridLine(row, yAxis + 1));
	}

	private void processGridLine(String row, int yAxis) {

		if (row.length() != dimensionX) {
			// log "Invalid Field dimension Could not determine center of
			// FieldFile", 0);
			System.exit(Simulator.INVALID_FIELD_LAYOUT);
		}

		int xAxis = -1;
		row.chars().mapToObj(i -> (char) i).forEach(c -> processGridCell(c, yAxis, xAxis + 1));
	}

	private void processGridCell(char c, int y, int x) {

		if (c == '.')
			return;

		if (String.valueOf(c).matches("[A-Z]") || String.valueOf(c).matches("[a-z]"))
			addMine(x, y, mineCharToInt(c));
		else {
			// log invalid character
			System.exit(Simulator.INVALID_FIELD_LAYOUT);
		}

	}

	private void addMine(int x, int y, int z) {
		minesX.add(x);
		minesY.add(y);
		minesZ.add(z);
		deployedMineCount += 1;
		remainingMineCount += 1;

	}
    
	/**
	 * Converts character to ASCII int and translate the result according to the Grid instructions
	 * @See  http://www.asciitable.com
	 * @param c character
	 * @return [1-26]  [a
	 */
	private int mineCharToInt(char c) {
		if ((int) c >= 65 && (int) c <= 90)
			// [A-Z]
			return (-1) * ((int) c - 38);
		else
			// [a - z]
			return (-1) * ((int) c - 96);

	}

	/**
	 * @param x
	 *            X Axis value on the field layout
	 * @param y
	 *            Y Axis value on field Layout
	 * @return list of mine indices located at offset: x, y
	 */
	public List<Integer> findMinesOnXY(int x, int y) {

		return minesX.stream().filter(i -> (i == x && minesY.get(i) == y)).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param mines
	 */
	public void destroyMines(List<Integer> mines) {
		mines.stream().forEach(i -> {
			minesX.remove(i);
			minesY.remove(i);
			minesZ.remove(i);
		}

		);
		remainingMineCount -= 1;
	}

	public boolean passedMine(int shipZ) {
        
		  if (minesX.stream().anyMatch(i -> i >= shipZ)){
			  return true;
		  }
		  return false;
	}

	public ArrayList<Integer> getMinesX() {
		return minesX;
	}

	public ArrayList<Integer> getMinesY() {
		return minesY;
	}

	public ArrayList<Integer> getMinesZ() {
		return minesZ;
	}

	public int getDimensionX() {
		return this.dimensionX;
	}

	public int getDimensionY() {
		return this.dimensionY;
	}

	public int getDeployedMineCount() {
		return this.deployedMineCount;
	}

	public int getRemaingMineCount() {
		return this.remainingMineCount;
	}

}
