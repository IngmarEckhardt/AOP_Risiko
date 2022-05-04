package de.htwk.aop.risiko;

import java.util.Random;

class Dice {
	private Random randomGenerator;
	
	Dice() {
		this.randomGenerator = new Random(System.nanoTime());
	}
	
	Integer rollDice() {
		return randomGenerator.nextInt(6)+1;
	}
}