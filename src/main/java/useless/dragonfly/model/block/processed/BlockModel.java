package useless.dragonfly.model.block.processed;

import net.minecraft.core.util.helper.Side;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.registries.TextureRegistry;

import java.util.HashMap;

public class BlockModel {
	public BlockCube[] blockCubes;
	protected ModelData modelData;
	protected BlockModel parentModel;
	protected HashMap<String, String> textureMap = new HashMap<>();
	public BlockModel(ModelData modelData){
		this.modelData = modelData;

		if (modelData.parent != null && !modelData.parent.equals("block/block")){ // Has parent Model
			String namespace;
			String modelName;
			if (modelData.parent.contains(":")){
				namespace = modelData.parent.split(":")[0];
				modelName = modelData.parent.split(":")[1];
			} else {
				namespace = TextureRegistry.coreNamepaceId;
				modelName = modelData.parent;
			}
			parentModel = ModelHelper.getOrCreateBlockModel(namespace, modelName );

			textureMap.putAll(parentModel.textureMap);
		}
		textureMap.putAll(modelData.textures);

		// Use parent elements if model does not specify its own
		if (parentModel != null && modelData.elements == null){
			this.blockCubes = parentModel.blockCubes;
		} else {
			this.blockCubes = new BlockCube[modelData.elements.length];
			for (int i = 0; i < blockCubes.length; i++) {
				blockCubes[i] = new BlockCube(modelData.elements[i]);
			}
		}

		// Initialize textures
		for (String texture: textureMap.values()) {
			TextureRegistry.softRegisterTexture(texture);
		}
	}
	public String getTexture(String faceTexKey){
		String result = textureMap.get(faceTexKey.substring(1));
		if (result != null && result.contains("#")){
			return getTexture(result);
		} else if (result != null && !result.contains(":")) {
			result = TextureRegistry.coreNamepaceId + ":" + result;
		}
		return result;
	}
	public boolean getAO(){
		return modelData.ambientocclusion;
	}
}
