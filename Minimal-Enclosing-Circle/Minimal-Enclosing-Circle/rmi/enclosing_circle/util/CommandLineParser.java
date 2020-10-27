package rmi.enclosing_circle.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import rmi.enclosing_circle.exception.*;

public class CommandLineParser {
	private static ArrayList<String> args;

	public CommandLineParser(String[] args) {
		this.args = new ArrayList<String>(Arrays.asList(args));
	}
	
	public static int getNumberOfPoints() throws Exception {
		try {
			Point2DData.getNumberFromFile(new File(getFileName()));
			if (indexOf("-n") > 0) {
				throw new AmbiguosCommandLineOptionException("Too many command line options!"); 
			} 
		} catch (LackingNumberOfPointsException ex) {
			if (indexOf("-n") < 0) {
				throw new LackingNumberOfPointsException("No number of points provided!"); 
			} 
		}
		return Integer.parseInt(args.get(indexOf("-n")+1));
	}
	
	public static String getFileName() throws LackingNumberOfPointsException {
		if (indexOf("-i") < 0) {
			throw new LackingNumberOfPointsException("No number of points provided!");
		}
		return args.get(indexOf("-i")+1);
	}
	
	public static int getNumberOfThreads() {
		return indexOf("-t") > 0 ? Integer.parseInt(args.get(indexOf("-t")+1)) : 1;
	}
	
	private static int indexOf(String option) {
		return args.indexOf(option);
	}
}
