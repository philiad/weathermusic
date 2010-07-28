package com.huewu.game.rocketnplanet.object;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;


public class GridPool {
	
	public static final int BACKGROUND_GRID = 0;
	public static final int SPRITE_GRID = 1;
	public static final int TILE_GRID = 2;
	public static final int PARTICLE_GRID = 3;
	
	ArrayList<Grid> gridPool = new ArrayList<Grid>();
	
	public GridPool(){
		
		Grid g = null;
		g = new Grid(2,2,false);
		gridPool.add(g);
		
		g = new Grid(2,2,false);
		gridPool.add(g);
		
		g = new Grid(2,2,false);
		gridPool.add(g);
		
		g = new Grid(2,2,false);
		gridPool.add(g);
	}
	
	public Grid requireGrid(int type){
		return gridPool.get(type);
	}
	
	public void releaseHardwareBuffers(GL10 gl, Context context) {
		for(Grid g : gridPool){
			g.releaseHardwareBuffers(gl);
		}
	}

	public void generateHardwareBufffers(GL10 gl, Context context) {
		for(Grid g : gridPool){
			g.generateHardwareBuffers(gl);
		}
	}
}//end of class
