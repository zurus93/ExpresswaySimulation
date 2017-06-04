package expresswaysimulation.init;

import java.util.Random;

import expresswaysimulation.agents.Auto;
import expresswaysimulation.agents.AutosGenerator;
import expresswaysimulation.agents.Gate;
import expresswaysimulation.agents.autos.AutoBlue;
import expresswaysimulation.agents.autos.AutoGreen;
import expresswaysimulation.agents.autos.AutoRed;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
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
		System.out.println((new Integer[]{}).getClass());
		context.setId ("ExpresswaySimulation");
		
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

		context.add(new AutosGenerator(2, space, grid));		

		for (int i = 0; i < Params.getLanesNumber(); ++i) {
		    Gate gate = new Gate(space, grid);
		    context.add(gate);
		    
		    grid.moveTo(gate, lanesManager.getLaneX(i), Params.END_POSITION);
		    space.moveTo(gate, lanesManager.getLaneX(i), Params.END_POSITION);
		}
		
		return context ;
	}

	public static Auto getAuto(ContinuousSpace<Object> space, Grid<Object> grid) {
		double std = Params.getVelocityStd();
		int mean = Params.getMeanVelocity();
		int velocity = (int)(rand.nextGaussian() * std + mean);
		if(velocity<=0)
			velocity = mean;
		switch (rand.nextInt(4)){
		case 1:
			return new AutoRed(space, grid, velocity);
		case 2:
			return new AutoGreen(space, grid, velocity);
		case 3:
			return new AutoBlue(space, grid, velocity);
			default: 
				return new Auto(space, grid, velocity);
		}
		
	}

}
