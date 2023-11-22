package useless.dragonfly.model.blockstates.data;

import com.google.gson.annotations.SerializedName;

public class VariantData {
	@SerializedName("")
	public VariantModelData[] models;

	@SerializedName("model")
	public String model;
	@SerializedName("x")
	public int x;
	@SerializedName("y")
	public int y;
	@SerializedName("uvlock")
	public boolean uvlock = false;
}
