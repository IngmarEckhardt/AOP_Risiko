package de.htwk.aop.risiko;

class Map {

	private Field[][] map;
	private UserInput userInput;

	// Constructor
	Map( UserInput userInput) {
		
		this.map = new Field[4][6];
		this.userInput = userInput;
	}


	Field getField(Integer line, Integer row) {
		
		if (line == null || row == null || line < 0 || line > 3 || row < 0 || row > 5) {
			System.err.println("Invalid Arguments, line was " + line + " and row was " + row);
			return null;
		}
		
		return map[line][row];
		
	}

	
	Field getFieldWithCheckingOwnership(String message, Player owner) {
		
		Field returnField = null;
		int line, row;
		
		do {
			line = userInput.getNextInt(message);
			row = userInput.getNextInt(null);
			userInput.resetScanner();
			
			if (line == 0 || row == 0) {
				return null;
			}
			
			returnField = getField(line - 1, row - 1);

		} while (!returnField.isOwner(owner));

		return returnField;
	}

	
	String printMap() {
		String result = "";
		for (Field[] line : map) {
			for (Field field : line) {
				result = result + field.getOwner().getName() + "\t\t||";
			}
			result += "\n";
			for (Field field : line) {
				result = result + field.getGamePieces() + " Armeen\t||";
			}
			result += "\n_________________________________________________________________________________________________\n";
		}
		return result;
	}

	
	void moveAndDeployArmy(Player player) {
		Field fieldWithArmys, fieldToPlaceArmys;
		Integer amountOfArmysToMove = 0, amountOfArmysToDeployFromDepot;

		if (player.getFieldCount() == 0) {
			return;
		}
		// move from field to field
		while (userInput.jaNeinQuestion("Möchtest du weitere Armeen bewegen, " + player.getName() + "?")) {
			
			fieldWithArmys = getFieldWithCheckingOwnership("Gib Zeile und Spalte des Feldes mit deiner Armee ein", player);
			fieldToPlaceArmys = getFieldWithCheckingOwnership("Gib ein benachbartes Feld an, zu welchem die Armeen verschoben werden sollen",
					player);
			
			if (fieldWithArmys.isNeighboorWith(fieldToPlaceArmys) && fieldWithArmys.getGamePieces() > 0) {
				do {
					amountOfArmysToMove = userInput.getInt("Wieviele Armeen möchtest du bewegen?");
				} while (amountOfArmysToMove < 0 || !(amountOfArmysToMove < fieldWithArmys.getGamePieces()));
			}
			
			fieldWithArmys.setGamePieces(fieldWithArmys.getGamePieces() - amountOfArmysToMove);
			fieldToPlaceArmys.setGamePieces(fieldToPlaceArmys.getGamePieces() + amountOfArmysToMove);

			System.out.println(printMap());
		}

		// deploy from depot
		while (player.getArmyCount() > 0 && userInput.jaNeinQuestion("Möchtest Du weitere Armeen aufstellen? (j/n)")) {

			fieldToPlaceArmys = getFieldWithCheckingOwnership("Gib das Feld an in dem die Armeen aufgestellt werden", player);
			
			do {
				System.out.println("Es befinden sich " + player.getArmyCount() + " Armeen im Depot.");
				amountOfArmysToDeployFromDepot = userInput.getInt("Wieviele Armeen möchtest du in diesem Feld platzieren?");

			} while (amountOfArmysToDeployFromDepot < 0 || !player.raiseArmy(amountOfArmysToDeployFromDepot));

			fieldToPlaceArmys.setGamePieces(fieldToPlaceArmys.getGamePieces() + amountOfArmysToDeployFromDepot);
			
			System.out.println(printMap());
		}
	}

	void initMapWithPieces(Player playerOne, Player playerTwo) {
		
		for (int row = 0; row < 3; row++) {
			for (int line = 0; line < 4; line++) {
				map[line][row] = new Field(line, row, playerOne);
				playerOne.setFieldCount(playerOne.getFieldCount() + 1);
			}
		}
		
		for (int row = 3; row < 6; row++) {
			for (int line = 0; line < 4; line++) {
				map[line][row] = new Field(line, row, playerTwo);
				playerTwo.setFieldCount(playerTwo.getFieldCount() + 1);
			}
		}

		do {
			playerOne.setArmyCount(38);
			clearArmys(playerOne);
			distributeGamePieces(playerOne);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));

		do {
			playerTwo.setArmyCount(38);
			clearArmys(playerTwo);
			distributeGamePieces(playerTwo);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));
	}

	private void clearArmys(Player player) {
		for (Field[] line : map) {
			for (Field field : line) {
				if (field.isOwner(player))
					field.setGamePieces(0);
			}
		}
	}

	private void distributeGamePieces(Player player) {
		Integer amountToDistribute;
		Field fieldToPlace;

		System.out.println(player + ", du hast " + player.getArmyCount()
				+ " Armeen. Wähle Zeile und Spalte wo du deine ersten Steine platzieren möchtest,\n"
				+ "danach wirst du gefragt wieviele Steine du platzieren möchtest.");

		while (player.getArmyCount() > 0) {
			amountToDistribute = 0;
			System.out.println(printMap());

			fieldToPlace = getFieldWithCheckingOwnership("Gib die Zeile und Spalte des nächsten Feldes an, getrennt durch ein Leerzeichen",
					player);

			if (fieldToPlace == null) {
				continue;
			}

			amountToDistribute = userInput.getInt("Wieviele Spielsteine willst du platzieren?");
			if (fieldToPlace.getGamePieces() > 0) {
				player.addArmyToDepot(fieldToPlace.getGamePieces());
			}
			if (player.raiseArmy(amountToDistribute)) {
				fieldToPlace.setGamePieces(amountToDistribute);
			}
			System.out.println("Es sind noch " + player.getArmyCount() + " Spielsteine zu verteilen");
		}
	}

}