package rmi.enclosing_circle;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.vlkan.combination.Combination;

import rmi.enclosing_circle.exception.NoFileNameException;
import rmi.enclosing_circle.exception.NoNumberOfPointsException;
import rmi.enclosing_circle.exception.NoNumberOfThreadsException;
import rmi.enclosing_circle.pojo.SmallestEnclosingCircle;
import rmi.enclosing_circle.runnable.ThreePointsCombination;
import rmi.enclosing_circle.runnable.TwoPointsCombination;
import rmi.enclosing_circle.util.CLOption;
import rmi.enclosing_circle.util.DataGenerator;

public class App 
{
    public static void main( String[] args )
    {
    	System.out.println("---------------------------- MINIMUM ENCLOSING CIRCLE ----------------------------"); 
    	int  numberOfPoints, numberOfThreads;
    	String fileName = null;
    	boolean quiet;
    	CLOption options = new CLOption(args);
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
    	
    	quiet = options.getQuiet();
    	
    	ArrayList<Point2D.Double> points;
    	
    	if (fileName == null) {
    		points = DataGenerator.generate(numberOfPoints);
    	} else {
    		try {
				points = DataGenerator.getFromFile(new File(fileName));
			} catch (FileNotFoundException e) {
				System.out.println("[ERROR] File not found.");
				return;
			}
    	}

    	long start = (new Date()).getTime(); 
    	
    	SmallestEnclosingCircle result = new SmallestEnclosingCircle(new Point2D.Double(0,0), Double.MAX_VALUE);
    	ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
    	
    	Combination c2 = new Combination(numberOfPoints, 2);
    	long numberOfC2 = c2.size();
    	
    	long grainSizeC2 = numberOfC2 / numberOfThreads;
    	
    	
         for (int i = 0; i < numberOfC2 ; i += grainSizeC2) {
        	 int begin = i;
        	 long end = ( i + grainSizeC2 ) > numberOfC2 ? numberOfC2  : i + grainSizeC2;
             Runnable worker = new TwoPointsCombination(quiet, numberOfPoints, begin, end, points, result);
             executor.execute(worker);
         }
         executor.shutdown();
         
         while (!executor.isTerminated()) {
         }
    	
    	Combination c3 = new Combination(numberOfPoints, 3);
    	long numberOfC3 = c3.size();
    	
    	long grainSizeC3 = numberOfC3 / numberOfThreads;
    	
    	ExecutorService executor1 = Executors.newFixedThreadPool(numberOfThreads);
    	
         for (int i = 0; i < numberOfC3 ; i += grainSizeC3) {
        	 int begin = i;
        	 long end = ( i + grainSizeC3 ) > numberOfC3 ? numberOfC3  : i + grainSizeC3;
             Runnable worker = new ThreePointsCombination(quiet, numberOfPoints, begin, end, points, result);
             executor1.execute(worker);
         }
         executor1.shutdown();
         
         while (!executor1.isTerminated()) {
         }
         
        System.out.println("--------------------------------- BUILD RESULT --------------------------------- "); 
    	System.out.println("[INFO] Minimal enclosing circle: center = (" + result.getCenter().x + ", " + result.getCenter().y+"), radius = " + result.getRadius());
    	
    	long end = (new Date()).getTime();
    	long endurance = end - start;
    	System.out.println("[INFO] Total execution time for current run: " + endurance + "ms.");
    }
    
}
