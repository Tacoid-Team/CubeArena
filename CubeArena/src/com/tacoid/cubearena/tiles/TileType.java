package com.tacoid.cubearena.tiles;

public enum TileType {
	NO_TILE(0),
	EMPTY(1),
	CHANGE_DIRECTION(2),
	ROTATE_RIGHT(3),
	ROTATE_LEFT(4),
	PUSH(5),
	TELEPORT(6),
	START(10),
	END(11);
	
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