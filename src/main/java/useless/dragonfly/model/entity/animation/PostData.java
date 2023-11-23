package useless.dragonfly.model.entity.animation;

import com.google.gson.annotations.SerializedName;
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.List;

public class PostData {
	@SerializedName("post")
	private List<Float> post = Lists.newArrayList();

	public List<Float> getPost() {
		return post;
	}
}
