package rmi.enclosing_circle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import rmi.enclosing_circle.util.Point2DData;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	long start = (new Date()).getTime(); 
    	
//    	try {
//			System.out.println(PointsGenerator.generateFromFile(new File("input.in")));
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    	Point2DData.generateByNumber(1024);
    	long end = (new Date()).getTime();
    	long endurance = end - start;
    	System.out.println(endurance);
    }
}
