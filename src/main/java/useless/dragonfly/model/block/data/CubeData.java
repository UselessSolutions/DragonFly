package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;
import useless.dragonfly.utilities.Utilities;

import java.util.Arrays;
import java.util.HashMap;

public class CubeData {
	@SerializedName("from")
	public float[] from = new float[3];
	@SerializedName("to")
	public float[] to = new float[3];
	@SerializedName("rotation")
	public RotationData rotation = new RotationData();
	@SerializedName("shade")
	public boolean shade = true;
	@SerializedName("faces")
	public HashMap<String, FaceData> faces = new HashMap<>();
	@SerializedName("color")
	public int color = 0; // Block bench exports this value, but I don't see it mention on the wiki https://minecraft.wiki/w/Tutorials/Models
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("from: ").append(Arrays.toString(from)).append("\n");
		builder.append("to: ").append(Arrays.toString(to)).append("\n");
		builder.append("rotation: ").append("\n");
		builder.append(Utilities.tabBlock(rotation.toString(), 1));
		builder.append("shade: ").append(shade).append("\n");
		builder.append("color: ").append(color).append("\n");
		builder.append("faces: ").append("\n");
		for (String key: faces.keySet()) {
			builder.append("\t").append(key).append(": \n");
			builder.append(Utilities.tabBlock(faces.get(key).toString(), 2));
		}
		return builder.toString();
	}
}
