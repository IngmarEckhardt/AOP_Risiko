package de.htwk.aop.risiko;

import java.util.Arrays;
import java.util.Collections;

public class GameMaster {
	private Map map;
	private UserInput userInput;
	private Dice dice;
	private Player playerOne, playerTwo, winner;

	public GameMaster() {
		this.userInput = new UserInputImpl();
		this.dice = new Dice();
	}

	public void startGame() {
		askPlayersForTheirNames();
		initMapWithPieces();

		String winner = playRounds();
		
		System.out.println("Der Gewinner ist " + winner);
	}

	private String playRounds() {
		Integer lineAttacker, rowAttacker, lineDefense, rowDefense;
		String winner = "";
		
		System.out.println(
				"Spieler 1, gib die Zeile und Spalte an wo sich die Armee befindet die du zum Angriff nutzen willst.");

		while (playerOne.getFieldCount() > 0 && playerTwo.getFieldCount() > 0) {
			System.out.println(map.printMap());
			do {
				lineAttacker = userInput.getNextInt(
						"Spieler 1: Gib die Zeile und Spalte des Feldes mit deiner Armee an, getrennt durch ein Leerzeichen (Abbruch mit 0)");
				rowAttacker = userInput.getNextInt(null);
				userInput.resetScanner();
				if (lineAttacker == 0 || rowAttacker == 0) {
					break;
				}
			} while (!map.getField(lineAttacker - 1, rowAttacker - 1).isOwner(playerOne));

			if (lineAttacker == 0 || rowAttacker == 0) {
				continue;
			}

			do {
				lineDefense = userInput.getNextInt(
						"Spieler 1: Gib die Zeile und Spalte eines benachbarten Feldes mit deinem Ziel an, getrennt durch ein Leerzeichen (Abbruch mit 0)");
				rowDefense = userInput.getNextInt(null);
				userInput.resetScanner();
				if (lineDefense == 0 || rowDefense == 0) {
					break;
				}
			} while (!map.getField(lineAttacker - 1, rowAttacker - 1)
					.isNeighboorWith(map.getField(lineDefense-1, rowDefense-1))
					&& !map.getField(lineDefense - 1, rowDefense - 1).isOwner(playerTwo));
			if (lineDefense == 0 || rowDefense == 0) {
				continue;
			}
			fight(map.getField(lineAttacker-1, rowAttacker-1), map.getField(lineDefense-1, rowDefense-1));
		}
		
		if (playerOne.getFieldCount() == 0) {
			winner = playerTwo.getName();
		} else {
			winner = playerOne.getName();
		}
		
		return winner;
	}

