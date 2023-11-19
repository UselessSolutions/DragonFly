package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.model.block.data.ModelData;

public class BlockFace {
	protected FaceData faceData;
	protected float[] uvScaled;
	protected Side side;
	public BlockFace(FaceData faceData){
		this.faceData = faceData;
	}
	public void process(String key){
		uvScaled = new float[faceData.uv.length];
		for (int i = 0; i < faceData.uv.length; i++) {
			uvScaled[i] = (float) (TextureFX.tileWidthTerrain - faceData.uv[i]) / TextureFX.tileWidthTerrain;
		}
		this.side = ModelData.keyToSide.get(key);
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
	public String getTexture(){
		return faceData.texture;
	}
}
