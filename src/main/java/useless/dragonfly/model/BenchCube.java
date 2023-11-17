package useless.dragonfly.model;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Side;

import java.util.HashMap;

public class BenchCube{
	@SerializedName("from")
	public int[] from;
	@SerializedName("to")
	public int[] to;
	@SerializedName("color")
	public int color;
	@SerializedName("faces")
	public HashMap<String, BenchFace> faces;

	public float[] fromScaled;
	public float[] toScaled;
	public boolean[] outerFace;
	public void process(){
		outerFace = new boolean[6];
		fromScaled = new float[from.length];
		for (int i = 0; i < from.length; i++) {
			fromScaled[i] = from[i]/BlockBenchModel.textureSize;
		}
		toScaled = new float[to.length];
		for (int i = 0; i < to.length; i++) {
			toScaled[i] = to[i]/BlockBenchModel.textureSize;
		}
		for (BenchFace face: faces.values()) {
			face.process();
		}
		outerFace[Side.TOP.getId()] = Math.abs(Float.compare(yMax(), 1f)) < 0.001f;
		outerFace[Side.BOTTOM.getId()] = Math.abs(Float.compare(yMin(), 0f)) < 0.001f;
		outerFace[Side.NORTH.getId()] = Math.abs(Float.compare(zMin(), 0f)) < 0.001f;
		outerFace[Side.SOUTH.getId()] = Math.abs(Float.compare(zMax(), 1f)) < 0.001f;
		outerFace[Side.WEST.getId()] = Math.abs(Float.compare(xMin(), 0f)) < 0.001f;
		outerFace[Side.EAST.getId()] = Math.abs(Float.compare(xMax(), 1f)) < 0.001f;
	}
	public boolean isOuterFace(Side side){
		return outerFace[side.getId()];
	}

	public BenchFace getFaceFromSide(Side side){
		String key;
		switch (side){
			case BOTTOM:
				key = "down";
				break;
			case TOP:
				key = "up";
				break;
			case NORTH:
				key = "north";
				break;
			case SOUTH:
				key = "south";
				break;
			case WEST:
				key = "west";
				break;
			case EAST:
				key = "east";
				break;
			default:
				throw new RuntimeException("Cube is missing face for side: " + side);
		}
		return faces.get(key);
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
