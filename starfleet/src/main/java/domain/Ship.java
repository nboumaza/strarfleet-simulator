package domain;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import driver.Simulator;

public class Ship {

	private static final List<String> MOVE_CMDS = Arrays.asList("north", "south", "east", "west");
	private static final List<String> FIRE_CMDS = Arrays.asList("alpha", "beta", "gamma", "delta");

	private final Field field;
	private int x, y, z;
	private int movesCount, shotsCount;

	public Ship(Field field) {
		this.field = field;
		initialize();
	}

	private void initialize() {
		// position in the center of the field
		z = 0;
		x = field.getDimensionX() / 2;
		y = field.getDimensionX() / 2;
		movesCount = 0;
		shotsCount = 0;
	}

	/**
	 * For each commandLine command, executes a navigation or firing strategy
	 * 
	 * @param commands
	 *            command list
	 */
	public void processStepCommands(List<String> commands) {

		commands.stream().forEach(command -> executeCommand(command));

	}

	private void executeCommand(String strategy) {
		if (MOVE_CMDS.contains(strategy))
			move(strategy);
		else if (FIRE_CMDS.contains(strategy))
			fire(strategy);
		else {
			// TODO log "Invalid strategy: "+strategy
			System.exit(Simulator.UNSUPORTED_STRATEGY);
		}
		drop();
	}

	/**
	 * process a valid navigation move and increments the number of navigations
	 * made
	 * 
	 * @param direction
	 */
	private void move(String direction) {

		switch (direction) {
		case "north":
			y -= 1;
			break;
		case "south":
			y += 1;
			break;
		case "east":
			x += 1;
			break;
		case "west":
			x -= 1;
		}

		movesCount += 1;

	}

	/**
	 * fires a shot according to a valid pattern type
	 * 
	 * @param pattern
	 */
	private void fire(String pattern) {
		List<Integer> mines = null;
		switch (pattern) {
		case "alpha": 
			mines = binaryConcat(
					binaryConcat(field.findMinesOnXY(x + 1, y + 1).stream(), field.findMinesOnXY(x + 1, y - 1).stream())
							.stream(),
					binaryConcat(field.findMinesOnXY(x - 1, y + 1).stream(), field.findMinesOnXY(x - 1, y - 1).stream())
							.stream());
			break;

		case "beta": 
			mines = binaryConcat(
					binaryConcat(field.findMinesOnXY(x, y - 1).stream(), field.findMinesOnXY(x, y + 1).stream())
							.stream(),
					binaryConcat(field.findMinesOnXY(x + 1, y).stream(), field.findMinesOnXY(x - 1, y).stream())
							.stream());
			break;

		case "gamma":
			mines = Stream
					.concat(binaryConcat(field.findMinesOnXY(x + 1, y).stream(), field.findMinesOnXY(x - 1, y).stream())
							.stream(), field.findMinesOnXY(x, y).stream())
					.collect(Collectors.toList());
			break;

		case "delta": 
			mines = Stream
					.concat(binaryConcat(field.findMinesOnXY(x, y - 1).stream(), field.findMinesOnXY(x, y + 1).stream())
							.stream(), field.findMinesOnXY(x, y).stream())
					.collect(Collectors.toList());
		}

		field.destroyMines(mines);
		shotsCount += 1;
	}

	private List<Integer> binaryConcat(Stream<Integer> stream, Stream<Integer> stream2) {
		return Stream.concat(stream, stream2).collect(Collectors.toList());
	}

	private void drop() {
		z -= 1;
	}

	public int getPositionX() {
		return x;
	}

	public int getPositionY() {
		return y;
	}

	public int getPositionZ() {
		return z;
	}

	public int getMovesCount() {
		return movesCount;
	}

	public int getShotsCount() {
		return shotsCount;
	}

}
