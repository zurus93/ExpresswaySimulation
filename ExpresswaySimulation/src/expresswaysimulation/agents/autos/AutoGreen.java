package expresswaysimulation.agents.autos;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

import java.util.List;

import expresswaysimulation.agents.Auto;
import expresswaysimulation.agents.Gate;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;

public class AutoGreen extends Auto {
    
	public AutoGreen(ContinuousSpace<Object> space, Grid<Object> grid,
			int velocity, int lane) {
		super(space, grid, velocity, lane);
	}
}
