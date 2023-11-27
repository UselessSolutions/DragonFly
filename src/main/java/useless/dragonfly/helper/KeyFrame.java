package useless.dragonfly.helper;


import useless.dragonfly.utilities.vector.Vector3f;

import java.util.List;

public class KeyFrame {
	public final float duration;

	public final List<Float> pose;

	public final String lerp_mode;


	public KeyFrame(float duration, List<Float> pose, String lerp_mode) {
		this.duration = duration;
		this.pose = pose;
		this.lerp_mode = lerp_mode;
	}

	public Vector3f vector3f() {
		return new Vector3f(this.pose.get(0), this.pose.get(1), this.pose.get(2));
	}
}
