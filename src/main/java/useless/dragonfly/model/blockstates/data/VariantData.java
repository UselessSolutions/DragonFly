package useless.dragonfly.model.blockstates.data;

import com.google.gson.annotations.SerializedName;

public class VariantData {
	@SerializedName("")
	public VariantData[] variants;

	@SerializedName("model")
	public String model;
	@SerializedName("x")
	public int x;
	@SerializedName("y")
	public int y;
	@SerializedName("uvlock")
	public boolean uvlock = false;
	@SerializedName("weight")
	public int weight = 1;
	public double weightAccum = 0;
	@Override
	public String toString() {
		String builder =
			"model: " + model + "\n" +
			"x: " + x + "\n" +
			"y: " + y + "\n" +
			"uvlock: " + uvlock + "\n";
		return builder;
	}
}
