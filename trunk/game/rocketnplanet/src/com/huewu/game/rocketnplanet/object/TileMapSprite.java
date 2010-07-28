package com.huewu.game.rocketnplanet.object;

import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import com.huewu.game.rocketnplanet.object.ObjectManager.TextureMap;

/**
 * 
 * @author huewu.yang
 * @date 2010-07-26
 *
 */
public class TileMapSprite extends GLSprite{

	private int ctW;
	private int ctH;
	private float tileWidth;
	private float tileHeight;
	private byte map[];
	private int textureList[];
	private int tileXIndex = 0;
	private int tileYIndex = 0;

	TileMapSprite(int resList[], TextureMap textures) {
		super(-1, textures);
		
		if(resList != null){
			textureList = new int[resList.length];
			for(int i = 0; i < resList.length; ++i)
				textureList[i] = textures.getTextureName(resList[i]);
		}
	}

	public void createTiles(float tileWidth, float tileHeight, float worldWidth, float worldHeight){
		ctW = (int) (worldWidth / tileWidth + 1);
		ctH = (int) (worldHeight / tileHeight + 1);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		
		//TODO how to generate a world map.
		map = new byte[ctW * ctH * 10];
		Random r = new Random();
		for(int i = 0; i < ctW * ctH * 10; ++i){
			map[i] = (byte) r.nextInt(3); 
		}
	}

	@Override
	public void draw(GL10 gl) {
		
		//whenever tile X or tile Y index is changed, tile map should require a new map block. 
		

		gl.glPushMatrix();
		tileXIndex = (int)-this.x / (int)tileWidth;
		//adjust position.
		int dif = (int)this.x % (int)tileWidth;
		if(dif > 0){
			dif = (int) (dif - tileWidth);
			tileXIndex -= 1;
		}

		tileXIndex += ctW * 5;

		int startTilePosY = (int)this.y / (int)tileHeight;		
		for(int j = 0; j < ctH; ++j){
			int locY = startTilePosY + j;
			for(int i = 0; i < ctW; ++i){
				int locX = tileXIndex + i;
				gl.glBindTexture(GL11.GL_TEXTURE_2D, getTextureName(locX, locY));
				gl.glLoadIdentity();
				gl.glTranslatef(
						dif, 
						0.0f,
						0.0f);
				gl.glTranslatef(
						tileWidth * i, 
						tileHeight * j,
						0.0f);
				mGrid.draw(gl, true, false);
			}
		}
		gl.glPopMatrix();
	}
	
	private int getTextureName(int x, int y){
		int t = map[x + y * ctW * 10];
		return textureList[t];
	}

}//end of class
