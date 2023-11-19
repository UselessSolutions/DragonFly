package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

public class FaceData {
	@SerializedName("uv")
	public int[] uv = new int[4];
	@SerializedName("texture")
	public String texture = null;
	@SerializedName("cullface")
	public String cullface = null;
	@SerializedName("rotation")
	public int rotation = 0; // can only be values 0, 90, 180, or 270
	@SerializedName("tintindex")
	public int tintindex = -1; // Should mean white tint, unsure how to handle other values seems to pass it into BlockColor to get a color from there in vanilla

}
