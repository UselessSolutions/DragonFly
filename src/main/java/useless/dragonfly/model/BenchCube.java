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
	public void process(){
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
