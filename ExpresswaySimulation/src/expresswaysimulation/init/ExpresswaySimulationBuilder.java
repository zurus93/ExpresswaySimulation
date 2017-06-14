package expresswaysimulation.init;

import java.util.Random;

import expresswaysimulation.agents.A4GoGate;
import expresswaysimulation.agents.Auto;
import expresswaysimulation.agents.AutosGenerator;
import expresswaysimulation.agents.Gate;
import expresswaysimulation.agents.autos.AutoBlue;
import expresswaysimulation.agents.autos.AutoGreen;
import expresswaysimulation.agents.autos.AutoA4GoRed;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
import expresswaysimulation.util.StatisticsManager;
import repast.simphony.context.Context;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.SimpleCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class ExpresswaySimulationBuilder implements ContextBuilder<Object> {
	
	private static Random rand = new Random();

	@Override
	public Context build(Context<Object> context) {
		context.setId ("ExpresswaySimulation");
		StatisticsManager.refresh();
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory (null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
		        new SimpleCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 
		        Params.GRID_WIDTH, Params.GRID_HEIGHT);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
                new GridBuilderParameters<Object>(new WrapAroundBorders(),
                        new SimpleGridAdder<Object>(), true, Params.GRID_WIDTH, Params.GRID_HEIGHT));
		
		LanesManager lanesManager = LanesManager.getInstance();
		Random rand = new Random();

		context.add(new AutosGenerator(10, space, grid));		

		int[] A4GoIndices = Params.getA4GoGatesIndices();
		for (int i = 0, j = 0; i < Params.getLanesNumber(); ++i) {
		    Gate gate;
		    if (A4GoIndices == null) {
		        gate = new Gate(space, grid);
		    } else if (j < A4GoIndices.length && A4GoIndices[j] == i) {
		        gate = new A4GoGate(space, grid);
		        j++;
		    } else {
		        gate = new Gate(space, grid);
		    }
		    
		    context.add(gate);
		    
		    grid.moveTo(gate, lanesManager.getLaneX(i), Params.END_POSITION);
		    space.moveTo(gate, lanesManager.getLaneX(i), Params.END_POSITION);
		}
		
		return context ;
	}

	public static Auto getAuto(ContinuousSpace<Object> space, Grid<Object> grid, int lane) {
		double std = Params.getVelocityStd();
		int mean = Params.getMeanVelocity();
		int velocity = (int) (rand.nextGaussian() * std + mean);
		
		if (velocity <= 0)
			velocity = mean;
		
		if (rand.nextFloat() < Params.getA4GoCarsRatio())
		    return new AutoA4GoRed(space, grid, velocity, lane);
		else {
    		switch (rand.nextInt(3)) {
    		    case 1:
    		        return new AutoGreen(space, grid, velocity, lane);
    		    case 2:
    		        return new AutoBlue(space, grid, velocity, lane);
    			default: 
    				return new Auto(space, grid, velocity, lane);
    		}
		}		
	}
}
