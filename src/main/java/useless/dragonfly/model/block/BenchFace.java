package useless.dragonfly.model.block;

import com.google.gson.annotations.SerializedName;
import net.minecraft.core.util.helper.Side;

public class BenchFace{
	@SerializedName("uv")
	public int[] uv;
	@SerializedName("texture")
	public String texture;
	protected float[] uvScaled;
	protected Side side;
	public void process(String key){
		uvScaled = new float[uv.length];
		for (int i = 0; i < uv.length; i++) {
			uvScaled[i] = (BlockBenchModel.textureSize - uv[i])/BlockBenchModel.textureSize;
		}
		this.side = BlockBenchModel.keyToSide.get(key);
	}
	public float uMin(){
		return uvScaled[2];
	}
	public float vMin(){ return uvScaled[3];}
	public float uMax(){
		return uvScaled[0];
	}
	public float vMax(){
		return uvScaled[1];
	}
	public Side getSide(){ return side;}
}
