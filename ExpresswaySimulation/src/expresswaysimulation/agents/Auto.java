package expresswaysimulation.agents;

import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

/**
 * Class representing car agent.
 * It can move in straight line or change lane to left or right.
 * When it encounters the gate, it stops for payment and then moves on.
 */
public class Auto {
	
	private ContinuousSpace<Object> mSpace;
	private Grid<Object> mGrid;
	private int mVelocity;
	
	private int mPaymentTime;
	private int mPaymentCount = 0;
	
	public Auto(ContinuousSpace<Object> space, Grid<Object> grid, int velocity, int paymentTime) {
		mSpace = space;
		mGrid = grid;
		mVelocity = velocity;
		mPaymentTime = paymentTime;
	}
	
	@ScheduledMethod(start = 1, interval = 5000)
	public void step() {
		GridPoint gp = mGrid.getLocation(this);
		
		// Move car according to its velocity
		int newY = gp.getY() + mVelocity;

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
		newY = nearGates(gp, newY);
		
		moveTo(new GridPoint(gp.getX(), newY));		
	}
	
	public void moveTo(GridPoint pt) {		
		if (!pt.equals(mGrid.getLocation(this))) {			
			mGrid.moveTo(this, (int) pt.getX(), (int) pt.getY());
			mSpace.moveTo(this, pt.getX(), pt.getY());
		}		
	}
	
	private int nearGates(GridPoint gp, int y) {
	    GridCellNgh<Gate> nghCreator = new GridCellNgh<Gate>(mGrid, gp, Gate.class, 1, y - gp.getY());
	    List<GridCell<Gate>> gridCells = nghCreator.getNeighborhood(true);
	    
	    int newY = y;
	    for (GridCell cell : gridCells) {
	        // Check only cells ahead of the car (we are not interested in the gate if we already passed one).
	        // If the gate is anywhere on the path we want to go, drive to the coordinates of the gate (not further)
	        // and stop for enough time to make a payment.
	        if (cell.getPoint().getY() >= gp.getY() && cell.size() > 0) {
	            if (mPaymentCount < mPaymentTime) {
	                mPaymentCount++;
	                newY = cell.getPoint().getY();

	                break;
	            } else {
	                mPaymentCount = 0;
	            }
	        }
	    }
	    
	    return newY;
	}
}
