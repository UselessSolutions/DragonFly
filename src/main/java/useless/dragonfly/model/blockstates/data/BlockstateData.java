package useless.dragonfly.model.blockstates.data;

import com.google.gson.annotations.SerializedName;
import useless.dragonfly.utilities.Utilities;

import java.util.HashMap;

public class BlockstateData {
	@SerializedName("variants")
	public HashMap<String, VariantData> variants;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (variants == null){
			builder.append("null\n");
		} else {
			for (String key: variants.keySet()) {
				builder.append(key).append("\n");
				builder.append(Utilities.tabBlock(variants.get(key).toString(),1));
			}
		}
		return builder.toString();
	}
}
