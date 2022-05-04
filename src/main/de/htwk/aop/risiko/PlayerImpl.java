package de.htwk.aop.risiko;

class PlayerImpl extends Player {
	

	private String name;
	private Integer armyCount, fieldCount;
	
	PlayerImpl(String name) {
		super(name);
		this.armyCount = 38;
		this.fieldCount = 0;
	}
	
	@Override
	Integer setArmyCount(Integer armyCount) {
		if (armyCount == null) {return null;}
		this.armyCount = armyCount;
		return this.armyCount;
	}
	
	@Override
	Integer getArmyCount() {
		return armyCount;
	}
	
	@Override
	String getName() {
		return name;
	}
	
	@Override
	Integer getFieldCount() {
		return fieldCount;
	}
	
	@Override
	Integer setFieldCount(Integer fieldCount) {
		if (fieldCount == null) {return null;}
		this.fieldCount = fieldCount;
		return this.fieldCount;
	}
	
	@Override
	boolean raiseArmy(Integer amount) {
		if (armyCount < 1 || amount == null) {return false;}
		if (armyCount < amount) {
			System.out.println("Du hast nicht genug Armeen");
			return false;
		} else {
			armyCount = armyCount - amount;
			return true;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}