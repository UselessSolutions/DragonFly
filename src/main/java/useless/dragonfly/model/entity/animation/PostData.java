package useless.dragonfly.model.entity.animation;

import java.util.List;

public class PostData {
	private final List<Float> post;

	public PostData(List<Float> post) {
		this.post = post;
	}

	public List<Float> getPost() {
		return post;
	}
}
