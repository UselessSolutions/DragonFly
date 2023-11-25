package useless.dragonfly.model.entity.animation;

import java.util.List;

public class PostData {
	private final List<Float> post;
	private final String lerp_mode;

	public PostData(List<Float> post, String lerp_mode) {
		this.post = post;
		this.lerp_mode = lerp_mode;
	}

	public List<Float> getPost() {
		return post;
	}

	public String getLerpMode() {
		return lerp_mode;
	}
}