	private void fight(Field attack, Field defense) {
		Integer [] playerOneToss, playerTwoToss;
		String result = "";
		while (attack.getGamePieces() > 0 && defense.getGamePieces() > 0) {
			
			if (attack.getGamePieces() > 2) {
				playerOneToss = new Integer[] {dice.rollDice(),dice.rollDice(), dice.rollDice()};
			} else if (attack.getGamePieces() < 3) {
				playerOneToss = new Integer[] {dice.rollDice(),dice.rollDice()};
			} else {
				playerOneToss = new Integer[] {dice.rollDice()};
			}
			
			if (defense.getGamePieces() > 2) {
				playerTwoToss = new Integer[] {dice.rollDice(),dice.rollDice(), dice.rollDice()};
			} else if (defense.getGamePieces() < 3) {
				playerTwoToss = new Integer[] {dice.rollDice(),dice.rollDice()};
			} else {
				playerTwoToss = new Integer[] {dice.rollDice()};
			}
			
			Arrays.sort(playerOneToss, Collections.reverseOrder());
			Arrays.sort(playerTwoToss, Collections.reverseOrder());
			
			
			for (int index = 0; index < playerOneToss.length; index++) {
				if (playerTwoToss[index] == null || playerOneToss[index] > playerTwoToss[index]) {
					attack.setGamePieces(attack.getGamePieces()+1);
					defense.setGamePieces(defense.getGamePieces()-1);
					
				} else {
					attack.setGamePieces(attack.getGamePieces()-1);
					defense.setGamePieces(defense.getGamePieces()+1);
				}
			}
			if (defense.getGamePieces() < 1) {
				System.out.println("Player 1 hat das Feld erfolgreich angegriffen");
				defense.setOwner(playerOne);
				playerOne.setFieldCount(playerOne.getFieldCount()+1);
				playerTwo.setFieldCount(playerTwo.getFieldCount()-1);
				break;
			}
			if (attack.getGamePieces() < 1) {
				System.out.println("Player 2 hat das Feld erfolgreich verteidigt");
				attack.setOwner(playerTwo);
				playerOne.setFieldCount(playerOne.getFieldCount()-1);
				playerTwo.setFieldCount(playerTwo.getFieldCount()+1);
				break;
			}
			result = printDice(playerOneToss, playerTwoToss, result);
			System.out.println(result + ". Nach dem Würfeln ist die Verteilung auf der Map folgendermaßen: ");
			System.out.println(map.printMap());
			if (!userInput.jaNeinQuestion("Möchtest du mit diesem Kampf fortfahren?")) {
				break;
			}
			
		}
		
	}

	private String printDice(Integer[] playerOneToss, Integer[] playerTwoToss, String result) {
		result = result + "Spieler 1 würfelte ";
		for (Integer integer: playerOneToss) {
			result = result + integer;
		}
		result = result + ". Spieler 2 würfelte ";
		for (Integer integer: playerTwoToss) {
			result = result + integer;
		}
		return result;
	}

	private void initMapWithPieces() {
		this.map = new Map(playerOne, playerTwo);

		do {
			playerOne.setArmyCount(38);
			map.clearArmysPlayerOne();
			distributeGamePieces(playerOne);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + map.printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));

		do {
			playerTwo.setArmyCount(38);
			map.clearArmysPlayerTwo();
			distributeGamePieces(playerTwo);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + map.printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));
	}

	private void distributeGamePieces(Player player) {
		Integer line, row, amountToDistribute;
		System.out.println(player.getName() + ", du hast " + player.getArmyCount()
				+ "Armeen. Wähle Zeile und Spalte wo du deine ersten Steine platzieren möchtest,\n"
				+ "danach wirst du gefragt wieviele Steine du platzieren möchtest.");

		while (player.getArmyCount() > 0) {
			amountToDistribute = 0;
			System.out.println(map.printMap());
			line = userInput.getNextInt("Gib die Zeile und Spalte des nächsten Feldes an, getrennt durch ein Leerzeichen");
			row = userInput.getNextInt(null);
			userInput.resetScanner();
			if (map.getField(line-1, row-1) == null) {
				continue;
			}
			if (map.getField(line-1, row-1) != null && !map.getField(line-1, row-1).isOwner(player)) {
				System.err.println("Player " + player + "wasnt the owner of the field " + line + ", " + row);
				continue;
			}
			amountToDistribute = userInput.getInt("Wieviele Spielsteine willst du platzieren?");
			if (map.getField(line-1, row-1) != null && map.getField(line-1, row-1).getGamePieces() > 0) {
				player.addArmyToDepot(map.getField(line-1, row-1).getGamePieces());
			}
		    if(player.raiseArmy(amountToDistribute)) {
		    	map.getField(line-1, row-1).setGamePieces(amountToDistribute);
		    }
			System.out.println("Es sind noch " + player.getArmyCount() + " Spielsteine zu verteilen");
		}

	}

	private void askPlayersForTheirNames() {
		playerOne = new Player(userInput.getString("Bitte den Namen des ersten Spielers eingeben: "));
		playerTwo = new Player(userInput.getString("Bitte den Namen des zweiten Spielers eingeben: "));
	}

}