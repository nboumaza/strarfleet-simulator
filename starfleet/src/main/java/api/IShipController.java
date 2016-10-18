package api;

public interface IShipController {

	int getOffsetZ();

	int getOffsetX();

	int getOffsetY();

	int getMoveCMDs();

	int getShotsFired();

	boolean isFireCommand(String c);

	boolean isMoveCommand(String c);

	/**
	 * 
	 * @param c fire according to a valid pattern
	 */
	void fire(String c);

	void move(String c);

}