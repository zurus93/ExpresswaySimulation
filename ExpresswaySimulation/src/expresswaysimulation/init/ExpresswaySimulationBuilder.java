package expresswaysimulation.init;

import java.util.Random;

import expresswaysimulation.agents.Auto;
import expresswaysimulation.agents.Gate;
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
	

	@Override
	public Context build(Context<Object> context) {
		context.setId ("ExpresswaySimulation");
		
		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder.createContinuousSpaceFactory (null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace("space", context, 
		        new SimpleCartesianAdder<Object>(), new repast.simphony.space.continuous.WrapAroundBorders(), 
		        Params.GRID_WIDTH, Params.GRID_HEIGHT);
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
                new GridBuilderParameters<Object>(new WrapAroundBorders(),
                        new SimpleGridAdder<Object>(), true, Params.GRID_WIDTH, Params.GRID_HEIGHT));
		
		// Cars initialization
		for (int i = 0; i < Params.NUMBER_OF_CARS; ++i) {
		    Random rndVelocity = new Random();
		    int velocity = 1 + rndVelocity.nextInt(2);
		    Random rndPaymentTime = new Random();
		    int paymentTime = 5 + rndPaymentTime.nextInt(10);

		    Auto auto = new Auto(space, grid, velocity, paymentTime);

		    context.add(auto);
		    
		    Random rndX = new Random();
		    boolean lane = rndX.nextBoolean();
		    int posX = lane ? 31 : 21;
		    
		    Random rndY = new Random();
		    int posY = rndY.nextInt(40);		   
		    
		    grid.moveTo(auto, posX, posY);
		    space.moveTo(auto, posX, posY);
		}
		
		// Gates initialization
		for (int i = 0; i < 4; ++i) {
		    Gate gate = new Gate(space, grid);
		    context.add(gate);
		    
		    grid.moveTo(gate, 10 + 10 * i, 45);
		    space.moveTo(gate, 10 + 10 * i, 45);
		}
		
		return context ;
	}

}
