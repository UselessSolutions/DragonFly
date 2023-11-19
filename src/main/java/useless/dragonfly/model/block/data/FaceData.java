package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

public class FaceData {
	@SerializedName("uv")
	public int[] uv = new int[0];
	@SerializedName("texture")
	public String texture = null;
	@SerializedName("tintindex")
	public int tintindex = -1; // Should mean white tint, unsure how to handle other values seems to pass it into BlockColor to get a color from there in vanilla

}
