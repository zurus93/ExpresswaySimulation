package expresswaysimulation.agents;

import java.util.Random;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

/**
 * Class representing gate at the expressway.
 * It is immobile and is the place where cars make payment for crossing.
 * @author FUKSIK
 *
 */
public class Gate {
	
	private ContinuousSpace<Object> mSpace;
	private Grid<Object> mGrid;
	private Random rndPaymentTime;
	
	public Gate(ContinuousSpace<Object> space, Grid<Object> grid) {
		mSpace = space;
		mGrid = grid;
		rndPaymentTime = new Random();
	}
	
	public int getAwaitingTime(){
		return 10 + rndPaymentTime.nextInt(10);
	}
}
