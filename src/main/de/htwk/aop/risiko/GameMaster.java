package de.htwk.aop.risiko;

import java.util.Arrays;
import java.util.Collections;

public class GameMaster {
	private Map map;
	private UserInput userInput;
	private Dice dice;
	private Player playerOne, playerTwo, winnerOfLastRound;
	private String winner;

	public GameMaster() {
		this.userInput = new UserInputImpl();
		this.dice = new Dice();
	}

	public void startGame() {

		askPlayersForTheirNames();

		this.map = new Map(playerOne, playerTwo, userInput);
		this.winnerOfLastRound = playerOne;

		winner = playRounds();
		System.out.println("Der Gewinner ist " + winner);
	}

	private void askPlayersForTheirNames() {

		playerOne = new Player(userInput.getString("Bitte den Namen des ersten Spielers eingeben: "));
		playerTwo = new Player(userInput.getString("Bitte den Namen des zweiten Spielers eingeben: "));

	}

	private String playRounds() {

		while (playerOne.getFieldCount() > 0 && playerTwo.getFieldCount() > 0) {
			System.out.println("\nAngriff!");
			System.out.println(map.printMap());

			if (winnerOfLastRound == playerOne) {
				fight(playerOne, playerTwo);
			} else {
				fight(playerTwo, playerOne);
			}
			refreshArmys(playerOne, playerTwo);
			map.moveAndDeployArmy(playerOne);
			map.moveAndDeployArmy(playerTwo);
		}
		
		return (playerOne.getFieldCount() == 0) ? playerTwo.getName() : playerOne.getName();
	}

	private void fight(Player attackingPlayer, Player defendingPlayer) {
		Integer[] attackersToss, defendersToss;
		Field attackField, defenseField = null;
		
		do {
			attackField = map.getField(attackingPlayer
					+ ", gib die Zeile und Spalte des Feldes mit deiner Armee an, getrennt durch ein Leerzeichen",
					attackingPlayer);
			if (attackField == null || attackField.isEmpty()) {
				continue;
			}

			defenseField = map.getField(
					attackingPlayer + ", gib die Zeile und Spalte eines benachbarten Feldes mit deinem Ziel an",
					defendingPlayer);
			if (defenseField == null) {
				continue;
			}

		} while (!attackField.isNeighboorWith(defenseField));

		
		while (!defenseField.isEmpty() && !attackField.isEmpty()) {
			String result = "";
			attackersToss = rollDice(attackField);

			defendersToss = rollDice(defenseField);

			applyFightResults(attackField, defenseField, attackersToss, defendersToss);

			result = printDice(attackersToss, defendersToss, result);

			System.out.println(result + "\n" + map.printMap());

			if (!userInput.jaNeinQuestion("Möchtest du mit dem Kampf um dieses Feld fortfahren?")) {
				break;
			}
		}
		
		if (defenseField.isEmpty()) {
			changeOwnership(attackField, defenseField, attackingPlayer, defendingPlayer);
		}
		
		if (attackField.isEmpty()) {
			attackField.setGamePieces(0);
			winnerOfLastRound = defendingPlayer;
		}
	}

	private Integer[] rollDice(Field field) {
		Integer[] toss;
		if (field.getGamePieces() > 2) {
			toss = new Integer[] { dice.rollDice(), dice.rollDice(), dice.rollDice() };
		} else if (field.getGamePieces() < 1) {
			toss = new Integer[0];
		} else if (field.getGamePieces() < 2) {
			toss = new Integer[] { dice.rollDice() };
		} else {
			toss = new Integer[] { dice.rollDice(), dice.rollDice() };
		}
		Arrays.sort(toss, Collections.reverseOrder());
		return toss;
	}

	private String printDice(Integer[] playerOneToss, Integer[] playerTwoToss, String result) {
		result = result + "Der Angreifer würfelte ";
		for (Integer integer : playerOneToss) {
			result = result + integer;
		}
		result = result + ". Der Verteidiger würfelte ";
		for (Integer integer : playerTwoToss) {
			result = result + integer;
		}
		return result;
	}
	
	private void applyFightResults(Field attack, Field defense, Integer[] playerOneToss, Integer[] playerTwoToss) {
		for (int index = 0; index < playerOneToss.length && index < playerTwoToss.length; index++) {
			if (playerTwoToss[index] == null || playerOneToss[index] > playerTwoToss[index]) {
				attack.setGamePieces(attack.getGamePieces() + 1);
				defense.setGamePieces(defense.getGamePieces() - 1);

			} else {
				attack.setGamePieces(attack.getGamePieces() - 1);
				defense.setGamePieces(defense.getGamePieces() + 1);
			}
		}
	}

	private void changeOwnership(Field attack, Field defense, Player attackingPlayer, Player defendingPlayer) {
		defense.setOwner(attackingPlayer);
		defense.setGamePieces(0);
		attackingPlayer.setFieldCount(attackingPlayer.getFieldCount() + 1);
		defendingPlayer.setFieldCount(defendingPlayer.getFieldCount() - 1);
	}

	private void refreshArmys(Player playerOne, Player playerTwo) {
		playerOne.setArmyCount(playerOne.getArmyCount() + (playerOne.getFieldCount() / 4));
		playerTwo.setArmyCount(playerTwo.getArmyCount() + (playerTwo.getFieldCount() / 4));
	}
}