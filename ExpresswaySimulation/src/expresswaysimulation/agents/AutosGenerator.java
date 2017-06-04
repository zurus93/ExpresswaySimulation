package expresswaysimulation.agents;

import java.util.Random;

import expresswaysimulation.init.ExpresswaySimulationBuilder;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
public class AutosGenerator {

	private ContinuousSpace<Object> mSpace;
	private Grid<Object> mGrid;

	public AutosGenerator(int autosPerTick, ContinuousSpace<Object> space, Grid<Object> grid) {
		mSpace = space;
		mGrid = grid;
	}
	
	@ScheduledMethod(start = 1, interval = 3)
	public void step() {
		int autosPerTick = Params.getAutosPerTick();
		int[] startLanesIndices = Params.getStartLanesIndices();
		for(int i=0;i<autosPerTick;i++){
			LanesManager lanesManager = LanesManager.getInstance();
			Context<Object> context = ContextUtils.getContext(this);
			Random rndVelocity = new Random();
			Auto auto = ExpresswaySimulationBuilder.getAuto(mSpace, mGrid);
		    context.add(auto);
		    Random rndX = new Random();
		    int lane = startLanesIndices[rndX.nextInt(startLanesIndices.length)];
		    
		    int posX = lanesManager.getLaneX(lane);
			System.out.println(posX);
		    Random rndY = new Random();	   
		    
		    mGrid.moveTo(auto, posX, 0);
		    mSpace.moveTo(auto, posX, 0);
		}
	}
}
