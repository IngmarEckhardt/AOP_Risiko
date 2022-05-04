package de.htwk.aop.risiko;

import java.util.Arrays;
import java.util.Collections;

class FieldImpl extends Field {
	
	private Integer gamePieces, line, row;
	private Player owner;
	private static Dice dice = new Dice();
	
	FieldImpl (Integer line, Integer row, Player owner) {
		super(line, row, owner);
	}

	@Override
	Integer getGamePieces() {
		return gamePieces;
	}
	
	@Override
	boolean increaseGamePieces(Integer amount) {
		if (amount == null) return false;
		this.gamePieces +=amount;
		return true;
	}
	@Override
	boolean decreaseGamePieces(Integer amount) {
		if (amount == null || amount > this.gamePieces) return false;
		if (amount > this.gamePieces) {return false;}
		this.gamePieces -=amount;
		return true;
	}
	
	@Override
	Player getOwner() {
		return owner;
	}
	@Override
	void setOwner(Player owner) {
		this.owner = owner;
	}
	
	@Override
	boolean isOwner (Player owner) {
		return this.owner == owner ? true : false;
	}
	
	@Override
	boolean isEmpty () {
		return (gamePieces < 1) ? true : false;
	}
	
	@Override
	boolean isNeighboorWith(Field fieldToCheck) {
		if ((this.line == fieldToCheck.line+1 || this.line == fieldToCheck.line-1) && this.row == fieldToCheck.row) {
			return true;
		} else if ((this.row == fieldToCheck.row+1 || this.row == fieldToCheck.row-1) && this.line == fieldToCheck.line) {
			return true;
		} else {
			return false;
		}
	}
	@Override
	String fightAgainst(Field defendingEnemy) {
		String result = "";
		Integer[] ownToss = rollDice();
		Integer[] enemyToss = defendingEnemy.rollDice();
		calculateFightResults(defendingEnemy, ownToss, enemyToss);
		result = getTossesAsString(ownToss, enemyToss, result);
		
		return result;
	}
	
	@Override
	Integer[] rollDice() {
		Integer[] collectedTosses;
		
		if (gamePieces > 2) {
			collectedTosses = new Integer[] { dice.rollDice(), dice.rollDice(), dice.rollDice() };
			
		} else if (gamePieces < 1) {
			collectedTosses = new Integer[0];
			
		} else if (gamePieces < 2) {
			collectedTosses = new Integer[] { dice.rollDice() };
			
		} else {
			collectedTosses = new Integer[] { dice.rollDice(), dice.rollDice() };
		}
		
		Arrays.sort(collectedTosses, Collections.reverseOrder());
		return collectedTosses;
	}
	
	private void calculateFightResults(Field enemy, Integer[] ownToss, Integer[] enemyToss) {
		
		for (int index = 0; index < ownToss.length; index++) {
			
			if (index > enemyToss.length-1 || enemyToss[index] == null || ownToss[index] > enemyToss[index]) {
				this.increaseGamePieces(1);
				enemy.decreaseGamePieces(1);

			} else {
				this.decreaseGamePieces(1);
				enemy.increaseGamePieces(1);
			}
		}
	}
	
	private String getTossesAsString(Integer[] ownToss, Integer[] enemyToss, String result) {
		
		result += "Der Angreifer würfelte ";
		for (Integer integer : ownToss) {
			result += integer;
		}
		
		result += ". Der Verteidiger würfelte ";
		for (Integer integer : enemyToss) {
			result += integer;
		}
		
		return result;
	}
}
