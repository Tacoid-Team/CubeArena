package com.tacoid.cubearena;

public class LevelFactory {
	/*
	SOUTH<==>NORTH
	
	 WEST
	 ^
	 |
	 v
	 EAST
	 
	 */
	static int __level[][] = {
			{2,1,1,1,1,1,1},
			{0,0,0,0,0,0,1},
			{0,0,0,0,0,0,1},
			{1,1,1,3,0,0,1},
			{1,0,0,0,0,0,1},
			{1,0,0,0,0,0,1},
			{1,1,1,1,1,1,1},
	};
	static int __level2[][] = {
		{0,0,0,3,0},
		{0,0,0,1,0},
		{0,0,1,1,1},
		{2,1,1,1,1},
		{0,0,1,1,1},

};
	public static LevelData getLevel(int i) {
		LevelData data = new LevelData();
		data.name = "Test level";
		data.data = __level2;
		data.dimX = data.data.length;
		data.dimY = data.data.length;
		data.initDir = Direction.NORTH;
		return data;
	}
}
