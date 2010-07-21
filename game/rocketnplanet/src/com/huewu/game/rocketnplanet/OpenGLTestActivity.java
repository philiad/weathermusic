/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huewu.game.rocketnplanet;

import java.lang.ref.WeakReference;

import com.huewu.game.rocketnplanet.R;
import com.huewu.game.rocketnplanet.logic.UserInputHandler;
import com.huewu.game.rocketnplanet.object.GLSprite;
import com.huewu.game.rocketnplanet.object.Grid;
import com.huewu.game.rocketnplanet.object.ObjectManager;
import com.huewu.game.rocketnplanet.object.RenderableList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;

/**
 * Activity for testing OpenGL ES drawing speed.  This activity sets up sprites 
 * and passes them off to an OpenGLSurfaceView for rendering and movement.
 */
public class OpenGLTestActivity extends Activity {
	private final static int SPRITE_WIDTH = 64;
	private final static int SPRITE_HEIGHT = 64;

	private GLSurfaceView mGLSurfaceView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGLSurfaceView = new GLSurfaceView(this);
		SimpleGLRenderer spriteRenderer = new SimpleGLRenderer(this);

		// Clear out any old profile results.
		ProfileRecorder.sSingleton.resetAll();
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);		
		
		ObjectManager om = ObjectManager.LoadSettings(new WeakReference<Activity>(this), null);

		// Now's a good time to run the GC.  Since we won't do any explicit
		// allocation during the test, the GC should stay dormant and not
		// influence our results.
		Runtime r = Runtime.getRuntime();
		r.gc();

		spriteRenderer.setSprites(om.getAllSprite());
		spriteRenderer.setVertMode(true, true);
		
		UserInputHandler handler = new UserInputHandler(); 
		handler.setRenderables(om.getCharacter());
		handler.setViewSize(dm.widthPixels, dm.heightPixels);
		spriteRenderer.setEvent(handler);
		
//		if (animate) {
//			Mover simulationRuntime = new Mover();
//			simulationRuntime.setRenderables(renderableArray);
//			simulationRuntime.setViewSize(dm.widthPixels, dm.heightPixels);
//			spriteRenderer.setEvent(simulationRuntime);
//		}

		mGLSurfaceView.setRenderer(spriteRenderer);
		setContentView(mGLSurfaceView);
		
		mGLSurfaceView.setOnTouchListener(handler);
	}
}//end of class
