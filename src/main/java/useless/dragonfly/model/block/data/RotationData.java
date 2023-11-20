package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class RotationData {
	@SerializedName("origin")
	public float[] origin = new float[3];
	@SerializedName("axis")
	public String axis = null;
	@SerializedName("angle")
	public float angle = 0;
	@SerializedName("rescale")
	public boolean rescale = false;

	public String toString(){
		return
			"origin: " + Arrays.toString(origin) + "\n" +
			"axis: " + axis + "\n" +
			"angle: " + angle + "\n" +
			"rescale: " + rescale;
	}
}
