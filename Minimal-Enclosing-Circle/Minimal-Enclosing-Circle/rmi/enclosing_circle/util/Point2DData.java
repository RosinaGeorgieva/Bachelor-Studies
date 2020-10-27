package rmi.enclosing_circle.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

import rmi.enclosing_circle.pojo.Point2D;

public class Point2DData {
	
	public static ArrayList<Point2D<Integer>> generateByNumber(int number) {
		ArrayList<Point2D<Integer>> list = new ArrayList<Point2D<Integer>>();
		for(int i = 0; i < number; i++) {
			list.add(new Point2D<Integer>(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE), ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE)));
		}
		return list;
	}
	
	public static ArrayList<Point2D<Integer>> generateFromFile(File name) throws FileNotFoundException{
		ArrayList<Point2D<Integer>> list = new ArrayList<Point2D<Integer>>();
		Scanner reader = new Scanner(name);
		reader.nextInt();
		while (reader.hasNext()) {
			list.add(new Point2D<Integer>(reader.nextInt(), reader.nextInt()));
		}
		reader.close();
		return list;
	}
	
	public static int getNumberFromFile(File name) throws FileNotFoundException {
		Scanner reader = new Scanner(name);
		int numberOfThreads =  reader.nextInt();
		reader.close();
		return numberOfThreads;
	}

}


