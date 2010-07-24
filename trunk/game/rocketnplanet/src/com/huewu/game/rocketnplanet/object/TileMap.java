package com.huewu.game.rocketnplanet.object;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import com.huewu.game.rocketnplanet.object.ObjectManager.TextureMap;

public class TileMap extends GLSprite{

	private int ctW;
	private int ctH;
	private float tileWidth;
	private float tileHeight;

	TileMap(int resourceId, TextureMap textures) {
		super(resourceId, textures);
	}

	public void createTiles(float tileWidth, float tileHeight, float worldWidth, float worldHeight){
		ctW = (int) (worldWidth / tileWidth + 1);
		ctH = (int) (worldHeight / tileHeight + 1);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	@Override
	public void draw(GL10 gl) {
		gl.glBindTexture(GL11.GL_TEXTURE_2D, textures.getTextureName(mResourceId));
		gl.glPushMatrix();
		gl.glLoadIdentity();

        for(int j = 0; j < ctH; ++j){
            for(int i = 0; i < ctW; ++i){
                mGrid.draw(gl, true, false);
        		gl.glTranslatef(
        				tileWidth, 
        				0.0f,
                        0.0f);
            }
    		gl.glTranslatef(
    				-tileWidth * ctW, 
    				0.0f,
                    0.0f);
    		gl.glTranslatef(
    				0.0f,
    				tileHeight,
                    0.0f);
        }

		gl.glPopMatrix();
		
//		
//		
//		
//		
//		
//        gl.glBindTexture(GL11.GL_TEXTURE_2D, mTextureName);
//
//        if (mGrid == null) {
//            // Draw using the DrawTexture extension.
//            ((GL11Ext) gl).glDrawTexfOES(x, y, z, width, height);
//        } else {
//            // Draw using verts or VBO verts.
//            gl.glPushMatrix();
//            gl.glLoadIdentity();
//            gl.glTranslatef(
//                    x, 
//                    y, 
//                    z);
//            
//            mGrid.draw(gl, true, false);
//            gl.glPopMatrix();
//        }
	}

}//end of class
