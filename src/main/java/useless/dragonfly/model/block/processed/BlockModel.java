package useless.dragonfly.model.block.processed;

import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.ModelData;

public class BlockModel {
	public boolean[] hasFaceToRenderOnSide;
	public BlockCube[] blockCubes;
	protected ModelData modelData;
	public boolean hasFaceToRender(Side side){
		return hasFaceToRenderOnSide[side.getId()];
	}
	public BlockModel(ModelData modelData){
		this.modelData = modelData;
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
	public String getTexture(String faceTexKey){
		return modelData.textures.get(faceTexKey.substring(1));
	}
	public boolean getAO(){
		return modelData.ambientocclusion;
	}
}
