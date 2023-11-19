package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class CubeData {
	@SerializedName("from")
	public float[] from = new float[3];
	@SerializedName("to")
	public float[] to = new float[3];
	@SerializedName("rotation")
	public RotationData rotation;
	@SerializedName("shade")
	public boolean shade = true;
	@SerializedName("faces")
	public HashMap<String, FaceData> faces = new HashMap<>();
	@SerializedName("color")
	public int color = 0; // Block bench exports this value, but I don't see it mention on the wiki https://minecraft.wiki/w/Tutorials/Models
}
