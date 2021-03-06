package expresswaysimulation.agents.autos;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import expresswaysimulation.agents.Auto;
import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;

/**
 * Class representing regular car (without A4Go card)
 * It will always move to the lane with least cars (without A4Go gate)
 */
public class AutoBlue extends Auto {
    
    private ContinuousSpace<Object> mSpace;
    private Grid<Object> mGrid;

    private int mLane;    
    private LanesManager lanesManager;

	public AutoBlue(ContinuousSpace<Object> space, Grid<Object> grid,
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
        
        // Get regular gates with least cars
        int laneWithLeastCars = mLane;
        int carsCount = lanesManager.getCarsCountOnLane(mLane, mGrid, newY);
        
        for (int i = 0; i < Params.getLanesNumber(); ++i) {
            if (!lanesManager.isA4GoGate(i)) {
                int cars = lanesManager.getCarsCountOnLane(i, mGrid, newY);
                if (cars < carsCount) {
                    carsCount = cars;
                    laneWithLeastCars = i;
                }
            }
        }
    
        // Check if we can move towards new gate, if yes change newX
        boolean moved = false;
        if (mLane > laneWithLeastCars) {
            int newLaneX = lanesManager.getLaneX(mLane - 1);
            if (lanesManager.isFree(newLaneX, newY, mGrid)) {
                newX = newLaneX;
                mLane = mLane - 1;
                moveTo(new GridPoint(newX, newY));
                moved = true;
            }
        } else if (mLane < laneWithLeastCars) {
            int newLaneX = lanesManager.getLaneX(mLane + 1);
            if (lanesManager.isFree(newLaneX, newY, mGrid)) {
                newX = newLaneX;
                mLane = mLane + 1;
                moveTo(new GridPoint(newX, newY));
                moved = true;
            }
        }
        
        if (!moved) {
            super.step();
        }
    }
}
