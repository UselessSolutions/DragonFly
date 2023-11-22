package useless.dragonfly.model.entity.animation;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class AnimationData {
	@SerializedName("loop")
	private boolean loop;

	@SerializedName("animation_length")
	private int animation_length;

	@SerializedName("bones")
	private HashMap<String, BoneData> bones = new HashMap<>();

	public boolean isLoop() {
		return loop;
	}

	public int getAnimationLength() {
		return animation_length;
	}

	public HashMap<String, BoneData> getBones() {
		return bones;
	}
}
