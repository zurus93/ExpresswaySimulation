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
	
	public Auto(ContinuousSpace<Object> space, Grid<Object> grid, int velocity) {
		mSpace = space;
		mGrid = grid;
		mVelocity = velocity;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		GridPoint gp = mGrid.getLocation(this);
		
		// Move car according to its velocity
		int x = gp.getX();
		int newY = gp.getY() + mVelocity;
		int newX = x;
		// Check if there are cars at this lane preventing this car from going so fast.
		// If so drive to the nearest car and start riding behind it.
		GridCellNgh<Auto> nghCreator = new GridCellNgh<Auto>(mGrid, gp, Auto.class, 0, mVelocity);

		List<GridCell<Auto>> gridCells = nghCreator.getNeighborhood(true);

		boolean firstCarInLane = true;
		for (int i = mVelocity + 1; i < gridCells.size(); ++i) {
		    GridCell cell = gridCells.get(i);
		    
		    if (cell.size() > 0) {
		    	
		    	newX = SwitchLane(newY);
		    	if(x != newX){

		    		break;//da siê zmieniæ pas
		    	}
		    	
		        firstCarInLane = false;
		        if (cell.getPoint().getY() - 1 > gp.getY())
		            newY = cell.getPoint().getY() - 1;
		        else
		            newY = gp.getY();
		        
		        break;
		    }
		}
		
		// Check if we are near the gate
		int y = nearGates(gp, newY);
		if(y!=newY){
			newX = x;//nie zmieniamy pasa, je¿eli jesteœmy na bramkach
		}
		newY = y;
		moveTo(new GridPoint(newX, newY));
		if(newY >= Params.GRID_HEIGHT - mVelocity){
			Context<Object> context = ContextUtils.getContext(this);
			context.remove(this);
		}
	}
	/**
	 * zwraca nowe x, je¿eli da siê zmieniæ pas
	 */
	private int SwitchLane(int newY){
		GridPoint gp = mGrid.getLocation(this);
		int x = gp.getX();
		LanesManager lm = LanesManager.getInstance();
		int newX = lm.getFreeLaneX(x, newY, mGrid);
		return newX;
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
	        	
	    		for (Object obj : mGrid.getObjectsAt(cell.getPoint().getX(), cell.getPoint().getY())) {
	    			if (obj instanceof Gate) {
	    				Gate gate = (Gate)obj;
	    	            if (mPaymentTime > 0) {
	    	            	mPaymentTime--;
	    	                newY = cell.getPoint().getY();
	    	                return newY;
	    	            } else {
	    	                mPaymentTime = gate.getAwaitingTime();
	    	            }
	    			}
	    		}
	        }
	    }
	    return newY;
	}
}
