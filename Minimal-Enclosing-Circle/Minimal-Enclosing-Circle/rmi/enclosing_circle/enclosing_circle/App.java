package rmi.enclosing_circle;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.vlkan.combination.Combination;
import com.vlkan.combination.CombinationIterator;

import rmi.enclosing_circle.exception.NoFileNameException;
import rmi.enclosing_circle.exception.NoNumberOfPointsException;
import rmi.enclosing_circle.exception.NoNumberOfThreadsException;
import rmi.enclosing_circle.pojo.Circle;
import rmi.enclosing_circle.thread.TwoPointsCombination;
import rmi.enclosing_circle.util.CLOption;
import rmi.enclosing_circle.util.Data;

public class App 
{
    public static void main( String[] args )
    {
    	long start = (new Date()).getTime(); 

    	CLOption options = new CLOption(args);
    	int  numberOfPoints, numberOfThreads;
    	String fileName = null;
    	try {
			numberOfPoints = options.getNumberOfPoints(); 
		} catch (NoNumberOfPointsException e1) {
			try {
				fileName = options.getFileName();
				Scanner reader;
				try {
					reader = new Scanner(new File(fileName));
					numberOfPoints =  reader.nextInt();
					reader.close();
				} catch (FileNotFoundException e) {
					System.out.println("[ERROR] File not found. "
							+ "\n[INFO] Absolute path to file required.");
					return;
				}
			} catch (NoFileNameException e2) {
				System.out.println("[ERROR] Not enough arguments.");
				return;
			}
		}
    	try {
    		numberOfThreads = options.getNumberOfThreads();
    	} catch (NoNumberOfThreadsException e3) {
    		numberOfThreads = 1;
    	} catch (NumberFormatException e4) {
    		System.out.println("[ERROR] Illegal thread number.");
    		return;
    	}
    	
    	ArrayList<Point2D.Double> points;
    	
    	if (fileName == null) {
    		points = Data.generate(numberOfPoints);
    	} else {
    		try {
				points = Data.getFromFile(new File(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("[ERROR] File not found.");
				return;
			}
    	}
    	
    	Combination c2 = new Combination(numberOfPoints, 2);
    	long numberOfCombinations = c2.size();
    	
        numberOfThreads = 8; // При 1000 точки: 1 -> 2046; 4 -> 606; 8 -> 314; 16 -> 205; 32 -> 133
        
        long combinationsPerThread = numberOfCombinations / numberOfThreads;
        Circle result = new Circle(new Point2D.Double(0.0,0.0), Double.MAX_VALUE);
        
        TwoPointsCombination.setCurrentMinimum(result);
        TwoPointsCombination.setPoints(points);
        TwoPointsCombination.setNumberOfPoints(numberOfPoints);

        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        pool.execute(new TwoPointsCombination(numberOfCombinations, numberOfThreads));
        pool.shutdown();
        
        try {
			pool.awaitTermination(2, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
   
        System.out.println(TwoPointsCombination.getCurrentMinimum());
        
    	long end = (new Date()).getTime();
    	long endurance = end - start;
    	System.out.println(endurance);
    }
}
