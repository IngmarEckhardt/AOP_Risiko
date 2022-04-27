package de.htwk.aop.risiko;

public interface UserInput {

	String getString(String message);

	Integer getInt(String message);

	boolean jaNeinQuestion(String message);
	
	public Integer getNextInt(String message);

	boolean resetScanner();

}