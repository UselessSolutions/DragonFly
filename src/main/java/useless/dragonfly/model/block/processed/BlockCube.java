package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.DragonFly;
import useless.dragonfly.model.block.data.CubeData;
import useless.dragonfly.model.block.data.ModelData;

import java.util.Arrays;
import java.util.HashMap;

public class BlockCube {
	protected CubeData cubeData;
	protected float[] fromScaled;
	protected float[] toScaled;
	protected boolean[] outerFace;
	protected boolean[] faceVisible;
	public HashMap<String, BlockFace> faces = new HashMap<>();

	public BlockCube(CubeData cubeData){
		this.cubeData = cubeData;
	}
	public void process(){
		outerFace = new boolean[6];
		faceVisible = new boolean[6];
		Arrays.fill(faceVisible, true);
		fromScaled = new float[cubeData.from.length];
		for (int i = 0; i < cubeData.from.length; i++) {
			fromScaled[i] = cubeData.from[i]/ TextureFX.tileWidthTerrain;
		}
		toScaled = new float[cubeData.to.length];
		for (int i = 0; i < cubeData.to.length; i++) {
			toScaled[i] = cubeData.to[i]/TextureFX.tileWidthTerrain;
		}
		for (String key: cubeData.faces.keySet()) {
			BlockFace face = new BlockFace(cubeData.faces.get(key));
			face.process(key);
			faces.put(key, face);
		}
		for (int i = 0; i < outerFace.length; i++) {
			outerFace[i] = DragonFly.equalFloats(getAxisPosition(Side.getSideById(i)), 0f) || DragonFly.equalFloats(getAxisPosition(Side.getSideById(i)), 1f);
		}
	}
	public void processVisibleFaces(BlockModel model){
		for (BlockCube otherCube: model.blockCubes) {
			if (this.equals(otherCube)) continue;
			for (BlockFace thisFace: faces.values()) {
				BlockFace otherFace = otherCube.getFaceFromSide(thisFace.side.getOpposite());
				if (otherFace == null) continue;
				if (!DragonFly.equalFloats(this.getAxisPosition(thisFace.side), otherCube.getAxisPosition(otherFace.side))) continue;
				float[] thisFaceDim = this.faceDimensions(thisFace.side);
				float[] otherFaceDim = otherCube.faceDimensions(otherFace.side);
				faceVisible[thisFace.side.getId()] &= face1Visible(thisFaceDim, otherFaceDim);
			}
		}
	}
	public boolean face1Visible(float[] face1, float[] face2){
		if (face1[0] < face2[0] || face1[1] < face2[1]){
			return true;
		}
		return face1[2] > face2[2] || face1[3] > face2[3];
	}
	public float[] faceDimensions(Side side){
		if (side.getAxis() == Axis.Y){
			return new float[]{xMin(), zMin(), xMax(), zMax()};
		}
		if (side.getAxis() == Axis.X){
			return new float[]{yMin(), zMin(), yMax(), zMax()};
		}
		return new float[]{xMin(), yMin(), xMax(), yMax()};
	}
	public float getAxisPosition(Side side){
		switch (side){
			case TOP:
				return yMax();
			case BOTTOM:
				return yMin();
			case NORTH:
				return zMin();
			case SOUTH:
				return zMax();
			case WEST:
				return xMin();
			case EAST:
				return xMax();
		}
		throw new RuntimeException("Specified side does not exist on a cube!!!");
	}
	public boolean isOuterFace(Side side){
		return outerFace[side.getId()];
	}
	public boolean isFaceVisible(Side side) { return faceVisible[side.getId()];}

	public BlockFace getFaceFromSide(Side side){
		return faces.get(ModelData.sideToKey.get(side));
	}
	public float xMin(){
		return fromScaled[0];
	}
	public float yMin(){
		return fromScaled[1];
	}
	public float zMin(){
		return fromScaled[2];
	}
	public float xMax(){
		return toScaled[0];
	}
	public float yMax(){
		return toScaled[1];
	}
	public float zMax(){
		return toScaled[2];
	}

}
