package com.huewu.game.rocketnplanet.logic;

import com.huewu.game.rocketnplanet.object.RenderableList;

public interface IApplyer {
	public void addTargets(RenderableList renderable);
	public void removeTargets(RenderableList renderable);
	public void apply(float timeDelta);
}//end of interface
