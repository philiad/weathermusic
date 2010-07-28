package com.huewu.game.rocketnplanet.logic;

import java.util.Random;

import android.graphics.Rect;

public class WorldGenerator {
	
	private final static int BLOCK_WIDTH = 16;
	private final static int BLOCK_HEIGHT = 16;
	
	private final static int WORLD_WIDTH= 100;
	private final static int WORLD_HEIGHT = 100;
	
	TileBlock[][] tileBlocks = new TileBlock[100][100];
	WorldCondition cond = null;
	
	public class Tile{
		int type;
		int texturename;
		Rect bound;
	}
	
	public class WorldCondition{
		int difficulity;
	}
	
	private class TileBlock{
		Tile[][] tiles = new Tile[BLOCK_WIDTH][BLOCK_HEIGHT];
		int type;
	}
	
	
	int xIndex = 0;
	int yIndex = 0;
	public void generate(int x, int y, WorldCondition cond, boolean replace){
		//generate certain block of world with a given condition.
		
	}
	
	public void setWorldCondition(WorldCondition cond){
		this.cond = cond;
	}

	public Tile getTile(int x, int y){
		xIndex = x / BLOCK_WIDTH;
		yIndex = y / BLOCK_HEIGHT;
		
		//check range.
		if(xIndex > WORLD_WIDTH || yIndex > BLOCK_HEIGHT)
			return null;	//out of index.
		
		TileBlock block = tileBlocks[xIndex][yIndex];
		
		if(block == null){
			//let's create the world!!
			 tileBlocks[xIndex][yIndex] = generateBlock();
		}
		
		xIndex = x % BLOCK_WIDTH;
		yIndex = y % BLOCK_HEIGHT;
		return block.tiles[x][y];
	}

	Random rand = new Random();
	private TileBlock generateBlock(){
		TileBlock block = new TileBlock();
		
		for(int i = 0; i < BLOCK_WIDTH; ++i){
			for(int j = 0; j < BLOCK_HEIGHT; ++j){
				block.tiles[i][j] = new Tile();
				block.tiles[i][j].type = 0;
				block.tiles[i][j].texturename = 1;
				block.tiles[i][j].texturename = 2;
			}
		}
		return block;
	}
}//end of class
