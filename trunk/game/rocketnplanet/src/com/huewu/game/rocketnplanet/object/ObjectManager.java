package com.huewu.game.rocketnplanet.object;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.LinkedHashMap;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.huewu.game.rocketnplanet.R;
import com.huewu.game.rocketnplanet.logic.ScrollApplyer;

public class ObjectManager {
	
	final SpriteList allSprite = new SpriteList();
	//handle user input
	final RenderableList character = new RenderableList();
	//move independently
	final RenderableList enemy = new RenderableList();
	//move with background
	final RenderableList obstacle = new RenderableList();	
	//scrolled by user character's position.
	final RenderableList background = new RenderableList();	
	final RenderableList tileMap = new RenderableList();	
	final GridPool gridPool = new GridPool();	
	final TextureMap textureMap = new TextureMap();
	
	private ObjectManager(){
	}
	
	public static ObjectManager LoadSettings(Context context, DisplayMetrics dm, InputStream is){
		ObjectManager om = new ObjectManager();
		
		//TODO read setting. parse xml. 
		int robotCount = 1;
		int SPRITE_WIDTH = 64;
		int SPRITE_HEIGHT = 64;

		// We need to know the width and height of the display pretty soon,
		// so grab the information now.
		
		int resList[] = new int[] {R.drawable.wood_bar, R.drawable.background, R.drawable.moon, R.drawable.skate1, R.drawable.skate2, R.drawable.skate3, R.drawable.icon};
		om.textureMap.setResourceList(resList);

		GLSprite background = new GLSprite(R.drawable.background, om.textureMap);
		BitmapDrawable backgroundImage = (BitmapDrawable)context.getResources().getDrawable(R.drawable.background);
		Bitmap backgoundBitmap = backgroundImage.getBitmap();
		background.width = backgoundBitmap.getWidth();
		background.height = backgoundBitmap.getHeight();

		// Setup the background grid.  This is just a quad.
		Grid backgroundGrid = om.gridPool.requireGrid(GridPool.BACKGROUND_GRID);
		backgroundGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f, 1.0f, null);
		backgroundGrid.set(1, 0, background.width, 0.0f, 0.0f, 1.0f, 1.0f, null);
		backgroundGrid.set(0, 1, 0.0f, background.height, 0.0f, 0.0f, 0.0f, null);
		backgroundGrid.set(1, 1, background.width, background.height, 0.0f, 
				1.0f, 0.0f, null );
		background.setGrid(backgroundGrid);
		om.allSprite.add(background);
		om.background.add(background);

		Grid spriteGrid = null;
		// Setup a quad for the sprites to use.  All sprites will use the
		// same sprite grid intance.
		spriteGrid = om.gridPool.requireGrid(GridPool.SPRITE_GRID);
		spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		spriteGrid.set(1, 0, SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		spriteGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		spriteGrid.set(1, 1, SPRITE_WIDTH, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);
		
		// Tile grid instance
		Grid tileGrid = om.gridPool.requireGrid(GridPool.TILE_GRID);
		tileGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		tileGrid.set(1, 0, SPRITE_WIDTH * 4.0f, 0.0f, 0.0f, 1.0f, 1.0f, null);
		tileGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		tileGrid.set(1, 1, SPRITE_WIDTH * 4.0f, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);
		
		TileMapSprite tileMap = new TileMapSprite(new int[]{R.drawable.wood_bar, R.drawable.moon, R.drawable.skate3}, om.textureMap);
		tileMap.createTiles(SPRITE_WIDTH * 4.0f, SPRITE_HEIGHT, background.width, background.height);
		tileMap.setGrid(tileGrid);
		om.tileMap.add(tileMap);	
		om.allSprite.add(tileMap);

		// This list of things to move. It points to the same content as the
		// sprite list except for the background.
		final int robotBucketSize = robotCount / 3;
		for (int x = 0; x < robotCount; x++) {
			GLSprite robot;
			// Our robots come in three flavors.  Split them up accordingly.
			if (x < robotBucketSize) {
				robot = new GLSprite(R.drawable.skate1, om.textureMap);
			} else if (x < robotBucketSize * 2) {
				robot = new GLSprite(R.drawable.skate2, om.textureMap);
			} else {
				robot = new GLSprite(R.drawable.skate3, om.textureMap);
			}

			robot.width = SPRITE_WIDTH;
			robot.height = SPRITE_HEIGHT;

			// Pick a random location for this sprite.
			robot.x = (float)(Math.random() * dm.widthPixels);
			robot.y = (float)(Math.random() * dm.heightPixels);
			robot.weight = 1.5f;

			// All sprites can reuse the same grid.  If we're running the
			// DrawTexture extension test, this is null.
			robot.setGrid(spriteGrid);

			// Add this robot to the spriteArray so it gets drawn and to the
			// renderableArray so that it gets moved.
			om.allSprite.add(robot);
			om.character.add(robot);			
		}
		
		for (int x = 0; x < 10; x++) {
			GLSprite meteor;
			// Our robots come in three flavors.  Split them up accordingly.
			meteor = new GLSprite(R.drawable.moon, om.textureMap);

			meteor.width = SPRITE_WIDTH;
			meteor.height = SPRITE_HEIGHT;

			// Pick a random location for this sprite.
			meteor.x = (float)(Math.random() * dm.widthPixels);
			meteor.y = (float)(Math.random() * dm.heightPixels);
			meteor.weight = 0.5f;

			// All sprites can reuse the same grid.  If we're running the
			// DrawTexture extension test, this is null.
			meteor.setGrid(spriteGrid);

			// Add this robot to the spriteArray so it gets drawn and to the
			// renderableArray so that it gets moved.
			om.allSprite.add(meteor);			
			om.enemy.add(meteor);			
		}		
		
		return om;
	}
	
