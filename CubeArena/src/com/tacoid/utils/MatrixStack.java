package com.tacoid.utils;

import java.util.Stack;

import com.badlogic.gdx.math.Matrix4;

public class MatrixStack {
	static private MatrixStack instance = null;
	
	static public MatrixStack getInstance() {
		if(instance == null) {
			instance = new MatrixStack();
		}
		return instance;
	}
	
	private Stack<Matrix4> stack;
	
	private MatrixStack() {
		stack = new Stack<Matrix4>();
	}
	
	public void push(Matrix4 mat) {
		stack.push(new Matrix4(mat));
	}
	
	public Matrix4 pop() {
		return stack.pop();
	}
}
