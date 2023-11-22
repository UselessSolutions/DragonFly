package useless.dragonfly.helper;

import java.util.List;

public class KeyFrame {
	public final float duration;

	public final List<Float> pose;


	public KeyFrame(float duration, List<Float> pose) {
		this.duration = duration;
		this.pose = pose;
	}
}
