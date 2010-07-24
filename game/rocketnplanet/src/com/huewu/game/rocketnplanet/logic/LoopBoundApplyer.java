package com.huewu.game.rocketnplanet.logic;

import com.huewu.game.rocketnplanet.object.Renderable;
import com.huewu.game.rocketnplanet.object.RenderableList;

public class LoopBoundApplyer implements IApplyer{

	private int width;
	private int height;
	private RenderableList mRenderables = new RenderableList();

	public void setBound(int w, int h){
		width = w;
		height = h;
	}	
	
	@Override
	public void apply(float timeDelta) {
		synchronized (mRenderables) {
			if(mRenderables == null)
				return;
			
			for(Renderable r : mRenderables){
				if(r.y <= 0){
					r.y = height;
					r.x = (float)(Math.random() * width);
					r.velocityX = (float)((Math.random() - 0.5f) * 200.0f);
				}
			}
		}
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