package de.htwk.aop.risiko;

abstract class Field {

	protected int line;
	protected int row;
	protected PlayerImpl owner;
	
	protected Field (Integer line, Integer row, PlayerImpl owner) {
		this.line = line;
		this.row = row;
		this.owner = owner;
	}

	abstract Integer getGamePieces();

	abstract boolean increaseGamePieces(Integer amount);

	abstract boolean decreaseGamePieces(Integer amount);

	abstract PlayerImpl getOwner();

	abstract void setOwner(PlayerImpl owner);

	abstract boolean isOwner(PlayerImpl playerToCheck);

	abstract boolean isEmpty();

	abstract boolean isNeighboorWith(Field defenseField);

	abstract String fightAgainst(Field defenseField);
	
	abstract Integer[] rollDice();
	

}