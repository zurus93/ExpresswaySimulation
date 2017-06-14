package expresswaysimulation.util;

import java.util.List;
import java.util.Random;

import expresswaysimulation.agents.Auto;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class LanesManager {
	private static LanesManager instance = null;
	
	private int[] mLanes;
	
	// true for A4Go gate, false otherwise
	private boolean[] mLanesType;
	
	private Random rand = new Random();
	
    public static void refresh(){
        instance = null;
    }
	
	public static LanesManager getInstance() {
		if(instance==null)
			instance = new LanesManager();
		return instance;
	}
	
	private LanesManager() {
		mLanes = new int[Params.getLanesNumber()];
		mLanesType = new boolean[Params.getLanesNumber()];
		
		int lanesSpace = Params.GRID_WIDTH / (Params.getLanesNumber() + 1);
		int[] A4GoIndecies = Params.getA4GoGatesIndices();
		
		for (int i = 0, j = 0; i < Params.getLanesNumber(); i++) {
			mLanes[i] = lanesSpace * (i + 1);
			mLanesType[i] = false;
		}

		if (A4GoIndecies != null) {
    		for (int i = 0; i < A4GoIndecies.length; ++i) {
    		    mLanesType[A4GoIndecies[i]] = true;
    		}
		}
	}
	
	public int getLaneX(int laneNumber) {
		return mLanes[laneNumber];
	}
	
	public int getCarsCountOnLane(int laneNumber, Grid<Object> grid, int currentY) {
	    GridPoint gp = new GridPoint(mLanes[laneNumber], currentY);
        GridCellNgh<Auto> nghCreator = new GridCellNgh<Auto>(grid, gp, Auto.class, 0, Params.GRID_HEIGHT / 2);

        List<GridCell<Auto>> gridCells = nghCreator.getNeighborhood(true);
        int count = 0;
        for (GridCell<Auto> gc : gridCells) {
            if (gc.getPoint().getY() > currentY && gc.size() > 0) 
                count++;
        }
        return count;
	}
	
	public void setLaneType(int laneNumber, boolean laneType) {
	    mLanesType[laneNumber] = laneType;
	}
	
	public boolean isA4GoGate(int laneNumber) {
	    return mLanesType[laneNumber];
	}

	public int getFreeLaneX(int x, int y, Grid<Object> grid) {
		int i = 0;
		for(;i<mLanes.length;i++){
			if(mLanes[i] == x)
				break;
		}
		
		if(i > 0 && i < mLanes.length-1){
			int q = rand.nextBoolean()?-1:1;//¿eby równomiernie roz³o¿yæ skrêcanie
			if(isFree(mLanes[i + q], y, grid))
				return mLanes[i + q];
			else if(isFree(mLanes[i - q], y, grid))
				return mLanes[i - q];
			else
				return x;
		}
		else if(i==0){
			if(isFree(mLanes[i + 1], y, grid))
				return mLanes[i + 1];
			else
				return x;
		}
		else if(i==mLanes.length-1){
			if(isFree(mLanes[i - 1], y, grid))
				return mLanes[i - 1];
			else
				return x;
		}
		else
			return x;
	}
	
	public int getLaneNumber(int x) {
	    for (int i = 0; i < mLanes.length; ++i) {
	        if (mLanes[i] == x)
	            return i;
	    }
	    
	    return -1;
	}
	
	public boolean isFree(int x, int y, Grid<Object> grid){
		return grid.getObjectsAt(x, y).spliterator().getExactSizeIfKnown() == 0;
	}
}
