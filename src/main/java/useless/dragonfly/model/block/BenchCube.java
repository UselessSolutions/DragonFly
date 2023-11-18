package useless.dragonfly.model.block;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Axis;
import net.minecraft.core.util.helper.Side;

import java.util.Arrays;
import java.util.HashMap;

public class BenchCube{
	private static final float COMPARE_CONST = 0.001f;
	@SerializedName("from")
	public float[] from;
	@SerializedName("to")
	public float[] to;
	@SerializedName("color")
	public int color;
	@SerializedName("faces")
	public HashMap<String, BenchFace> faces;
	protected float[] fromScaled;
	protected float[] toScaled;
	protected boolean[] outerFace;
	protected boolean[] faceVisible;
	private static boolean equalFloats(float a, float b){
		return Math.abs(Float.compare(a, b)) < COMPARE_CONST;
	}
	public void process(){
		outerFace = new boolean[6];
		faceVisible = new boolean[6];
		Arrays.fill(faceVisible, true);
		fromScaled = new float[from.length];
		for (int i = 0; i < from.length; i++) {
			fromScaled[i] = from[i]/BlockBenchModel.textureSize;
		}
		toScaled = new float[to.length];
		for (int i = 0; i < to.length; i++) {
			toScaled[i] = to[i]/BlockBenchModel.textureSize;
		}
		for (String key: faces.keySet()) {
			BenchFace face = faces.get(key);
			face.process(key);
		}
		for (int i = 0; i < outerFace.length; i++) {
			outerFace[i] = equalFloats(getAxisPosition(Side.getSideById(i)), 0f) || equalFloats(getAxisPosition(Side.getSideById(i)), 1f);
		}
	}
	public void processVisibleFaces(BlockBenchModel model){
		for (BenchFace face: faces.values()) {
			if (model.textures == null) continue;
			if (model.textures.get(face.texture.replaceFirst("[#]", "")) != null){
				face.texture = model.textures.get(face.texture.replaceFirst("[#]", ""));
			}
		}
		for (BenchCube otherCube: model.elements) {
			if (this.equals(otherCube)) continue;
			for (BenchFace thisFace: faces.values()) {
				BenchFace otherFace = otherCube.getFaceFromSide(thisFace.side.getOpposite());
				if (otherFace == null) continue;
				if (!equalFloats(this.getAxisPosition(thisFace.side), otherCube.getAxisPosition(otherFace.side))) continue;
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

	public BenchFace getFaceFromSide(Side side){
		return faces.get(BlockBenchModel.sideToKey.get(side));
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
