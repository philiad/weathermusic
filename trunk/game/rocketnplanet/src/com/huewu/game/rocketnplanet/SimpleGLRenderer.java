/*
 * Copyright (C) 2008 The Android Open Source Project
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

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import com.huewu.game.rocketnplanet.object.GLSprite;
import com.huewu.game.rocketnplanet.object.Grid;
import com.huewu.game.rocketnplanet.object.ObjectManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

/**
 * An OpenGL ES renderer based on the GLSurfaceView rendering framework.  This
 * class is responsible for drawing a list of renderables to the screen every
 * frame.  It also manages loading of textures and (when VBOs are used) the
 * allocation of vertex buffer objects.
 */
public class SimpleGLRenderer implements Renderer {
	// Specifies the format our textures should be converted to upon load.
	private static BitmapFactory.Options sBitmapOptions
	= new BitmapFactory.Options();
	// An array of things to draw every frame.
	// Pre-allocated arrays to use at runtime so that allocation during the
	// test can be avoided.
	// A reference to the application context.
	private Context mContext;
	// Determines the use of vertex buffer objects.
	private boolean mUseHardwareBuffers;
	private CopyOnWriteArrayList<Runnable> events = new CopyOnWriteArrayList<Runnable>();
	private int[] mCropWorkspace = new int[4];
	private int[] mTextureNameWorkspace = new int[1];

	public SimpleGLRenderer(Context context) {
		// Pre-allocate and store these objects so we can use them at runtime
		// without allocating memory mid-frame.

		// Set our bitmaps to 16-bit, 565 format.
		sBitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;

		mContext = context;
	}

	public void addEvent(Runnable run){
		events.add(run);
	}
	
	public void removeEvent(Runnable run){
		events.remove(run);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		for(Runnable r : events)
			r.run();
		drawFrame(gl);
		ProfileRecorder.sSingleton.endFrame();
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		sizeChanged(gl,width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		surfaceCreated(gl);
		ProfileRecorder.sSingleton.start(ProfileRecorder.PROFILE_FRAME);
	}    

	public int[] getConfigSpec() {
		// We don't need a depth buffer, and don't care about our
		// color depth.
		int[] configSpec = { EGL10.EGL_DEPTH_SIZE, 0, EGL10.EGL_NONE };
		return configSpec;
	}

	/** 
	 * Changes the vertex mode used for drawing.  
	 * @param useVerts  Specifies whether to use a vertex array.  If false, the
	 *     DrawTexture extension is used.
	 * @param useHardwareBuffers  Specifies whether to store vertex arrays in
	 *     main memory or on the graphics card.  Ignored if useVerts is false.
	 */
	public void setVertMode(boolean useVerts, boolean useHardwareBuffers) {
		mUseHardwareBuffers = useVerts ? useHardwareBuffers : false;
	}

	/** Draws the sprites. */
	public void drawFrame(GL10 gl) {
		synchronized (om) {
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			Grid.beginDrawing(gl, true, false);
			for(GLSprite s : om.getAllSprite())
				s.draw(gl);
			Grid.endDrawing(gl);
		}
	}

	/* Called when the size of the window changes. */
	public void sizeChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

		/*
		 * Set our projection matrix. This doesn't have to be done each time we
		 * draw, but usually a new projection needs to be set when the viewport
		 * is resized.
		 */
		gl.glMatrixMode(GL11.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glOrthof(0.0f, width, 0.0f, height, 0.0f, 1.0f);

		gl.glShadeModel(GL11.GL_FLAT);
		gl.glEnable(GL11.GL_BLEND);
		gl.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		gl.glColor4x(0x10000, 0x10000, 0x10000, 0x10000);
		gl.glEnable(GL11.GL_TEXTURE_2D);
	}

	/**
	 * Called whenever the surface is created.  This happens at startup, and
	 * may be called again at runtime if the device context is lost (the screen
	 * goes to sleep, etc).  This function must fill the contents of vram with
	 * texture data and (when using VBOs) hardware vertex arrays.
	 */
	public void surfaceCreated(GL10 gl) {
		/*
		 * Some one-time OpenGL initialization can be made here probably based
		 * on features of this particular context
		 */
		gl.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

		gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
		gl.glShadeModel(GL11.GL_FLAT);
		gl.glDisable(GL11.GL_DEPTH_TEST);
		gl.glEnable(GL11.GL_TEXTURE_2D);
		/*
		 * By default, OpenGL enables features that improve quality but reduce
		 * performance. One might want to tweak that especially on software
		 * renderer.
		 */
		gl.glDisable(GL11.GL_DITHER);
		gl.glDisable(GL11.GL_LIGHTING);

		gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// If we are using hardware buffers and the screen lost context
		// then the buffer indexes that we recorded previously are now
		// invalid.  Forget them here and recreate them below.
		if (mUseHardwareBuffers) {
			for(GLSprite s : om.getAllSprite()){
				// Ditch old buffer indexes.
				s.getGrid().invalidateHardwareBuffers();
			}
		}
		
		om.buildTextureMap(gl, mContext);

		// Load our texture and set its texture name on all sprites.

//		for(GLSprite s : om.getAllSprite()){
//			int resource = s.getResourceId();
//			s.setTextureName(textureMap.getBitamp(gl, resource));
//			if (mUseHardwareBuffers) {
//				Grid currentGrid = s.getGrid();
//				if (!currentGrid.usingHardwareBuffers()) {
//					currentGrid.generateHardwareBuffers(gl);
//				}
//			}
//		}
	}

	/**
	 * We don't need to concern about it. 
	 * android platform will destroy all gl related resoruces, when gl lost event happens.
	 * Called when the rendering thread shuts down.  This is a good place to
	 * release OpenGL ES resources.
	 * @param gl
	 */
	@Deprecated
	public void shutdown(GL11 gl) {

		//release all textures.
		int[] textureToDelete = new int[1];

//		for(Entry<Integer, Integer> texture : textureMap.entrySet()){
//			textureToDelete[0] = texture.getValue(); 
//			gl.glDeleteTextures(1, textureToDelete, 0);
//		}

		//release hardware buffer.

		for(GLSprite s : om.getAllSprite()){
			if (mUseHardwareBuffers) {
				s.getGrid().releaseHardwareBuffers(gl);
			}
		}
	}

	private ObjectManager om = null;
	public void setObjectManager(ObjectManager om) {
		this.om = om;
	}

}//end of class
