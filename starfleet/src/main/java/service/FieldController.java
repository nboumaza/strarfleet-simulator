package service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import api.IFieldController;

public class FieldController implements IFieldController {

	List<Integer> activeMinesX;
	List<Integer> activeMinesY;
	List<Integer> activeMinesZ;
	List<Integer> inactiveMinesX;
	List<Integer> inactiveMinesY;
	List<Integer> inactiveMinesZ;
	int numMines;
	int originX, originY;
	private int maxX;
	private int maxY;

	public FieldController() {

		activeMinesX = new ArrayList<Integer>();
		activeMinesY = new ArrayList<Integer>();
		activeMinesZ = new ArrayList<Integer>();
		inactiveMinesX = new ArrayList<Integer>();
		inactiveMinesY = new ArrayList<Integer>();
		inactiveMinesZ = new ArrayList<Integer>();
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#parseField(java.util.List)
	 */
	@Override
	public void parseField(List<String> fieldLines) throws ParseException {
		maxX = fieldLines.get(0).length();
		maxY = fieldLines.size();
		if (maxX % 2 == 0 || maxY % 2 == 0) {
			throw new ParseException("Could not determine center of FieldFile", 0);
		}
		originX = Math.round(maxX / 2);
		originY = Math.round(maxY / 2);

		for (int l = 0; l < fieldLines.size(); l++) {
			String line = fieldLines.get(l);
			if (line.length() != maxX) {
				throw new ParseException("Found lines of different lengths in FieldFile", l);
			}
			for (int c = 0; c < line.length(); c++) {
				if (line.charAt(c) == '.') {
					// Found empty space
				} else if (line.substring(c, c + 1).matches("[a-zA-Z]")) {
					addMine(c, l, charToZ(line.charAt(c)));
					numMines++;
				} else {
					throw new ParseException("Found unexpected character in FieldFile", l);
				}
			}
		}

	}

	/**
	 * @param c
	 *            character in [a-zA-z] as it was already filtered
	 * @return ASCII value converted to position in Z Axis
	 */
	private int charToZ(char c) {
		if ((int) c >= 65 && (int) c <= 90) {
			// [A - Z]
			return -((int) c - 38);
		} else {
			// [a - z]
			return -((int) c - 96);
		}
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#destroyMine(int)
	 */
	@Override
	public void destroyMine(int mineNumber) {
		int x = activeMinesX.remove(mineNumber);
		int y = activeMinesY.remove(mineNumber);
		int z = activeMinesZ.remove(mineNumber);
		inactiveMinesX.add(x);
		inactiveMinesY.add(y);
		inactiveMinesZ.add(z);
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#addMine(int, int, int)
	 */
	@Override
	public void addMine(int x, int y, int z) {
		activeMinesX.add(x);
		activeMinesY.add(y);
		activeMinesZ.add(z);
	}

	/*
	 * Find the indices of all mines on the line defined by x, y (where z can vary)
	 * 
	 * @return list of mine indices
	 */
	/* (non-Javadoc)
	 * @see service.IFieldController#findMinesOnXYLine(int, int)
	 */
	@Override
	public List<Integer> findMinesOnXYLine(int x, int y) {
		
		return (activeMinesX.stream()
				  .filter(i -> (i == x && activeMinesY.get(i) == y))
				  .collect(Collectors.toList()));
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#missedMine(int)
	 */
	@Override
	public boolean missedMine(int shipZ) {
		
		if (activeMinesZ.stream().anyMatch(i -> i >= shipZ)){
			  return true;
		 }
		  
		return false;
		
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getActiveMinesX()
	 */
	@Override
	public List<Integer> getActiveMinesX() {
		return activeMinesX;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getActiveMinesY()
	 */
	@Override
	public List<Integer> getActiveMinesY() {
		return activeMinesY;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getActiveMinesZ()
	 */
	@Override
	public List<Integer> getActiveMinesZ() {
		return activeMinesZ;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getInactiveMinesX()
	 */
	@Override
	public List<Integer> getInactiveMinesX() {
		return inactiveMinesX;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getInactiveMinesY()
	 */
	@Override
	public List<Integer> getInactiveMinesY() {
		return inactiveMinesY;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getInactiveMinesZ()
	 */
	@Override
	public List<Integer> getInactiveMinesZ() {
		return inactiveMinesZ;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getNumMines()
	 */
	@Override
	public int getNumMines() {
		return numMines;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getOriginX()
	 */
	@Override
	public int getOriginX() {
		return originX;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getOriginY()
	 */
	@Override
	public int getOriginY() {
		return originY;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getWidth()
	 */
	@Override
	public int getWidth() {
		return maxX;
	}

	/* (non-Javadoc)
	 * @see service.IFieldController#getHeight()
	 */
	@Override
	public int getHeight() {
		return maxY;
	}
}