	public SpriteList getAllSprite() {
		return allSprite;
	}
	
	public RenderableList getBackground() {
		return background;
	}
	
	public RenderableList getHero() {
		return character;
	}
	
	public RenderableList getEnemy() {
		return enemy;
	}
	
	public RenderableList getObstacle() {
		return obstacle;
	}
	
	public RenderableList getTileMap(){
		return tileMap;
	}

	public void initObjectManager(GL10 gl, Context context) {
		textureMap.clearTextureMap();
		textureMap.buildTextureMap(gl, context);
		gridPool.releaseHardwareBuffers(gl, context);
		gridPool.generateHardwareBufffers(gl, context);
	}
	
	class TextureMap extends LinkedHashMap<Integer, Integer>{

		private static final long serialVersionUID = 1620883313432432583L;
		private BitmapFactory.Options sBitmapOptions;	
		private int[] mCropWorkspace = new int[4];
		private int[] mTextureNameWorkspace = new int[1];	
		
		int resList[];
		
		public void setResourceList(int[] resList){
			this.resList = resList;
		}
		
		public void buildTextureMap(GL10 gl, Context context){
			if(gl == null || context == null || resList == null)
				return;
			
			sBitmapOptions = new Options();
			sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
			for(int r : resList){
				buildTexture(gl, context, r);
			}
		}
		
		public void clearTextureMap(){
			this.clear();
		}

		public int getTextureName(int resourceId){
			if(get(resourceId) == null)
				return -1;
			else
				return get(resourceId);			
//			if(this.containsKey(resourceId) == true)
//				return get(resourceId);
//			else
//				return -1;
		}
		
		/** 
		 * Loads a bitmap into OpenGL and sets up the common parameters for 
		 * 2D texture maps. 
		 */
		protected void buildTexture(GL10 gl, Context context, int resourceId) {

			int textureName = -1;
			if(this.containsKey(resourceId) == true)
				return;
			
			else if (context != null && gl != null) {
				gl.glGenTextures(1, mTextureNameWorkspace, 0);
				textureName = mTextureNameWorkspace[0];
				gl.glBindTexture(GL11.GL_TEXTURE_2D, textureName);

				gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
				gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

				gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
				gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);

				gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_REPLACE);

				InputStream is = context.getResources().openRawResource(resourceId);
				Bitmap bitmap;
				try {
					bitmap = BitmapFactory.decodeStream(is, null, sBitmapOptions);
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						// Ignore.
					}
				}

				GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

				mCropWorkspace[0] = 0;
				mCropWorkspace[1] = bitmap.getHeight();
				mCropWorkspace[2] = bitmap.getWidth();
				mCropWorkspace[3] = -bitmap.getHeight();

				bitmap.recycle();

				((GL11) gl).glTexParameteriv(GL10.GL_TEXTURE_2D, 
						GL11Ext.GL_TEXTURE_CROP_RECT_OES, mCropWorkspace, 0);


				int error = gl.glGetError();
				if (error != GL10.GL_NO_ERROR) {
					Log.e("SpriteMethodTest", "Texture Load GLError: " + error);
				}else{
					put(resourceId, textureName);
				}
			}
		}	
	}//end of class	Texture Map.
}//end of class
