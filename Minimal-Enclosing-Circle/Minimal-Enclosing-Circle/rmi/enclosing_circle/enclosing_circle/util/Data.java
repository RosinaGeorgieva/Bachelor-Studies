package rmi.enclosing_circle.util;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Data {
	
	public static ArrayList<Point2D.Double> generate(int number) {
		ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
		for(int i = 0; i < number; i++) {
			list.add(new Point2D.Double(new Double(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE)), new Double(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE))));
		}
		return list;
	}
	
	public static ArrayList<Point2D.Double> getFromFile(File name) throws FileNotFoundException{
		ArrayList<Point2D.Double> list = new ArrayList<Point2D.Double>();
		Scanner reader = new Scanner(name);
		reader.nextInt();
		while (reader.hasNext()) {
			list.add(new Point2D.Double(new Double(reader.nextInt()), new Double(reader.nextInt())));
		}
		reader.close();
		return list;
	}

}


