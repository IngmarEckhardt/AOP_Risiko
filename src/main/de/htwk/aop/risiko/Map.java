package de.htwk.aop.risiko;

abstract class Map {
	abstract Field getField(Integer line, Integer row);
	abstract Field getFieldWithCheckingOwnership(String message, Player owner);
	abstract String printMap();
	abstract boolean moveAndDeployArmy(Player player);
	abstract boolean initMapWithPieces(Player playerOne, Player playerTwo);
}
