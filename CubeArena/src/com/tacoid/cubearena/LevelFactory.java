package com.tacoid.cubearena;

public class LevelFactory {
	static int __level[][] = {
			{0,0,2,0,0,0},
			{0,0,10,0,0,0},
			{0,0,1,0,0,0},
			{6,1,4,1,6,0},
			{0,0,1,0,0,0},
			{0,0,11,0,0,0},
	};
	public static LevelData getLevel(int i) {
		LevelData data = new LevelData();
		data.name = "Test level";
		data.dimX = 6;
		data.dimY = 6;
		data.data = __level;
		data.initDir = Direction.EAST;
		return data;
	}
}
