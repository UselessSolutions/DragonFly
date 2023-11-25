package useless.dragonfly.model.entity.animation;

import java.util.Map;

public class BoneData {
	private final Map<String, PostData> rotation;

	private final Map<String, PostData> position;

	public BoneData(Map<String, PostData> rotation, Map<String, PostData> position) {
		this.rotation = rotation;
		this.position = position;
	}

	public Map<String, PostData> getPosition() {
		return position;
	}

	public Map<String, PostData> getRotation() {
		return rotation;
	}
}
