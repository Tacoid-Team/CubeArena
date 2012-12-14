package com.tacoid.cubearena;

public class LevelFactory {
	static int __level[][] = {
			{2,1,1,1,1,1,1},
			{0,0,0,0,0,0,1},
			{0,0,0,0,0,0,1},
			{1,1,1,3,0,0,1},
			{1,0,0,0,0,0,1},
			{1,0,0,0,0,0,1},
			{1,1,1,1,1,1,1},
	};
	public static LevelData getLevel(int i) {
		LevelData data = new LevelData();
		data.name = "Test level";
		data.dimX = 7;
		data.dimY = 7;
		data.data = __level;
		data.initDir = Direction.NORTH;
		return data;
	}
}
