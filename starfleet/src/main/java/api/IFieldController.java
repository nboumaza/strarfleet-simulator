package api;

import java.text.ParseException;
import java.util.List;

public interface IFieldController {

	void parseField(List<String> fieldLines) throws ParseException;

	/**
	 * Deactivate a mine while maintaining condition that the activeMines
	 * variables are always the same length.
	 * 
	 * @param mineNumber
	 */
	void destroyMine(int mineNumber);

	/**
	 * Add another mine while maintaining condition that the activeMines
	 * variables are always the same length.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	void addMine(int x, int y, int z);

	/*
	 * Find the indices of all mines on the line defined by x, y (where z can vary)
	 * 
	 * @return list of mine indices
	 */
	List<Integer> findMinesOnXYLine(int x, int y);

	/**
	 * Returns true when there is an active mine with a Z coordinate that sg than 
	 * the shipController's current Z position. 
	 * Z position is decremented after every step.
	 * 
	 * @return true if the shipController passed a mine
	 */
	boolean missedMine(int shipZ);

	List<Integer> getActiveMinesX();

	List<Integer> getActiveMinesY();

	List<Integer> getActiveMinesZ();

	List<Integer> getInactiveMinesX();

	List<Integer> getInactiveMinesY();

	List<Integer> getInactiveMinesZ();

	int getNumMines();

	int getOriginX();

	int getOriginY();

	int getWidth();

	int getHeight();

}