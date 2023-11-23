package useless.dragonfly.model.entity.animation;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class Animation {
	@SerializedName("animations")
	private HashMap<String, AnimationData> animations = new HashMap<>();

	public HashMap<String, AnimationData> getAnimations() {
		return animations;
	}
}
