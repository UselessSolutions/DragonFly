package useless.dragonfly.model.block.processed;

import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.model.block.data.PositionData;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.utilities.NamespaceId;

import javax.annotation.Nonnull;
import java.util.HashMap;

public class BlockModel {
	private static HashMap<String, PositionData> defaultDisplays = new HashMap<>();
	static {
		PositionData gui = new PositionData();
		gui.rotation = new double[]{30, 225, 0};
		gui.translation = new double[] {0, 0, 0};
		gui.scale = new double[]{0.625, 0.625, 0.625};

		PositionData ground = new PositionData();
		ground.rotation = new double[]{0, 0, 0};
		ground.translation = new double[] {0, 3, 0};
		ground.scale = new double[]{0.25, 0.25, 0.25};

		PositionData right_3rd = new PositionData();
		right_3rd.rotation = new double[]{75, 45, 0};
		right_3rd.translation = new double[] {0, 2.5, 0};
		right_3rd.scale = new double[]{0.375, 0.375, 0.375};

		PositionData right_first = new PositionData();
		right_first.rotation = new double[]{0, 45, 0};
		right_first.translation = new double[] {0, 0, 0};
		right_first.scale = new double[]{0.4, 0.4, 0.4};

		defaultDisplays.put("gui", gui);
		defaultDisplays.put("ground", ground);
		defaultDisplays.put("thirdperson_righthand", right_3rd);
		defaultDisplays.put("firstperson_righthand", right_first);
	}
	public BlockCube[] blockCubes = new BlockCube[0];
	protected ModelData modelData;
	protected BlockModel parentModel;
	public HashMap<String, String> textureMap = new HashMap<>();
	public HashMap<String, PositionData> display = new HashMap<>();
	public BlockModel(ModelData modelData){
		this.modelData = modelData;

		if (modelData.parent != null){ // Has parent Model
			String namespace;
			String modelName;
			if (modelData.parent.contains(":")){
				namespace = modelData.parent.split(":")[0];
				modelName = modelData.parent.split(":")[1];
			} else {
				namespace = NamespaceId.coreNamespaceId;
				modelName = modelData.parent;
			}
			parentModel = ModelHelper.getOrCreateBlockModel(namespace, modelName );

			textureMap.putAll(parentModel.textureMap);
			display.putAll(parentModel.display);
		}
		textureMap.putAll(modelData.textures);
		display.putAll(modelData.display);


		// Initialize textures
		for (String texture: textureMap.values()) {
			if (texture == null) continue;
			TextureRegistry.softRegisterTexture(texture);
		}

		// Use parent elements if model does not specify its own
		if (parentModel != null && modelData.elements == null){
			this.blockCubes = new BlockCube[parentModel.blockCubes.length];
			for (int i = 0; i < blockCubes.length; i++) {
				blockCubes[i] = new BlockCube(this, parentModel.blockCubes[i].cubeData);
			}
		} else if (modelData.elements != null) {
			this.blockCubes = new BlockCube[modelData.elements.length];
			for (int i = 0; i < blockCubes.length; i++) {
				blockCubes[i] = new BlockCube(this, modelData.elements[i]);
			}
		}

	}
	public NamespaceId getTexture(String textureKey){
		String result;
		if (textureKey.contains("#")){
			result = textureMap.get(textureKey.substring(1));
		} else {
			result =textureMap.get(textureKey);
		}

		if (result == null || result.equals(textureKey)) return TextureRegistry.getNamespaceId(0,0);
		if (result.contains("#")){
			return getTexture(result);
		} else if (!result.contains(":")) {
			result = NamespaceId.coreNamespaceId + ":" + result;
		}
		return NamespaceId.idFromString(result);
	}
	public boolean getAO(){
		return modelData.ambientocclusion;
	}
	@Nonnull
	public PositionData getDisplayPosition(String key){
		if (display.containsKey(key)){
			return display.get(key);
		}
		display.put(key, defaultDisplays.getOrDefault(key, new PositionData()));
		return display.get(key);
	}
}
