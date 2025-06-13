package org.useless.dragonfly.animation;

import java.util.List;

public class AnimationData {

	private final String bone;
	private final String target;
	private final List<KeyFrameData> keyFrames;

	public AnimationData(String bone, String target, List<KeyFrameData> keyFrames) {

		this.bone = bone;
		this.target = target;
		this.keyFrames = keyFrames;
	}

	public String getTarget() {
		return target;
	}

	public String getBone() {
		return bone;
	}

	public List<KeyFrameData> getKeyFrames() {
		return keyFrames;
	}
}
