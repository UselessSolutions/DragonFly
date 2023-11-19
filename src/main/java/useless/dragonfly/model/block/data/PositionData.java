package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class PositionData {
	@SerializedName("rotation")
	public double[] rotation;
	@SerializedName("translation")
	public double[] translation;
	@SerializedName("scale")
	public double[] scale;
	public String toString(){
		return
			"rotation: " + Arrays.toString(rotation) + "\n" +
			"translation: " + Arrays.toString(translation) + "\n" +
			"scale: " + Arrays.toString(scale) + "\n";
	}
}
