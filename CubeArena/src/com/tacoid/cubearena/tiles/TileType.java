package com.tacoid.cubearena.tiles;

public enum TileType {
	NO_TILE(0),
	EMPTY(1),
	START(2),
	END(3),
	CHANGE_DIRECTION(4),
	ROTATE_RIGHT(5),
	ROTATE_LEFT(6),
	PUSH(7),
	TELEPORT(8);
	
	
	
	final int value;
	
	TileType(int value) {
		this.value = value;
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