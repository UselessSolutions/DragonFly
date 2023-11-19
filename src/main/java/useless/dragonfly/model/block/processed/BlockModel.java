package useless.dragonfly.model.block.processed;

import net.minecraft.core.util.helper.Side;
import useless.dragonfly.helper.ModelHelper;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.registries.TextureRegistry;

public class BlockModel {
	public boolean[] hasFaceToRenderOnSide;
	public BlockCube[] blockCubes;
	protected ModelData modelData;
	protected ModelData parentModel;
	public boolean hasFaceToRender(Side side){
		return hasFaceToRenderOnSide[side.getId()];
	}
	public BlockModel(ModelData modelData){
		this.modelData = modelData;
		ModelHelper.loadBlockModel(modelData.parent);
		initializeTextures();

		this.blockCubes = new BlockCube[modelData.elements.length];
		for (int i = 0; i < blockCubes.length; i++) {
			blockCubes[i] = new BlockCube(modelData.elements[i]);
		}


		hasFaceToRenderOnSide = new boolean[6];
		for (BlockCube cube: blockCubes) {
			cube.process();
			for (int i = 0; i < hasFaceToRenderOnSide.length; i++) {
				hasFaceToRenderOnSide[i] |= cube.isOuterFace(Side.getSideById(i));
			}
		}
		for (BlockCube cube: blockCubes) {
			cube.processVisibleFaces(this);
		}
	}
	protected void initializeTextures(){
		for (String texture: modelData.textures.values()) {
			if (TextureRegistry.containsTexture(texture)) continue;
			String[] nameSpaceSplit = texture.split(":");
			if (nameSpaceSplit[0].equals(TextureRegistry.coreNamepaceId) || nameSpaceSplit.length != 2) continue;

			String[] dirSplit = nameSpaceSplit[1].split("/");
			if (dirSplit[0].equals("block")){
				TextureRegistry.registerModBlockTexture(nameSpaceSplit[0], nameSpaceSplit[1].replace("block/", ""));
			}
			if (dirSplit[0].equals("item")){
				TextureRegistry.registerModItemTexture(nameSpaceSplit[0], nameSpaceSplit[1].replace("block/", ""));
			}
		}
	}
	public String getTexture(String faceTexKey){
		return modelData.textures.get(faceTexKey.substring(1));
	}
	public boolean getAO(){
		return modelData.ambientocclusion;
	}
}
