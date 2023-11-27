package useless.dragonfly.helper;


import useless.dragonfly.utilities.vector.Vector3f;

import java.util.List;

public class KeyFrame {
	public final float duration;

	public final List<Float> pose;


	public KeyFrame(float duration, List<Float> pose) {
		this.duration = duration;
		this.pose = pose;
	}

	public Vector3f vector3f() {
		return new Vector3f(this.pose.get(0), this.pose.get(1), this.pose.get(2));
	}
}
