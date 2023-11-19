package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;

public class PositionData {
	@SerializedName("rotation")
	public double[] rotation;
	@SerializedName("translation")
	public double[] translation;
	@SerializedName("scale")
	public double[] scale;
}
