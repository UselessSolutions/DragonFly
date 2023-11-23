package useless.dragonfly.model.blockstates.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class BlockstateData {
	@SerializedName("variants")
	public HashMap<String, VariantData> variants;
}
