package de.htwk.aop.risiko;

public class Map {
	
	private Field[][] map;
	private Player playerOne, playerTwo;
	
	public Map (Player playerOne, Player playerTwo) {
		this.map = new Field[4][6];
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		for (int row = 0; row < 3; row++) {
			for (int line = 0; line < 4; line++) {
				map[line][row] = new Field(line,row,playerOne);
				playerOne.setFieldCount(playerOne.getFieldCount()+1);
			}
		}
		for (int row = 3; row < 6; row++) {
			for (int line = 0; line < 4; line++) {
				map[line][row] = new Field(line,row,playerTwo);
				playerTwo.setFieldCount(playerTwo.getFieldCount()+1);
			}
		}
	}
	
	public Field getField (Integer line, Integer row) {
		if (line == null || row == null || line < 0 || line > 3 || row < 0 || row > 5) {
			System.err.println("Invalid Arguments, line was " + line + " and row was " + row);
			return null;
		}
		return map[line][row];
		
	}

	public String printMap() {
		String result = "";
		for (Field[] line: map) {
			for (Field field : line) {
				result = result + field.getOwner().getName() + "\t\t||";
			}
			result += "\n";
			for (Field field: line) {
				result = result + field.getGamePieces() + " Armeen\t||";
			}
			result += "\n_________________________________________________________________________________________________\n";
		}
//		result += "\n";
		return result;
	}

	public void clearArmysPlayerOne() {
		for (Field[] line : map) {
			for (Field field : line) {
				if (field.isOwner(playerOne)) field.setGamePieces(0);
			}
		}
		
	}
	public void clearArmysPlayerTwo() {
		for (Field[] line : map) {
			for (Field field : line) {
				if (field.isOwner(playerTwo)) field.setGamePieces(0);
			}
		}
		
	}

}