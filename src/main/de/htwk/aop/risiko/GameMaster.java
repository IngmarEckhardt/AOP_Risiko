package de.htwk.aop.risiko;

import java.util.Arrays;
import java.util.Collections;

public class GameMaster {
	private Map map;
	private UserInput userInput;
	private Dice dice;
	private Player playerOne, playerTwo;
	private String winner;

	public GameMaster() {
		this.userInput = new UserInputImpl();
		this.dice = new Dice();
	}

	public void startGame() {
		askPlayersForTheirNames();
		initMapWithPieces();

		winner = playRounds();
		
		System.out.println("Der Gewinner ist " + winner);
	}
	

	private void askPlayersForTheirNames() {
		playerOne = new Player(userInput.getString("Bitte den Namen des ersten Spielers eingeben: "));
		playerTwo = new Player(userInput.getString("Bitte den Namen des zweiten Spielers eingeben: "));
	}
	
	

	private void initMapWithPieces() {
		this.map = new Map(playerOne, playerTwo);

		do {
			playerOne.setArmyCount(38);
			map.clearArmys(playerOne);
			distributeGamePieces(playerOne);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + map.printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));

		do {
			playerTwo.setArmyCount(38);
			map.clearArmys(playerTwo);
			distributeGamePieces(playerTwo);
			System.out.println("Die endgültige Verteilung der Steine sieht folgendermaßen aus: \n" + map.printMap());
		} while (!userInput.jaNeinQuestion(
				"Möchtest du die Verteilung der Steine so beibehalten? (J/N) Bei Nein wird die Verteilung gelöscht)"));
	}
	

	private void distributeGamePieces(Player player) {
		Integer amountToDistribute;
		Field fieldToPlace;
		
		System.out.println(player.getName() + ", du hast " + player.getArmyCount()
				+ "Armeen. Wähle Zeile und Spalte wo du deine ersten Steine platzieren möchtest,\n"
				+ "danach wirst du gefragt wieviele Steine du platzieren möchtest.");

		while (player.getArmyCount() > 0) {
			amountToDistribute = 0;
			System.out.println(map.printMap());
			
			fieldToPlace = getField("Gib die Zeile und Spalte des nächsten Feldes an, getrennt durch ein Leerzeichen", player);
			
			if (fieldToPlace == null) {continue;}
			
			amountToDistribute = userInput.getInt("Wieviele Spielsteine willst du platzieren?");
			if (fieldToPlace.getGamePieces() > 0) {
				player.addArmyToDepot(fieldToPlace.getGamePieces());
			}
		    if(player.raiseArmy(amountToDistribute)) {
		    	fieldToPlace.setGamePieces(amountToDistribute);
		    }
			System.out.println("Es sind noch " + player.getArmyCount() + " Spielsteine zu verteilen");
		}
	}
	

	private String playRounds() {
		Field attackField, defenseField;
		String winner = "";
		
		System.out.println(
				"Spieler 1, gib die Zeile und Spalte an wo sich die Armee befindet die du zum Angriff nutzen willst.");

		while (playerOne.getFieldCount() > 0 && playerTwo.getFieldCount() > 0) {
			System.out.println(map.printMap());
			
			attackField = getField("Spieler 1: Gib die Zeile und Spalte des Feldes mit deiner Armee an, getrennt durch ein Leerzeichen (Abbruch mit 0)", playerOne);
			if (attackField == null) {continue;}
			
			defenseField = getField("Spieler 1: Gib die Zeile und Spalte eines benachbarten Feldes mit deinem Ziel an, getrennt durch ein Leerzeichen (Abbruch mit 0)", playerTwo);
			if (defenseField == null) { continue;}
			
			fight(attackField, defenseField);
		}
		
		if (playerOne.getFieldCount() == 0) {
			winner = playerTwo.getName();
		} else {
			winner = playerOne.getName();
		}
		
		return winner;
	}

	private Field getField(String string, Player player) {
		Field returnField;
		Integer line, row;
		do {
			line = userInput.getNextInt("Spieler 1: Gib die Zeile und Spalte des Feldes mit deiner Armee an, getrennt durch ein Leerzeichen (Abbruch mit 0)");
			row = userInput.getNextInt(null);
			userInput.resetScanner();
			if (line == 0 || row == 0) {return null;}
			returnField = map.getField(line - 1, row - 1);
		
		} while (!returnField.isOwner(player));

		return returnField;
	}

	private void fight(Field attack, Field defense) {
		Integer [] playerOneToss, playerTwoToss;
		String result = "";
		while (attack.getGamePieces() > 0 && defense.getGamePieces() > 0) {
			
			playerOneToss = rollDice(attack);
			
			playerTwoToss = rollDice(defense);
						
			applyFightResults(attack, defense, playerOneToss, playerTwoToss);
			
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
	

	private Integer[] rollDice(Field field) {
		Integer[] toss;
		if (field.getGamePieces() > 2) {
			toss = new Integer[] {dice.rollDice(),dice.rollDice(), dice.rollDice()};
		} else if (field.getGamePieces() < 3) {
			toss = new Integer[] {dice.rollDice(),dice.rollDice()};
		} else {
			toss = new Integer[] {dice.rollDice()};
		}
		Arrays.sort(toss, Collections.reverseOrder());
		return toss;
	}

	private void applyFightResults(Field attack, Field defense, Integer[] playerOneToss, Integer[] playerTwoToss) {
		for (int index = 0; index < playerOneToss.length; index++) {
			if (playerTwoToss[index] == null || playerOneToss[index] > playerTwoToss[index]) {
				attack.setGamePieces(attack.getGamePieces()+1);
				defense.setGamePieces(defense.getGamePieces()-1);
				
			} else {
				attack.setGamePieces(attack.getGamePieces()-1);
				defense.setGamePieces(defense.getGamePieces()+1);
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
}