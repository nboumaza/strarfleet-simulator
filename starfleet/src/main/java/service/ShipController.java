package service;

import java.util.Arrays;
import java.util.List;

import api.IFieldController;
import api.IShipController;

public class ShipController implements IShipController {

	// valid list of commands till I move to enum....
	private static final List<String> MOVE_CMDS = Arrays.asList("north", "south", "east", "west");
	private static final List<String> FIRE_CMDS = Arrays.asList("alpha", "beta", "gamma", "delta");

	private int offsetX, offsetY, offsetZ;
	private int moveCMDs;
	private int shotsFired;
	private IFieldController fieldController;

	public ShipController(IFieldController fieldController) {
		this.fieldController = fieldController;
		// set x,y coordinates to center of fieldController
		offsetX = fieldController.getOriginX();
		offsetY = fieldController.getOriginY();
		offsetZ = 0;
		shotsFired = 0;
		moveCMDs = 0;
	}

	/* (non-Javadoc)
	 * @see service.IShipController#getOffsetZ()
	 */
	@Override
	public int getOffsetZ() {
		return offsetZ;
	}

	/* (non-Javadoc)
	 * @see service.IShipController#getOffsetX()
	 */
	@Override
	public int getOffsetX() {
		return offsetX;
	}

	/* (non-Javadoc)
	 * @see service.IShipController#getOffsetY()
	 */
	@Override
	public int getOffsetY() {
		return offsetY;
	}

	
	/* (non-Javadoc)
	 * @see service.IShipController#getMoveCMDs()
	 */
	@Override
	public int getMoveCMDs() {
		return moveCMDs;
	}

	/* (non-Javadoc)
	 * @see service.IShipController#getShotsFired()
	 */
	@Override
	public int getShotsFired() {
		return shotsFired;
	}

	
	/* (non-Javadoc)
	 * @see service.IShipController#isFireCommand(java.lang.String)
	 */
	@Override
	public boolean isFireCommand(String c) {
		if (FIRE_CMDS.contains(c))
			return true;

		return false;
	}

	/* (non-Javadoc)
	 * @see service.IShipController#isMoveCommand(java.lang.String)
	 */
	@Override
	public boolean isMoveCommand(String c) {
		if (MOVE_CMDS.contains(c))
			return true;

		return false;
	}

	
	private void fireAlpha() {

		fieldController.findMinesOnXYLine(offsetX + 1, offsetY + 1).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX + 1, offsetY - 1).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX - 1, offsetY + 1).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX - 1, offsetY - 1).stream().forEach(m -> fieldController.destroyMine(m));

		shotsFired++;

	}

	private void fireBeta() {
		fieldController.findMinesOnXYLine(offsetX, offsetY - 1).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX, offsetY + 1).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX + 1, offsetY).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX - 1, offsetY).stream().forEach(m -> fieldController.destroyMine(m));

		shotsFired++;
	}



	private void fireGamma() {
		fieldController.findMinesOnXYLine(offsetX + 1, offsetY).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX - 1, offsetY).stream().forEach(m -> fieldController.destroyMine(m));

		fieldController.findMinesOnXYLine(offsetX, offsetY).stream().forEach(m -> fieldController.destroyMine(m));

		shotsFired++;

	}

	private void fireDelta() {
		
		fieldController.findMinesOnXYLine(offsetX, offsetY - 1).stream().forEach(m -> fieldController.destroyMine(m));		
		fieldController.findMinesOnXYLine(offsetX, offsetY + 1).stream().forEach(m -> fieldController.destroyMine(m));		
		fieldController.findMinesOnXYLine(offsetX, offsetY).stream().forEach(m -> fieldController.destroyMine(m));
		
		shotsFired++;
		
	}


	 /* (non-Javadoc)
	 * @see service.IShipController#fire(java.lang.String)
	 */
	@Override
	public void fire(String c) {
		
		if (c.equals("alpha")) {
			     fireAlpha();			
		} else if (c.equals("beta")) {
			   fireBeta();

		} else if (c.equals("gamma")) {
			 fireGamma();
			
		} else if (c.equals("delta") ) {
			fireDelta();
		}
		//drop regardless of step commands
		offsetZ--;
	}
	/* (non-Javadoc)
	 * @see service.IShipController#move(java.lang.String)
	 */
	@Override
	public void move(String c) {
		//TODO switch to Navigation enum
		if (c.equals("north")) {
			offsetY--;
			moveCMDs++;
		} else if (c.equals("south")) {
			offsetY++;
			moveCMDs++;
		} else if (c.equals("east")) {
			offsetX++;
			moveCMDs++;
		} else if (c.equals("west")) {
			offsetX--;
			moveCMDs++;
		}
		//drop regardless of step commands
		offsetZ--;
		
	}
	/**
	 *  //TODO
	 */
	public enum Navigation {
		NORTH, EAST, SOUTH, WEST;

		public static boolean isValid(String value) {
			try {
				valueOf(value);
				return true;
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}
	}
	
	public enum Torpedo {
		ALPHA, BETA, GAMMA, DELTA;

		public static boolean isValid(String value) {
			try {
				valueOf(value);
				return true;
			} catch (IllegalArgumentException ex) {
				return false;
			}
		}
	}

}
