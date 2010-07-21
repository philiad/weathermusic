package com.huewu.game.rocketnplanet;

import com.huewu.game.rocketnplanet.logic.GraviyApplyer;
import com.huewu.game.rocketnplanet.logic.JumpApplyer;
import com.huewu.game.rocketnplanet.logic.LoopBoundApplyer;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class UserInputHandler implements OnTouchListener, Runnable{
	
	private JumpApplyer jumper = new JumpApplyer();
	private GraviyApplyer graviter = new GraviyApplyer();
	private LoopBoundApplyer looper = new LoopBoundApplyer();
	
    private RenderableList mRenderables;
    
    private long mLastTime;
    private long mLastJumbleTime;
    private int mViewWidth;
    private int mViewHeight;
    
    private float x;
    private float y;
	
    
    private float mPressTimeDelta;
    private float mPressDownTime;
    private float mPressUpTime;
    private float mEnergy = 0.0f;//Float.MAX_VALUE;
    
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			
			jumper.jumpOn(event.getX(), event.getY());
			
			mPressDownTime = SystemClock.uptimeMillis();
			mEnergy = 1200.0f;	//Maximum 1 seconds.
			
			x = event.getX();
			y = mViewHeight - event.getY();
			
			return true;
		case MotionEvent.ACTION_UP:
			
			jumper.jumpOff(event.getX(), event.getY());
			
			mPressUpTime = SystemClock.uptimeMillis();
			mPressTimeDelta =  (mPressUpTime - mPressDownTime) / 1000.0f;
			mEnergy = Math.min(mPressTimeDelta * 600.0f, mEnergy);
			return true;
		}
		return false;
	}
	
    public void setRenderables(RenderableList renderables) {
        mRenderables = renderables;
        Renderable[] r = new Renderable[1];
        jumper.setTargets(renderables.subList(0, 1));
        graviter.setTargets(renderables);
        looper.setTargets(renderables.subList(1, renderables.size()-1));
    }
    
    public void setViewSize(int width, int height) {
        mViewHeight = height;
        mViewWidth = width;
        jumper.setBound(width, height);
        looper.setBound(width, height);
    }

	@Override
	public void run() {
		
        final long time = SystemClock.uptimeMillis();
        final long timeDelta = time - mLastTime;
        final float timeDeltaSeconds = 
            mLastTime > 0.0f ? timeDelta / 1000.0f : 0.0f;
        mLastTime = time;		
        
        jumper.apply(timeDeltaSeconds);
        graviter.apply(timeDeltaSeconds);
		
		for(Renderable r : mRenderables){
			
			//apply velocity
            r.x = r.x + (r.velocityX) * timeDeltaSeconds;
            r.y = r.y + (r.velocityY) * timeDeltaSeconds;
            r.z = r.z + (r.velocityZ) * timeDeltaSeconds;

            //TODO
            //check bound.
            if(r.y <= 0){
            	r.y = 0.0f;
    			r.velocityY = 0.0f;
				r.velocityX = 0.0f;
            }
		}
		
		looper.apply(timeDeltaSeconds);
	}	
}//end of class

