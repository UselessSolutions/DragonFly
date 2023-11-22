package useless.dragonfly.model.entity.animation;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class BoneData {
	@SerializedName("rotation")
	private HashMap<String, PostData> rotation = new HashMap<>();

	@SerializedName("position")
	private HashMap<String, PostData> postion = new HashMap<>();

	public HashMap<String, PostData> getPostion() {
		return postion;
	}

	public HashMap<String, PostData> getRotation() {
		return rotation;
	}
}
