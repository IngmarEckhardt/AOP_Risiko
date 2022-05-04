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

	abstract boolean decreaseGamePieces(Integer amount);

	abstract Player getOwner();

	abstract void setOwner(Player owner);

	abstract boolean isOwner(Player playerToCheck);

	abstract boolean isEmpty();

	abstract boolean isNeighboorWith(Field defenseField);

	abstract String fightAgainst(Field defenseField);
	
	abstract Integer[] rollDice();
	

}