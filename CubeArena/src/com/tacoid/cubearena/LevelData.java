package com.tacoid.cubearena;

import java.io.Serializable;

public class LevelData implements Serializable {
	private static final long serialVersionUID = -8460207740949938230L;
	public String name;
	public int[][] data;
	public int dimX;
	public int dimY;
	public Direction initDir;
}
