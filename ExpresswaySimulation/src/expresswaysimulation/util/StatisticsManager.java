package expresswaysimulation.util;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import expresswaysimulation.agents.Auto;
import expresswaysimulation.agents.autos.AutoA4GoRed;
import expresswaysimulation.agents.autos.AutoBlue;
import expresswaysimulation.agents.autos.AutoGreen;
import repast.simphony.engine.schedule.ScheduledMethod;

public class StatisticsManager {
    private static final int BLACK_INDEX = 0;
    private static final int GREEN_INDEX = 1;
    private static final int BLUE_INDEX = 2;
    private static final int RED_INDEX = 3;
    
    private static final int DISPLAY_TIME = 100;
    private String csvName;
    private int[] carsCount = new int[4];
    private long[] carsTime = new long[4];
    
    int count = 0;
    
    int limit = 10;
    
    public static void refresh(){
    	instance = null;
    }
    
    private static StatisticsManager instance = null;
    
    public static StatisticsManager getInstance() {
        if(instance==null){
            instance = new StatisticsManager();
        }
        return instance;
    }
    
    private StatisticsManager(){
    	Date date = new Date();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    	csvName = "doc/logs/"+sdf.format(date);
    }
    
    public void logTime(Auto car, float time) {
        count++;
        
        if (car instanceof AutoGreen) {
            carsCount[GREEN_INDEX] += 1;
            carsTime[GREEN_INDEX] += time;
        } else if (car instanceof AutoBlue) {
            carsCount[BLUE_INDEX] += 1;
            carsTime[BLUE_INDEX] += time;
        } else if (car instanceof AutoA4GoRed) {
            carsCount[RED_INDEX] += 1;
            carsTime[RED_INDEX] += time;
        } else {
            carsCount[BLACK_INDEX] += 1;
            carsTime[BLACK_INDEX] += time;
        }
        
        if (count >= DISPLAY_TIME) {
            count = 0;
            calculateStatictis();
        }
    }
    
    private void calculateStatictis() {
        // Calculate avarage times for specific cars
        float avarageBlackCarTime = (float) carsTime[BLACK_INDEX] / carsCount[BLACK_INDEX];
        float avarageGreenCarTime = (float) carsTime[GREEN_INDEX] / carsCount[GREEN_INDEX];
        float avarageBlueCarTime = (float) carsTime[BLUE_INDEX] / carsCount[BLUE_INDEX];
        float avarageRedCarTime = (float) carsTime[RED_INDEX] / carsCount[RED_INDEX];
        
        // Calculate avarage time for all cars
        float avarageCarTime = (avarageBlackCarTime + avarageGreenCarTime + avarageBlueCarTime) / 3.f;
        limit--;
        // Print results
        displayResults(avarageBlackCarTime, avarageGreenCarTime, avarageBlueCarTime, avarageRedCarTime, avarageCarTime);
        serializeResults(avarageBlackCarTime, avarageGreenCarTime, avarageBlueCarTime, avarageRedCarTime, avarageCarTime);
        // Clear data
        
        carsTime = new long[4];
        carsCount = new int[4];
        
    }
    
    private void serializeResults(float black, float green, float blue, float red, float all) {
    	if(limit<=0)
    		return;
    	try{
    		String fileName =  csvName+".csv";
    		System.out.println(fileName);
	    	File file= new File(fileName);
	    	if (!file.exists())
	    		file.createNewFile();
	    	String outputFile = fileName;
	    	FileWriter fileWriter = new FileWriter(outputFile, true); 
	    	CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, CSVFormat.RFC4180);
	
	    	
	    	csvFilePrinter.printRecord(black, green, blue, red, all);
	
	    	fileWriter.flush();
	    	fileWriter.close();
	    	csvFilePrinter.close();
    	}
    	catch(Exception ex){
    		ex.printStackTrace();
    	}
	}

	private void displayResults(float black, float green, float blue, float red, float all) {
		if(limit<=0){
			System.out.println("----------------------------------");
			System.out.println("---------------ENOUGH-------------");
			System.out.println("----------------------------------");
			return;
		}
        System.out.println(limit+"--------------------------------");
        System.out.println("[BlackCar] " + black + " ticks");
        System.out.println("[GreenCar] " + green + " ticks");
        System.out.println("[BlueCar] " + blue + " ticks");
        System.out.println("[A4GoCar] " + red + " ticks");
        System.out.println("[AllCarsWithoutA4Go] " + all + " ticks");
        System.out.println("----------------------------------");
        System.out.println("");
    }

}
