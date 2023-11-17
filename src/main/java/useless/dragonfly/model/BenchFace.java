package useless.dragonfly.model;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Side;

public class BenchFace{
	@SerializedName("uv")
	public int[] uv;
	@SerializedName("texture")
	public String texture;
	public float[] uvScaled;
	public Side side;
	public void process(String key){
		uvScaled = new float[uv.length];
		for (int i = 0; i < uv.length; i++) {
			uvScaled[i] = uv[i]/BlockBenchModel.textureSize;
		}
		this.side = BlockBenchModel.keyToSide.get(key);
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
