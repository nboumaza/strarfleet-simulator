package driver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import service.ScoreEvaluator;

public class Simulator {
	
	
	public static final int INCORRECT_USAGE = 1;
	public static final int INVALID_FIELD_FILE = 2;
	public static final int INVALID_FIELD_LAYOUT =3;
	public static final int INVALID_SCRIPT_FILE = 4;
	public static final int IO_EXCEPTION = 5;
	public static  final int INVALID_STRATEGY = 6;
	public static  final int UNSUPORTED_STRATEGY = 7;
    
	 public static void main(String[] args){
		 
		   if(args.length != 2){
			   //TODO log 
			   System.out.println("usage:  java" + "Simulator <fieldFilePathName> <scriptFilePathName>" );
			   System.exit(INCORRECT_USAGE);
		   }
		   
		   
		   File field = new File(args[0]);
	       if(!field.isFile()) {
	    	   System.out.println("could not find field file: " + field);
	    	   System.exit(INVALID_FIELD_FILE);
	       }
	       
	       File script = new File(args[1]);
	       if(!script.isFile()) {
	    	   //TODO log 
	    	   System.out.println("could not find script file: " + script);
	    	   System.exit(INVALID_SCRIPT_FILE);
	       }
	       
	       List<String> fieldLines = null;
	       List<String> scriptLines = null;
	       
	       try{
	    	   fieldLines = Files.readAllLines(field.toPath());
	    	   scriptLines = Files.readAllLines(script.toPath());
	       }
	       catch(IOException e){
	    	   //log
	    	   System.exit(IO_EXCEPTION);
	       }
	       
	 
	       ScoreEvaluator se = new ScoreEvaluator(fieldLines, scriptLines);
	       se.run();
	 }
}
