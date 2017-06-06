package expresswaysimulation.agents;

import java.util.Random;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class A4GoGate extends Gate {
    private final int mAwaitingTime = 5;
    
	public A4GoGate(ContinuousSpace<Object> space, Grid<Object> grid) {
		super(space, grid);
	}
	
	@Override
	public int getAwaitingTime(){
		return mAwaitingTime;
	}
}
