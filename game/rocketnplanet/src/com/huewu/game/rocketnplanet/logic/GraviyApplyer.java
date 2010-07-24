package com.huewu.game.rocketnplanet.logic;

import com.huewu.game.rocketnplanet.object.Renderable;
import com.huewu.game.rocketnplanet.object.RenderableList;

public class GraviyApplyer implements IApplyer{

    private RenderableList mRenderables = new RenderableList();
    private final static float GRAVITY = 800.0f;     
	
	@Override
	public void apply(float timeDelta) {
		synchronized (mRenderables) {
			if(mRenderables == null)
				return;
			
			for(Renderable r : mRenderables)
				r.velocityY -= GRAVITY * timeDelta * r.weight;				
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
