package com.huewu.game.rocketnplanet.logic;

import android.graphics.Rect;
import android.graphics.RectF;

import com.huewu.game.rocketnplanet.object.Renderable;
import com.huewu.game.rocketnplanet.object.RenderableList;

public class ScrollApplyer implements IApplyer{

    private RenderableList mRenderables = new RenderableList();
    private Renderable mHero = null;
    
	RectF scrollBound = new RectF();
	RectF fixBound = new RectF();
	
	boolean bRightScroll = false;
	boolean bLeftScroll = false;
	
	@Override
	public void apply(float timeDelta) {
		synchronized (mRenderables) {
			
			if(mHero == null)	//no hero, no move.
				return;
			
			float hx = mHero.x + mHero.width / 2.0f;
			float hvx = mHero.velocityX;
				
			if(hx > scrollBound.right)
				bRightScroll = (hvx > 0);
			else if(hx < scrollBound.left)
				bLeftScroll = (hvx < 0);
			
			if(bRightScroll ||  bLeftScroll){
				if(fixBound.contains(hx, mHero.y) == true || hvx == 0.0f){
					bRightScroll = bLeftScroll = false;
				}
			}
			
			//move bound = viewWidth / 4
			//move world.
			
			if(bRightScroll || bLeftScroll){
				mHero.x -= hvx * timeDelta * 1.0f;
				for(Renderable r : mRenderables)
					r.x -= hvx * timeDelta * 1.0f;				
			}
		}
	}
	
	
	void setViewSize(int w, int h){
		
		int c = w / 2;
		int s = w / 6;
		
		scrollBound.bottom = h;
		scrollBound.top = 0;
		scrollBound.left = s;
		scrollBound.right = w - s;
		
		fixBound.bottom = h;
		fixBound.top = 0;
		fixBound.left = c - s;
		fixBound.right = c + s;
	}
	
	void setHero(RenderableList hero){
		mHero = hero.get(0);
	}
	
	void setWorldMap(){
		
	}

	@Override
	public void addTargets(RenderableList renderable) {
		synchronized (mRenderables) {
			mRenderables.addAll(renderable);
		}
	}
	
	@Override
	public void removeTargets(RenderableList renderable) {
		synchronized (mRenderables) {
			mRenderables.removeAll(renderable);
		}
	}
}//end of class
