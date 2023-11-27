package useless.dragonfly.model.entity.animation;

import java.util.Map;

public class AnimationData {
	private final boolean loop;

	private final float animation_length;

	private final Map<String, BoneData> bones;

	public AnimationData(boolean loop, float animationLength, Map<String, BoneData> bones) {
		this.loop = loop;

		this.animation_length = animationLength;
		this.bones = bones;
	}

	public boolean isLoop() {
		return loop;
	}

	public float getAnimationLength() {
		return animation_length;
	}

	public Map<String, BoneData> getBones() {
		return bones;
	}
}
