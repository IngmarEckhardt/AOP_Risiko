package de.htwk.aop.risiko;

abstract class Player {
	
	protected String name;
	private int counter = 0;
	protected int playerId, armyCount, fieldCount;
	
	private Player() {
		this.playerId = counter + 1;
		counter ++;
	}
	
	protected Player (String name) {
		super();
		this.name = name;
	}
	
	abstract Integer setArmyCount(Integer armyCount);
	abstract Integer getArmyCount();
	abstract String getName();
	abstract Integer getFieldCount();
	abstract Integer setFieldCount(Integer fieldCount);
	abstract boolean raiseArmy(Integer amount);
	abstract Integer addArmyToDepot (Integer amount);
	public abstract String toString();
	
}
