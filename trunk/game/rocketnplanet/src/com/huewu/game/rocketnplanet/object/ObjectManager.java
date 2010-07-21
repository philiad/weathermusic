package com.huewu.game.rocketnplanet.object;

import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;

import com.huewu.game.rocketnplanet.R;

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
	final GridPool gridPool = new GridPool();	
	
	private ObjectManager(){
	}
	
	public static ObjectManager LoadSettings(WeakReference<Activity> activity, InputStream is){
		ObjectManager om = new ObjectManager();
		
		//TODO read setting. parse xml. 
		int robotCount = 1;
		int SPRITE_WIDTH = 64;
		int SPRITE_HEIGHT = 64;

		// We need to know the width and height of the display pretty soon,
		// so grab the information now.
		DisplayMetrics dm = new DisplayMetrics();
		activity.get().getWindowManager().getDefaultDisplay().getMetrics(dm);

		GLSprite background = new GLSprite(R.drawable.background);
		BitmapDrawable backgroundImage = (BitmapDrawable)activity.get().getResources().getDrawable(R.drawable.background);
		Bitmap backgoundBitmap = backgroundImage.getBitmap();
		background.width = backgoundBitmap.getWidth();
		background.height = backgoundBitmap.getHeight();

		// Setup the background grid.  This is just a quad.
		Grid backgroundGrid = om.gridPool.requireGrid(2, 2, false);
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
		spriteGrid = om.gridPool.requireGrid(2,2,false);
		spriteGrid.set(0, 0,  0.0f, 0.0f, 0.0f, 0.0f , 1.0f, null);
		spriteGrid.set(1, 0, SPRITE_WIDTH, 0.0f, 0.0f, 1.0f, 1.0f, null);
		spriteGrid.set(0, 1, 0.0f, SPRITE_HEIGHT, 0.0f, 0.0f, 0.0f, null);
		spriteGrid.set(1, 1, SPRITE_WIDTH, SPRITE_HEIGHT, 0.0f, 1.0f, 0.0f, null);

		// This list of things to move. It points to the same content as the
		// sprite list except for the background.
		final int robotBucketSize = robotCount / 3;
		for (int x = 0; x < robotCount; x++) {
			GLSprite robot;
			// Our robots come in three flavors.  Split them up accordingly.
			if (x < robotBucketSize) {
				robot = new GLSprite(R.drawable.skate1);
			} else if (x < robotBucketSize * 2) {
				robot = new GLSprite(R.drawable.skate2);
			} else {
				robot = new GLSprite(R.drawable.skate3);
			}

			robot.width = SPRITE_WIDTH;
			robot.height = SPRITE_HEIGHT;

			// Pick a random location for this sprite.
			robot.x = (float)(Math.random() * dm.widthPixels);
			robot.y = (float)(Math.random() * dm.heightPixels);

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
			meteor = new GLSprite(R.drawable.moon);

			meteor.width = SPRITE_WIDTH;
			meteor.height = SPRITE_HEIGHT;

			// Pick a random location for this sprite.
			meteor.x = (float)(Math.random() * dm.widthPixels);
			meteor.y = (float)(Math.random() * dm.heightPixels);

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
	
	public RenderableList getCharacter() {
		return character;
	}
	
	public RenderableList getEnemy() {
		return enemy;
	}
	
	public RenderableList getObstacle() {
		return obstacle;
	}
	
}//end of class
