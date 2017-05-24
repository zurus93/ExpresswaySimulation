package expresswaysimulation.agents;

import java.util.Random;

import expresswaysimulation.init.ExpresswaySimulationBuilder;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class AutosGenerator {
	
	private int mAutosPerTick;
	private ContinuousSpace<Object> mSpace;
	private Grid<Object> mGrid;

	public AutosGenerator(int autosPerTick, ContinuousSpace<Object> space, Grid<Object> grid) {
		mSpace = space;
		mGrid = grid;
		mAutosPerTick = autosPerTick;
		
	}
	
	@ScheduledMethod(start = 1, interval = 3)
	public void step() {
		// Cars initialization
		for(int i=0;i<mAutosPerTick;i++){
			LanesManager lanesManager = LanesManager.getInstance();
			Context<Object> context = ContextUtils.getContext(this);
			Random rndVelocity = new Random();
		    int velocity = 1 + rndVelocity.nextInt(3);
			Auto auto = ExpresswaySimulationBuilder.getAuto(mSpace, mGrid, velocity, rndVelocity);
		    context.add(auto);
		    Random rndX = new Random();
		    int lane = rndX.nextInt(Params.NUMBER_OF_LANES-4)+2;//zaczynamy tylko na œrodkowym pasie
		    int posX = lanesManager.getLaneX(lane);
			System.out.println(posX);
		    Random rndY = new Random();	   
		    
		    mGrid.moveTo(auto, posX, 0);
		    mSpace.moveTo(auto, posX, 0);
		}
	}
}
