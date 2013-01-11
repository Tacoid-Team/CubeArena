package com.tacoid.cubearena;

public enum Direction {
	NORTH,
	SOUTH,
	EAST,
	WEST;
	
	public float toAngle() {
		float f = 0.0f;
		
		switch(this) {
		case EAST:
			f = -90.0f;
			break;
		case NORTH:
			f = 0.0f;
			break;
		case SOUTH:
			f = 180.0f;
			break;
		case WEST:
			f = 90.0f; 
			break;
		default:
			break;
		}
		
		return f;
	}
};