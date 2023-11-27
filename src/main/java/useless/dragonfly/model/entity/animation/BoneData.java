package useless.dragonfly.model.entity.animation;

import java.util.Map;

public class BoneData {
	private final Map<String, PostData> rotation;

	private final Map<String, PostData> position;

	private final Map<String, PostData> scale;

	public BoneData(Map<String, PostData> rotation, Map<String, PostData> position, Map<String, PostData> scale) {
		this.rotation = rotation;
		this.position = position;
		this.scale = scale;
	}

	public Map<String, PostData> getPosition() {
		return position;
	}

	public Map<String, PostData> getRotation() {
		return rotation;
	}

	public Map<String, PostData> getScale() {
		return scale;
	}
}
