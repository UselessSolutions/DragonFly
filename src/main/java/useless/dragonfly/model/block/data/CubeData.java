package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CubeData {
	@SerializedName("from")
	public float[] from = new float[0];
	@SerializedName("to")
	public float[] to = new float[0];
	@SerializedName("color")
	public int color = 0;
	@SerializedName("faces")
	public HashMap<String, FaceData> faces = new HashMap<>();
}
