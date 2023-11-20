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
	public void process(BlockCube cube, String key){
		this.side = ModelData.keyToSide.get(key);
		generateUVs(cube);




	}
	protected void generateUVs(BlockCube cube){
		uvScaled = new float[4];
		float[] _uvs = new float[0];
		if (faceData.uv == null){
			float xDif = cube.cubeData.to[0] - cube.cubeData.from[0];
			float yDif = cube.cubeData.to[1] - cube.cubeData.from[1];
			float zDif = cube.cubeData.to[2] - cube.cubeData.from[2];
			switch (side){ // TODO replace with actual port of vanilla's uv generation
				case NORTH:
				case SOUTH:
					_uvs = new float[]{cube.cubeData.from[0], TextureFX.tileWidthTerrain - cube.cubeData.to[1], cube.cubeData.from[0] + xDif, TextureFX.tileWidthTerrain - cube.cubeData.to[1] + yDif};
					break;
				case EAST:
				case WEST:
					_uvs = new float[]{cube.cubeData.from[2], TextureFX.tileWidthTerrain - cube.cubeData.to[1], cube.cubeData.from[2] + zDif, TextureFX.tileWidthTerrain - cube.cubeData.to[1] + yDif};
					break;
				case TOP:
				case BOTTOM:
					_uvs = new float[]{cube.cubeData.from[0], TextureFX.tileWidthTerrain - cube.cubeData.to[2], cube.cubeData.from[0] + xDif, TextureFX.tileWidthTerrain - cube.cubeData.to[2] + zDif};
					break;
			}

		} else {
			_uvs = faceData.uv;
		}

		for (int i = 0; i < _uvs.length; i++) {
			if (i == 0 || i == 2){ // u
				uvScaled[i] = (_uvs[i]) / TextureFX.tileWidthTerrain;
			} else { // v
				uvScaled[i] = (TextureFX.tileWidthTerrain - _uvs[i]) / TextureFX.tileWidthTerrain;
			}

		}
	}
	public float uMin(){
		return uvScaled[0];
	}
	public float vMin(){ return uvScaled[3];}
	public float uMax(){
		return uvScaled[2];
	}
	public float vMax(){
		return uvScaled[1];
	}
	public Side getSide(){ return side;}
	public String getTexture(){
		return faceData.texture;
	}
}
