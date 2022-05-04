package de.htwk.aop.risiko;

abstract class Map {
	
	/**
	 * check if the parameters are null or a invalid argument that doesn't match
	 * a index in the array
	 * 
	 * @param line
	 * @param row
	 * @return the field identified by the parameters or null if errors occurred
	 */
	abstract Field getField(Integer line, Integer row);
	
	
	/** 
	 * 
	 * @param message Asking the user to chose a field which is specified by the message
	 * @param owner	the Player that should be the owner of the field
	 * @return	a field if the user chose own with the ownership, null if message or owner was null
	 */
	abstract Field getFieldWithCheckingOwnership(String message, Player owner);
	
	/**
	 * 
	 * @return a String that with visualization of the map prepared with line breaks for a command line console
	 */
	abstract String printMap();
	
	/**
	 * the player can move his armies from one field to a neighbor or deploy armies from his
	 * depot in a second step.
	 * 
	 * @param player
	 * @return true if both steps finished successfully, false if player has the value null
	 */
	abstract boolean moveAndDeployArmy(Player player);
	
	/**
	 * initializes the map and let the players deploy their armies at the start of the game
	 * 
	 * @param playerOne
	 * @param playerTwo
	 * @return true if all steps finished successfully, false if the parameters has the value null
	 */
	abstract boolean initMapWithPieces(Player playerOne, Player playerTwo);
}
