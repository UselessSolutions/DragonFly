package useless.dragonfly.model.entity.animation;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

public class BoneData {
	@SerializedName("rotation")
	private HashMap<String, List<Float>> rotation = new HashMap<>();

	@SerializedName("position")
	private HashMap<String, List<Float>> postion = new HashMap<>();

	public HashMap<String, List<Float>> getPostion() {
		return postion;
	}

	public HashMap<String, List<Float>> getRotation() {
		return rotation;
	}
}
