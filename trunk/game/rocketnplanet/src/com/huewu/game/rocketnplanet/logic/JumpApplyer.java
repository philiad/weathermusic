package com.huewu.game.rocketnplanet.logic;

import com.huewu.game.rocketnplanet.object.Renderable;
import com.huewu.game.rocketnplanet.object.RenderableList;

import android.os.SystemClock;

public class JumpApplyer implements IApplyer{
	
    private float x;
    private float y;
    private float width;
    private float height;
    private float mPressTimeDelta;
    private float mPressDownTime;
    private float mPressUpTime;
    private float mEnergy = 0.0f;//Float.MAX_VALUE;	
    private RenderableList mRenderables = new RenderableList();    
    
    private final static float JUMP_POWER_DIVIDER = 2.0f;
    
    boolean bJumping = false;
    
    public void jumpOn(float x, float y){
    	bJumping = true;
    	this.x = x;
    	this.y = y;
		mPressDownTime = SystemClock.uptimeMillis();
		mEnergy = (height / JUMP_POWER_DIVIDER) * 2.0f;
    }
    
    public void jumpOff(float x, float y){
    	bJumping = false;
		mPressUpTime = SystemClock.uptimeMillis();
		
		if(mPressDownTime > mPressUpTime)	//something wrong. reset value.
			mPressTimeDelta = 0.0f;
		else
			mPressTimeDelta = mPressUpTime - mPressDownTime;
		
		mEnergy = Math.min(mPressTimeDelta * (height / JUMP_POWER_DIVIDER), mEnergy);		
    }
    
	public void setBound(int w, int h){
		width = w;
		height = h;
	}    

	@Override
	public void apply(float timeDelta) {
		synchronized (mRenderables) {
	    	if(mRenderables == null || bJumping == false)
	    		return;
	    	
			if(mEnergy > 0){
				//jump!
				float vy = Math.min(height / JUMP_POWER_DIVIDER, mEnergy);	//	mEnergy * timeDeltaSeconds;
				mEnergy -= height * timeDelta;
				
		    	for(Renderable r : mRenderables){
					float vx = (x - (r.x + r.width / 2.0f)) * 3.0f;;
					
					if(vx > 0)
						vx = Math.min(vx, width / JUMP_POWER_DIVIDER);
					else
						vx = Math.max(vx, -width / JUMP_POWER_DIVIDER);
					
		    		//set velocity.
					r.velocityX = vx;
					r.velocityY = vy;
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
