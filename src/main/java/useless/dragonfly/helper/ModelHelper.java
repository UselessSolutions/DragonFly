package useless.dragonfly.helper;

import com.google.gson.stream.JsonReader;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.BenchCube;
import useless.dragonfly.model.BlockBenchModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
	public static final Map<String, BlockBenchModel> registeredModels = new HashMap<>();

	/**
	 * Place mod textures in the <i>assets/modid/item/</i> directory for them to be seen.
	 */
	public static BlockBenchModel getOrCreateModel(String modId, String modelSource) {
		String modelKey = ModelHelper.getModelLocation(modId, modelSource);
		if (registeredModels.containsKey(modelKey)){
			return registeredModels.get(modelKey);
		}
		InputStream inputStream = BlockBenchModel.class.getResourceAsStream(modelKey);
		JsonReader reader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)));
		BlockBenchModel model = DragonFly.GSON.fromJson(reader, BlockBenchModel.class);
		model.hasFaceToRenderOnSide = new boolean[6];
		for (BenchCube cube: model.elements) {
			cube.process();
			for (int i = 0; i < model.hasFaceToRenderOnSide.length; i++) {
				model.hasFaceToRenderOnSide[i] |= cube.isOuterFace(Side.getSideById(i));
			}
		}
		for (BenchCube cube: model.elements) {
			cube.processVisibleFaces(model);
		}
		registeredModels.put(modelKey, model);
		return model;
	}
	public static String getModelLocation(String modID, String modelSource){
		return "/assets/" + modID + "/model/" + modelSource;
	}
}
