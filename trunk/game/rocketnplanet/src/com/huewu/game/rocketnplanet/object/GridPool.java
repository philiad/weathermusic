package com.huewu.game.rocketnplanet.object;


public class GridPool {
	public void loadGrid(){
	}
	
	public Grid requireGrid(int vertsAcross, int vertsDown, boolean useFixedPoint){
		return new Grid(vertsAcross, vertsDown, useFixedPoint);
	}
}//end of class
