package de.htwk.aop.risiko;

public class Player {
	private Integer playerId = 1;
	private String name;
	private Integer armyCount, fieldCount;

	public Player(String name) {
		this.armyCount = 38;
		this.name = name;
		this.playerId = playerId++;
		this.fieldCount = 0;
	}

	public void setArmyCount(Integer armyCount) {
		this.armyCount = armyCount;
	}

	public String getName() {
		return name;
	}
	
	public Integer getFieldCount() {
		return fieldCount;
	}

	public void setFieldCount(Integer fieldCount) {
		this.fieldCount = fieldCount;
	}

	public boolean raiseArmy(Integer amount) {
		if (armyCount < 1) {return false;}
		if (armyCount < amount) {
			System.out.println("Du hast nicht genug Armeen");
			return false;
		} else {
			armyCount = armyCount - amount;
			return true;
		}
	}
	
	public Integer addArmyToDepot (Integer amount) {
		armyCount = armyCount + amount;
		return armyCount;
	}
	
	public Integer getArmyCount() {
		return armyCount;
	}

}