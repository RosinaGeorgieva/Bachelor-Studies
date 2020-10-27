package rmi.enclosing_circle.util;

import java.util.ArrayList;
import java.util.Arrays;

import rmi.enclosing_circle.exception.NoFileNameException;
import rmi.enclosing_circle.exception.NoNumberOfPointsException;
import rmi.enclosing_circle.exception.NoNumberOfThreadsException;

public class CLOption {
	private static ArrayList<String> args;
	
	public CLOption(String[] args) {
		this.args = new ArrayList<String>(Arrays.asList(args));
	}
	
	public int getNumberOfPoints() throws NoNumberOfPointsException {
		if (indexOf("-n") < 0) {
			throw new NoNumberOfPointsException(NoNumberOfPointsException.class.toString());
		}
		return Integer.parseInt(args.get(indexOf("-n")+1));
	}
	
	public String getFileName() throws NoFileNameException {
		if (indexOf("-i") < 0) {
			throw new NoFileNameException(NoFileNameException.class.toString());
		}
		return args.get(indexOf("-i")+1);
	}
	
	public int getNumberOfThreads() throws NoNumberOfThreadsException {
		if (indexOf("-t") < 0) {
			throw new NoNumberOfThreadsException(NoNumberOfThreadsException.class.toString());
		}
		return Integer.parseInt(args.get(indexOf("-t")+1));
	}
	
	private int indexOf(String option) {
		return args.indexOf(option);
	}
}
