package de.htwk.aop.risiko;

abstract class Map {
	abstract Field getField(Integer line, Integer row);
	abstract Field getFieldWithCheckingOwnership(String message, PlayerImpl owner);
	abstract String printMap();
	abstract boolean moveAndDeployArmy(PlayerImpl player);
	abstract boolean initMapWithPieces(PlayerImpl playerOne, PlayerImpl playerTwo);
}
