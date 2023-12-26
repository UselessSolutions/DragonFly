package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class PositionData {
	@SerializedName("rotation")
	public double[] rotation = new double[3];
	@SerializedName("translation")
	public double[] translation = new double[3];
	@SerializedName("scale")
	public double[] scale = new double[]{1,1,1};
	public String toString(){
		return
			"rotation: " + Arrays.toString(rotation) + "\n" +
			"translation: " + Arrays.toString(translation) + "\n" +
			"scale: " + Arrays.toString(scale) + "\n";
	}
}
