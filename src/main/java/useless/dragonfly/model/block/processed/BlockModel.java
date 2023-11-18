package useless.dragonfly.model.block.processed;

import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.ModelData;

import java.util.Arrays;

public class BlockModel extends ModelData {
	public boolean[] hasFaceToRenderOnSide;
	public boolean hasFaceToRender(Side side){
		return hasFaceToRenderOnSide[side.getId()];
	}
	public BlockModel(ModelData modelData){
		this.textures = modelData.textures;
		this.credit = modelData.credit;
		this.elements = modelData.elements;

		hasFaceToRenderOnSide = new boolean[6];
		for (BlockCube cube: elements) {
			cube.process();
			for (int i = 0; i < hasFaceToRenderOnSide.length; i++) {
				hasFaceToRenderOnSide[i] |= cube.isOuterFace(Side.getSideById(i));
			}
		}
		for (BlockCube cube: elements) {
			cube.processVisibleFaces(this);
		}
	}
	public String toString(){
		StringBuilder builder = new StringBuilder(this.credit);
		for (BlockCube cube: elements) {
			builder.append(Arrays.toString(cube.from));
			builder.append(Arrays.toString(cube.to));
			builder.append(cube.color);
			for (String key: cube.faces.keySet()) {
				builder.append(key);
				BlockFace face = cube.faces.get(key);
				builder.append(face.texture);
				builder.append(Arrays.toString(face.uv));
			}
		}
		return builder.toString();
	}
}
