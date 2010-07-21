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
    private RenderableList mRenderables;    
    
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

	@Override
	public void apply(float timeDelta) {
    	if(mRenderables == null || bJumping == false)
    		return;
    	
		if(mEnergy > 0){
			//jump!
			float vy = Math.min(height / JUMP_POWER_DIVIDER, mEnergy);	//	mEnergy * timeDeltaSeconds;
			mEnergy -= height * timeDelta;
			
	    	for(Renderable r : mRenderables){
				float vx = (x - r.x);
	    		//set velocity.
				r.velocityX = vx;
				r.velocityY = vy;
	    	}		
		}
    }

	@Override
	public void setTargets(RenderableList renderable) {
    	mRenderables = renderable;
	}
	
	public void setBound(int w, int h){
		width = w;
		height = h;
	}
}//end of class
