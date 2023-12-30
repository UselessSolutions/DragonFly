package useless.dragonfly.model.blockstates.data;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ModelPart {
	@SerializedName("apply")
	public VariantData apply;
	@SerializedName("when")
	public HashMap<String, Object> when = new HashMap<>();
}
