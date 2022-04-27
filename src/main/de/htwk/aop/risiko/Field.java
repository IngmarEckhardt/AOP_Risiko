package de.htwk.aop.risiko;

public class Field {
	private Integer gamePieces, line, row;
	private Player owner;
	
	public Field (Integer line, Integer row, Player owner) {
		this.line = line;
		this.row = row;
		this.owner = owner;
	}
	public Integer getLine() {
		return line;
	}
	public Integer getRow() {
		return row;
	}
	public Integer getGamePieces() {
		return gamePieces;
	}
	public void setGamePieces(Integer gamePieces) {
		this.gamePieces = gamePieces;
	}
	public Player getOwner() {
		return owner;
	}
	public void setOwner(Player owner) {
		this.owner = owner;
	}
	
	public boolean isOwner (Player playerToCheck) {
		return this.owner == playerToCheck ? true : false;
	}
	
	public boolean isEmpty () {
		return (gamePieces < 1) ? true : false;
	}
	
	public boolean isNeighboorWith(Field fieldToCheck) {
		if ((this.line == fieldToCheck.line+1 || this.line == fieldToCheck.line-1) && this.row == fieldToCheck.row) {
			return true;
		} else if ((this.row == fieldToCheck.row+1 || this.row == fieldToCheck.row-1) && this.line == fieldToCheck.line) {
			return true;
		} else {
			return false;
		}
	}
}
