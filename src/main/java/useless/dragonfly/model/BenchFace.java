package useless.dragonfly.model;

import com.google.gson.annotations.SerializedName;

public class BenchFace{
	@SerializedName("uv")
	public int[] uv;
	@SerializedName("texture")
	public String texture;
	public float[] uvScaled;
	public void process(){
		uvScaled = new float[uv.length];
		for (int i = 0; i < uv.length; i++) {
			uvScaled[i] = uv[i]/BlockBenchModel.textureSize;
		}
	}
	public float uMin(){
		return uvScaled[0];
	}
	public float vMin(){
		return uvScaled[1];
	}
	public float uMax(){
		return uvScaled[2];
	}
	public float vMax(){
		return uvScaled[3];
	}
}
