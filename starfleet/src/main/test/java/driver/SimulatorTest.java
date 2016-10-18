package driver;

//import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
//import org.junit.Test;

public class SimulatorTest {

	Simulator sim;
	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testScenario1() {
		String[] args = new String[] {"field1.txt", "script1.txt" };
		Simulator.main(args);
		//TODO
		//assertThat(result1.txt ) compare output files from expected and actual
	}

}
