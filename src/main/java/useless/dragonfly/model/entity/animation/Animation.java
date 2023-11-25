package useless.dragonfly.model.entity.animation;

import java.util.Map;

public class Animation {
	private final Map<String, AnimationData> animations;

	public Map<String, AnimationData> getAnimations() {
		return animations;
	}

	public Animation(Map<String, AnimationData> animations) {
		this.animations = animations;
	}
}
