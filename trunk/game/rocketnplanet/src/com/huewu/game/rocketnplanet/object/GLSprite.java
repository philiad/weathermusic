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

package com.huewu.game.rocketnplanet.object;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;
import javax.microedition.khronos.opengles.GL11Ext;

import com.huewu.game.rocketnplanet.object.ObjectManager.TextureMap;



/**
 * This is the OpenGL ES version of a sprite.  It is more complicated than the
 * CanvasSprite class because it can be used in more than one way.  This class
 * can draw using a grid of verts, a grid of verts stored in VBO objects, or
 * using the DrawTexture extension.
 */
public class GLSprite extends Renderable {
	// The OpenGL ES texture handle to draw.
	int mTextureName;
	// The id of the original resource that mTextureName is based on.
	int mResourceId;
	// If drawing with verts or VBO verts, the grid object defining those verts.
	Grid mGrid;
	TextureMap textures;

	public GLSprite(int resourceId, TextureMap textures) {
		super();
		mResourceId = resourceId;
		this.textures = textures;
		mTextureName = textures.getTextureName(mResourceId);
	}

	public void setGrid(Grid grid) {
		mGrid = grid;
	}

	public Grid getGrid() {
		return mGrid;
	}

	public void draw(GL10 gl) {
		if(mTextureName == -1)
			mTextureName = textures.getTextureName(mResourceId);
		
		gl.glBindTexture(GL11.GL_TEXTURE_2D, mTextureName);

		if (mGrid == null) {
			// Draw using the DrawTexture extension.
			((GL11Ext) gl).glDrawTexfOES(x, y, z, width, height);
		} else {
			// Draw using verts or VBO verts.
			gl.glPushMatrix();
			gl.glLoadIdentity();
			gl.glTranslatef(
					x, 
					y, 
					z);

			mGrid.draw(gl, true, false);
			gl.glPopMatrix();
		}
	}
}//end of class
