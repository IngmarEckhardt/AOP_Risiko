package de.htwk.aop.risiko;

abstract class Field {

	protected int line;
	protected int row;
	protected Player owner;
	
	protected Field (Integer line, Integer row, Player owner) {
		this.line = line;
		this.row = row;
		this.owner = owner;
	}

	abstract Integer getGamePieces();

	abstract boolean increaseGamePieces(Integer amount);
	
	/**
	 * the method checks if there are more armies in the field than the amount that 
	 * should be subtracted.
	 * 
	 * @param amount of the armies that should be subtracted from the field-army-attribute
	 * @return true if the amount is successfully subtracted, false if parameter has value 'null' or amount is bigger
	 * than the amount of game pieces
	 *  */
	abstract boolean decreaseGamePieces(Integer amount);

	abstract Player getOwner();

	abstract void setOwner(Player owner);

	abstract boolean isOwner(Player playerToCheck);

	abstract boolean isEmpty();
	
	/**
	 * 
	 * @return true if the fieldToCheck is a neighbor of the field, false otherwise
	 */
	abstract boolean isNeighboorWith(Field fieldToCheck);
	
	
	/** 
	 * uses a dice and the amount of armies at the fields to decide which field win stones from the
	 * other field 
	 * 
	 * 
	 * @param defenseField the Field that belong to the enemy of this round
	 * @return a String message that contain a information which tosses the players got
	 * */
	abstract String fightAgainst(Field defenseField);
	
	
	/** 
	 * uses a Dice.class-Instance to collect tosses dependent on the amount of armies
	 * 
	 * 
	 * 
	 * @return a Integer-Array with the tosses from the field in descending order
	 * */
	abstract Integer[] rollDice();
	

}