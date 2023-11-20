package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.model.block.data.ModelData;

public class BlockFace {
	protected FaceData faceData;
	protected float[] uvScaled;
	protected Side side;
	public final Vector3f[] vertices;
	public final String[] vertexUVs;
	public BlockCube parentCube;
	public BlockFace(BlockCube cube, String key){
		this.faceData = cube.cubeData.faces.get(key);
		this.side = ModelData.keyToSide.get(key);
		this.parentCube = cube;
		generateUVs(cube);
		String[] vertexKeyMap = new String[4];
		switch (side){ // TODO replace this whole string key system with something better
			case NORTH:
				vertexKeyMap = new String[]{"-+-", "++-", "+--", "---"};
				vertexUVs = new String[]{"+-", "--", "-+", "++"};
				break;
			case SOUTH:
				vertexKeyMap = new String[]{"-++", "--+", "+-+", "+++"};
				vertexUVs = new String[]{"--", "-+", "++", "+-"};
				break;
			case EAST:
				vertexKeyMap = new String[]{"+-+", "+--", "++-", "+++"};
				vertexUVs = new String[]{"-+", "++", "+-", "--"};
				break;
			case WEST:
				vertexKeyMap = new String[]{"-++", "-+-", "---", "--+"};
				vertexUVs = new String[]{"+-", "--", "-+", "++"};
				break;
			case TOP:
				vertexKeyMap = new String[]{"+++", "++-", "-+-", "-++"};
				vertexUVs = new String[]{"++", "+-", "--", "-+"};
				break;
			case BOTTOM:
				vertexKeyMap = new String[]{"--+", "---", "+--", "+-+"};
				vertexUVs = new String[]{"-+", "--", "+-", "++"};
				break;
			default:
				vertexUVs = null;
		}
		vertices = new Vector3f[]{parentCube.vertices.get(vertexKeyMap[0]), parentCube.vertices.get(vertexKeyMap[1]), parentCube.vertices.get(vertexKeyMap[2]), parentCube.vertices.get(vertexKeyMap[3])};
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
	public float vMin(){ return uvScaled[1];}
	public float uMax(){
		return uvScaled[2];
	}
	public float vMax(){
		return uvScaled[3];
	}
	public Side getSide(){ return side;}
	public String getTexture(){
		return faceData.texture;
	}
	public double[] getVertexUV(double uMin, double vMin, double uMax, double vMax, int point){
		String uvKey = vertexUVs[point];
		double u = uvKey.charAt(0) == '-' ? uMin : uMax;
		double v = uvKey.charAt(1) == '-' ? vMin : vMax;
		return new double[]{u, v};
	}
}
