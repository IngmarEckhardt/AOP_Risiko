package de.htwk.aop.risiko;

import java.util.Random;

public class Dice {
	private Random randomGenerator;
	
	public Dice() {
		this.randomGenerator = new Random(System.nanoTime());
	}
	
	public Integer rollDice() {
		return randomGenerator.nextInt(6)+1;
	}
}