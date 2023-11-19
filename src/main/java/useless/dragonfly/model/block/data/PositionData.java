package useless.dragonfly.model.block.data;

import com.google.gson.annotations.SerializedName;
import useless.dragonfly.DragonFly;

public class PositionData {
	@SerializedName("rotation")
	public double[] rotation;
	@SerializedName("translation")
	public double[] translation;
	@SerializedName("scale")
	public double[] scale;
	public String toString(){
		return DragonFly.writeFields(PositionData.class);
	}
}
