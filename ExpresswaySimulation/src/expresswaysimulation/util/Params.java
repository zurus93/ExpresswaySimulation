package expresswaysimulation.util;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class Params {
    
    public final static int GRID_WIDTH = 50;
    
    public final static int GRID_HEIGHT = 50;
        
    public final static int END_POSITION = 45;
    
    public static Integer getAutosPerTick(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		return (Integer) params.getValue("autos_per_tick");
    }
    
    public static Integer getLanesNumber(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		return (Integer) params.getValue("lanes_number");
    }
    
    public static int[] getA4GoGatesIndices(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		String value = (String) params.getValue("a4go_gates_indices");
		String[] values = value.split(" ");
		int[] indices = new int[values.length];
		for(int i =0;i<values.length;i++)
			indices[i] = Integer.parseInt(values[i]);
		return indices;
    }
    
    public static int[] getStartLanesIndices(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		String value = (String) params.getValue("start_lanes_indices");
		String[] values = value.split(" ");
		int[] indices = new int[values.length];
		for(int i =0;i<values.length;i++)
			indices[i] = Integer.parseInt(values[i]);
		return indices;
    }
    
    public static Float getA4GoCarsRatio(){
		Parameters params = RunEnvironment.getInstance().getParameters();
		return (Float) params.getValue("a4go_cars_ratio");
    }
}
