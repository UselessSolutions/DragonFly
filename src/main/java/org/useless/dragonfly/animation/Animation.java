package org.useless.dragonfly.animation;

import java.util.List;

public class Animation {
	private final List<AnimationData> animations;
	private final float animation_length;
	private final boolean loop;


	public List<AnimationData> getAnimations() {
		return animations;
	}


	public float getAnimationLength() {
		return animation_length;
	}

	public boolean isLoop() {
		return loop;
	}

	public Animation(List<AnimationData> animations, float animationLength, boolean loop) {
		this.animations = animations;
		animation_length = animationLength;
		this.loop = loop;
	}
}
