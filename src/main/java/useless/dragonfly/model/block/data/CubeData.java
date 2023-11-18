package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;
import useless.dragonfly.model.block.processed.BlockFace;

import java.util.HashMap;

public class CubeData {
	@SerializedName("from")
	public float[] from;
	@SerializedName("to")
	public float[] to;
	@SerializedName("color")
	public int color;
	@SerializedName("faces")
	public HashMap<String, BlockFace> faces;
}
