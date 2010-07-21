package com.huewu.game.rocketnplanet;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderableList extends CopyOnWriteArrayList<Renderable>{
	
	public RenderableList(){
		super();
	}
	
	public RenderableList(List<Renderable> subList) {
		super();
		addAll(subList);
	}

	@Override
	public RenderableList subList(int fromIndex, int toIndex) {
		return new RenderableList(super.subList(fromIndex, toIndex));
	}
}
