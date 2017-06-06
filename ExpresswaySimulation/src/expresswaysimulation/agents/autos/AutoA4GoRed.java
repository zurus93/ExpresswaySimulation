package expresswaysimulation.agents.autos;

import expresswaysimulation.agents.Auto;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

/**
 * Class representing car with A4Go card
 *
 */
public class AutoA4GoRed extends Auto {
    private ContinuousSpace<Object> mSpace;
    private Grid<Object> mGrid;

    private int mLane;    
    private LanesManager lanesManager;
    
	public AutoA4GoRed(ContinuousSpace<Object> space, Grid<Object> grid,
			int velocity, int lane) {
		super(space, grid, velocity, lane);
	    mSpace = space;
	    mGrid = grid;
	    mLane = lane;
	    
	    lanesManager = LanesManager.getInstance();
	}
	
	@Override
    @ScheduledMethod(start = 1, interval = 1)
    public void step() {
        GridPoint gp = mGrid.getLocation(this);
        
        int newY = gp.getY();
        int newX = gp.getX();
        
        // Get A4GoGate with least cars
        int laneA4Go = mLane;
        int carsCount = Integer.MAX_VALUE;
        for (int i = 0; i < Params.getLanesNumber(); ++i) {
            if (lanesManager.isA4GoGate(i)) {
                int cars = lanesManager.getCarsCountOnLane(i, mGrid, newY);
                if (cars < carsCount) {
                    carsCount = cars;
                    laneA4Go = i;
                }
            }
        }
        
        // Check if we can move towards new gate, if yes change newX
        if (mLane > laneA4Go) {
            int newLaneX = lanesManager.getLaneX(mLane - 1);
            if (lanesManager.isFree(newLaneX, newY, mGrid)) {
                newX = newLaneX;
                mLane = mLane - 1;
                moveTo(new GridPoint(newX, newY));
            }
        } else if (mLane < laneA4Go) {
            int newLaneX = lanesManager.getLaneX(mLane + 1);
            if (lanesManager.isFree(newLaneX, newY, mGrid)) {
                newX = newLaneX;
                mLane = mLane + 1;
                moveTo(new GridPoint(newX, newY));
            }
        } else {
            super.step();
        }       
    }
}
