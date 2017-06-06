package expresswaysimulation.agents;

import java.util.ArrayList;
import java.util.List;

import expresswaysimulation.util.LanesManager;
import expresswaysimulation.util.Params;
import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

/**
 * Class representing car agent.
 * It can move in straight line or change lane to left or right.
 * When it encounters the gate, it stops for payment and then moves on.
 */
public class Auto {
	
	private ContinuousSpace<Object> mSpace;
	private Grid<Object> mGrid;
	private int mVelocity;
	
	private int mPaymentTime = 0;
	
	private int mLane;
	
	private boolean mPaying = false;
	
	public Auto(ContinuousSpace<Object> space, Grid<Object> grid, int velocity, int lane) {
		mSpace = space;
		mGrid = grid;
		mVelocity = velocity;
		mLane = lane;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		GridPoint gp = mGrid.getLocation(this);
		
		int newY = gp.getY() + mVelocity;
		int newX = gp.getX();

		// Check if there are cars at this lane preventing this car from going so fast.
		// If so drive to the nearest car and start riding behind it.
		GridCellNgh<Auto> nghCreator = new GridCellNgh<Auto>(mGrid, gp, Auto.class, 0, mVelocity);

		List<GridCell<Auto>> gridCells = nghCreator.getNeighborhood(true);

		boolean firstCarInLane = true;
		for (int i = mVelocity + 1; i < gridCells.size(); ++i) {
		    GridCell cell = gridCells.get(i);
		    
		    if (cell.size() > 0) {		    			    	
		        firstCarInLane = false;
		        if (cell.getPoint().getY() - 1 > gp.getY())
		            newY = cell.getPoint().getY() - 1;
		        else
		            newY = gp.getY();
		        
		        break;
		    }
		}
		
		// Check if we are near the gate
		if (firstCarInLane && newY >= Params.END_POSITION) {
		    if (mPaying && mPaymentTime > 0) {
		        mPaymentTime--;
		        return;
		    } else if (mPaying && mPaymentTime <= 0) {
		        Context<Object> context = ContextUtils.getContext(this);
		        context.remove(this);
		        return;
		    } else {
		        mPaying = true;
		        newY = Params.END_POSITION;
		        Gate gate = (Gate) mGrid.getObjectAt(gp.getX(), Params.END_POSITION);
		        mPaymentTime = gate.getAwaitingTime();
		    }
    		
		} else if (!firstCarInLane && !gateAhead(gp)) {		    
	        // We can't go faster on this lane, but maybe on the lane next to ours
            newX = SwitchLane(gp.getY() + mVelocity);
            if (newX != gp.getX()) {
                mLane = LanesManager.getInstance().getLaneNumber(newX);
                newY = gp.getY();               
            }          
        }
		
		moveTo(new GridPoint(newX, newY));
	}
	
	/**
	 * zwraca nowe x, je¿eli da siê zmieniæ pas
	 */
	private int SwitchLane(int newY) {
		GridPoint gp = mGrid.getLocation(this);
		int x = gp.getX();
		LanesManager lm = LanesManager.getInstance();
		int newX = lm.getFreeLaneX(x, newY, mGrid);
		
		// Don't change if it is A4Go lane
		int laneNumber = lm.getLaneNumber(newX);			
		if (lm.isA4GoGate(laneNumber)) {
		    newX = x;
		}
		return newX;
	}
	
	public void moveTo(GridPoint pt) {		
		if (!pt.equals(mGrid.getLocation(this))) {			
			mGrid.moveTo(this, (int) pt.getX(), (int) pt.getY());
			mSpace.moveTo(this, pt.getX(), pt.getY());
		}		
	}
	
	private boolean gateAhead(GridPoint gp) {	    
	    
	    GridCellNgh<Gate> nghCreator = new GridCellNgh<Gate>(mGrid, gp, Gate.class, 0, 4);
	    List<GridCell<Gate>> gridCells = nghCreator.getNeighborhood(true);
	        
        for (GridCell cell : gridCells) {
            // Check only cells ahead of the car (we are not interested in the gate if we already passed one).
            // If the gate is anywhere on the path we want to go, drive to the coordinates of the gate (not further)
            // and stop for enough time to make a payment.
            if (cell.getPoint().getY() >= gp.getY() && cell.size() > 0) {
                return true;
            }
        }
        
        return false;
	}
}
