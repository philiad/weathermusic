package com.huewu.game.rocketnplanet.logic;

import com.huewu.game.rocketnplanet.Renderable;
import com.huewu.game.rocketnplanet.RenderableList;

public class GraviyApplyer implements IApplyer{

    private RenderableList mRenderables;
    private final static float GRAVITY = 400.0f;     
	
	@Override
	public void apply(float timeDelta) {
		if(mRenderables == null)
			return;
		
		for(Renderable r : mRenderables)
			r.velocityY -= GRAVITY * timeDelta;				
	}

	@Override
	public void setTargets(RenderableList renderable) {
		mRenderables = renderable;
	}
	
}//end of class
