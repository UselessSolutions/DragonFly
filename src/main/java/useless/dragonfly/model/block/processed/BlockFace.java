package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.FaceData;

public class BlockFace extends FaceData {
	protected float[] uvScaled;
	protected Side side;
	public void process(String key){
		uvScaled = new float[uv.length];
		for (int i = 0; i < uv.length; i++) {
			uvScaled[i] = (float) (TextureFX.tileWidthTerrain - uv[i]) / TextureFX.tileWidthTerrain;
		}
		this.side = BlockModel.keyToSide.get(key);
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
