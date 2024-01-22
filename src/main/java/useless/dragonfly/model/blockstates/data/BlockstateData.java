package useless.dragonfly.model.blockstates.data;

import useless.dragonfly.utilities.Utilities;

import java.util.Map;

public class BlockstateData {
	public Map<String, ModelPart> variants;
	public ModelPart[] multipart;
	public BlockstateData(Map<String, ModelPart> variants){
		this.variants = variants;
	}
	public BlockstateData(ModelPart[] multipart){
		this.multipart = multipart;
	}
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
