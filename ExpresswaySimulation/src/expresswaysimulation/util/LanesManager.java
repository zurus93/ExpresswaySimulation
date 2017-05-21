package expresswaysimulation.util;

public class LanesManager {
	private static LanesManager instance = null;
	
	private int[] mLanes;
	
	public static LanesManager getInstance(){
		if(instance==null)
			instance = new LanesManager();
		return instance;
	}
	
	private LanesManager(){
		mLanes = new int[Params.NUMBER_OF_LANES];
		int lanesSpace = Params.GRID_WIDTH / (Params.NUMBER_OF_LANES + 1);
		for(int i=0;i<Params.NUMBER_OF_LANES;i++){
			mLanes[i] = lanesSpace * (i + 1);
		}
	}
	
	public int getLaneX(int laneNumber){
		return mLanes[laneNumber];
	}
}
