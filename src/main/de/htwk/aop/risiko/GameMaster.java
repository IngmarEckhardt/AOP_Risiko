package de.htwk.aop.risiko;

class GameMaster {
	private Map map;
	private UserInput userInput;

	private PlayerImpl playerOne, playerTwo, winnerOfLastRound;
	private String winner;

	public GameMaster() {
		
		this.userInput = new UserInputImpl();
		this.map = new MapImpl(userInput);
		
	}

	void startGame() {

		askPlayersForTheirNames();
		this.winnerOfLastRound = playerOne;
		map.initMapWithPieces(playerOne, playerTwo);

		winner = playRounds();
		System.out.println("Der Gewinner ist " + winner);
	}

	
	
	private void askPlayersForTheirNames() {

		playerOne = new PlayerImpl(userInput.getString("Bitte den Namen des ersten Spielers eingeben: "));
		playerTwo = new PlayerImpl(userInput.getString("Bitte den Namen des zweiten Spielers eingeben: "));

	}
	
	private String playRounds() {

		while (playerOne.getFieldCount() > 0 && playerTwo.getFieldCount() > 0) {
			System.out.println("\nAngriff!");
			System.out.println(map.printMap());

			if (winnerOfLastRound == playerOne) {
				fightBetween(playerOne, playerTwo);
				
			} else {
				fightBetween(playerTwo, playerOne);
			}
			
			refreshArmys(playerOne, playerTwo);
			
			map.moveAndDeployArmy(playerOne);
			map.moveAndDeployArmy(playerTwo);
		}
		
		return (playerOne.getFieldCount() == 0) ? playerTwo.getName() : playerOne.getName();
	}

	
	private void fightBetween(PlayerImpl attackingPlayer, PlayerImpl defendingPlayer) {
		Field attackField, defenseField = null;
		
		do {
			attackField = map.getFieldWithCheckingOwnership(attackingPlayer + ", gib die Zeile und Spalte des Feldes mit deiner Armee an, getrennt durch ein Leerzeichen",
					attackingPlayer);
			if (attackField == null || attackField.isEmpty()) {
				continue;
			}

			defenseField = map.getFieldWithCheckingOwnership(attackingPlayer + ", gib die Zeile und Spalte eines benachbarten Feldes mit deinem Ziel an",
					defendingPlayer);
			if (defenseField == null) {
				continue;
			}

		} while (!attackField.isNeighboorWith(defenseField));

		
		while (!defenseField.isEmpty() && !attackField.isEmpty()) {
			
			String result = attackField.fightAgainst(defenseField);
			
			System.out.println(result + "\n" + map.printMap());

			if (!userInput.jaNeinQuestion("Möchtest du mit dem Kampf um dieses Feld fortfahren?")) {
				break;
			}
		}
		if (defenseField.isEmpty()) {changeOwnership(defenseField, attackingPlayer, defendingPlayer);}
		if (attackField.isEmpty()) {winnerOfLastRound = defendingPlayer;}
	}

		
	private void changeOwnership(Field defense, PlayerImpl attackingPlayer, PlayerImpl defendingPlayer) {
		
		defense.setOwner(attackingPlayer);	
		attackingPlayer.setFieldCount(attackingPlayer.getFieldCount() + 1);
		defendingPlayer.setFieldCount(defendingPlayer.getFieldCount() - 1);
	}
	

	private void refreshArmys(PlayerImpl playerOne, PlayerImpl playerTwo) {
		
		playerOne.setArmyCount(playerOne.getArmyCount() + (playerOne.getFieldCount() / 4));
		playerTwo.setArmyCount(playerTwo.getArmyCount() + (playerTwo.getFieldCount() / 4));
		
	}
}