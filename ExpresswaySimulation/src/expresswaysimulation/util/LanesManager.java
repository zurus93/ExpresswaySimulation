package expresswaysimulation.util;

import java.util.Random;

import repast.simphony.space.grid.Grid;

public class LanesManager {
	private static LanesManager instance = null;
	
	private int[] mLanes;
	private Random rand = new Random();
	public static LanesManager getInstance(){
		if(instance==null)
			instance = new LanesManager();
		return instance;
	}
	
	private LanesManager(){
		mLanes = new int[Params.getLanesNumber()];
		int lanesSpace = Params.GRID_WIDTH / (Params.getLanesNumber() + 1);
		for(int i=0;i<Params.getLanesNumber();i++){
			mLanes[i] = lanesSpace * (i + 1);
		}
	}
	
	public int getLaneX(int laneNumber){
		return mLanes[laneNumber];
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
	private boolean isFree(int x, int y, Grid<Object> grid){
		return grid.getObjectsAt(x, y).spliterator().getExactSizeIfKnown() == 0;
	}
}
