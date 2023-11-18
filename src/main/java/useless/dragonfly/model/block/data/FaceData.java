package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

public class FaceData {
	@SerializedName("uv")
	public int[] uv;
	@SerializedName("texture")
	public String texture;
}
