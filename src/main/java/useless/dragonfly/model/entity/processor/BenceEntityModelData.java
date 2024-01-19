package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BenceEntityModelData {
	@SerializedName("format_version")
	public String formatVersion;
	@SerializedName("minecraft:geometry")
	public List<BenchEntityGeometry> benchEntityGeometry;
}
