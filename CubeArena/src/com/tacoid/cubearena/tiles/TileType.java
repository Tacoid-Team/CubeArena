package com.tacoid.cubearena.tiles;

public enum TileType {
	NO_TILE(0, false),
	EMPTY(1, false),
	START(2, true),
	END(3, false),
	CHANGE_DIRECTION(4, true),
	ROTATE_RIGHT(5, false),
	ROTATE_LEFT(6, false),
	PUSH(7, true),
	TELEPORT(8, false);
	
	
	
	final int value;
	final boolean directionRequired;
	
	public boolean isDirectionRequired() {
		return directionRequired;
	}

	TileType(int value, boolean directionRequired) {
		this.value = value;
		this.directionRequired = directionRequired;
	}
	
	public int getValue() {
		return value;
	}
	
	public static TileType fromValue(int value) {
		for(TileType t : TileType.values()) {
			if(t.getValue() == value)
				return t;
		}
		return NO_TILE;
	}
};