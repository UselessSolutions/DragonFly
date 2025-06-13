package org.useless.dragonfly.animation;

import java.util.List;

public class KeyFrameData {
	private final List<Float> target;
	private final float timestamp;
	private final String interpolation;

	public KeyFrameData(List<Float> target, float timestamp, String interpolation) {
		this.target = target;
		this.timestamp = timestamp;
		this.interpolation = interpolation;
	}

	public List<Float> getTarget() {
		return target;
	}

	public String getInterpolation() {
		return interpolation;
	}

	public float getTimestamp() {
		return timestamp;
	}
}
