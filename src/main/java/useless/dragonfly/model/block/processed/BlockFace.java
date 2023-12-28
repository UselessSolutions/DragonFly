package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.Global;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.FaceData;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.registries.TextureRegistry;
import useless.dragonfly.utilities.vector.Vector3f;

import static useless.dragonfly.DragonFly.terrainAtlasWidth;

public class BlockFace {
	protected FaceData faceData;
	protected double[] uvScaled;
	protected Side side;
	protected Side cullFace = null;
	public final Vector3f[] vertices;
	protected final String[] vertexUVMap;
	protected String[] vertexKeyMap = new String[4];
	protected double[][] vertexUVs;
	public BlockCube parentCube;
	public BlockFace(BlockCube cube, String key){
		this.faceData = cube.cubeData.faces.get(key);
		this.side = ModelData.keyToSide.get(key);
		if (faceData.cullface != null){
			this.cullFace = ModelData.keyToSide.get(faceData.cullface);
		}
		this.parentCube = cube;
		generateUVs(cube);
		switch (side){ // TODO replace this whole string key system with something better
			case NORTH:
				vertexKeyMap = new String[]{"-+-", "++-", "+--", "---"};
				vertexUVMap = new String[]{"+-", "--", "-+", "++"};
				break;
			case SOUTH:
				vertexKeyMap = new String[]{"-++", "--+", "+-+", "+++"};
				vertexUVMap = new String[]{"--", "-+", "++", "+-"};
				break;
			case EAST:
				vertexKeyMap = new String[]{"+-+", "+--", "++-", "+++"};
				vertexUVMap = new String[]{"-+", "++", "+-", "--"};
				break;
			case WEST:
				vertexKeyMap = new String[]{"-++", "-+-", "---", "--+"};
				vertexUVMap = new String[]{"+-", "--", "-+", "++"};
				break;
			case TOP:
				vertexKeyMap = new String[]{"+++", "++-", "-+-", "-++"};
				vertexUVMap = new String[]{"++", "+-", "--", "-+"};
				break;
			case BOTTOM:
				vertexKeyMap = new String[]{"--+", "---", "+--", "+-+"};
				vertexUVMap = new String[]{ "--", "-+", "++", "+-"};
				break;
			default:
				vertexUVMap = null;
		}
		System.out.println(getTexture());
		System.out.println(parentCube.parentModel.getTexture(getTexture()));
		int texture = TextureRegistry.getIndexOrDefault(parentCube.parentModel.getTexture(getTexture()), 0);
		vertices = new Vector3f[]{parentCube.vertices.get(vertexKeyMap[0]), parentCube.vertices.get(vertexKeyMap[1]), parentCube.vertices.get(vertexKeyMap[2]), parentCube.vertices.get(vertexKeyMap[3])};
		vertexUVs = new double[][]{generateVertexUV(texture, 0), generateVertexUV(texture, 1), generateVertexUV(texture, 2), generateVertexUV(texture, 3)};
	}
	protected void generateUVs(BlockCube cube){
		uvScaled = new double[4];
		double[] _uvs = new double[0];
		if (faceData.uv == null){
			float xDif = cube.cubeData.to[0] - cube.cubeData.from[0];
			float yDif = cube.cubeData.to[1] - cube.cubeData.from[1];
			float zDif = cube.cubeData.to[2] - cube.cubeData.from[2];
			switch (side){ // TODO replace with actual port of vanilla's uv generation
				case NORTH:
				case SOUTH:
					_uvs = new double[]{cube.cubeData.from[0], TextureFX.tileWidthTerrain - cube.cubeData.to[1], cube.cubeData.from[0] + xDif, TextureFX.tileWidthTerrain - cube.cubeData.to[1] + yDif};
					break;
				case EAST:
				case WEST:
					_uvs = new double[]{cube.cubeData.from[2], TextureFX.tileWidthTerrain - cube.cubeData.to[1], cube.cubeData.from[2] + zDif, TextureFX.tileWidthTerrain - cube.cubeData.to[1] + yDif};
					break;
				case TOP:
				case BOTTOM:
					_uvs = new double[]{cube.cubeData.from[0], TextureFX.tileWidthTerrain - cube.cubeData.to[2], cube.cubeData.from[0] + xDif, TextureFX.tileWidthTerrain - cube.cubeData.to[2] + zDif};
					break;
			}

		} else {
			_uvs = faceData.uv;
		}

		for (int i = 0; i < _uvs.length; i++) {
			if (i == 0 || i == 2){ // u
				uvScaled[i] = _uvs[i] / TextureFX.tileWidthTerrain;
			} else { // v
				uvScaled[i] = (TextureFX.tileWidthTerrain - _uvs[i]) / TextureFX.tileWidthTerrain;
			}
		}
	}
	public double uMin(){
		return uvScaled[0];
	}
	public double vMin(){ return uvScaled[1];}
	public double uMax(){
		return uvScaled[2];
	}
	public double vMax(){
		return uvScaled[3];
	}
	public Side getSide(){ return side;}
	public String getTexture(){
		return faceData.texture;
	}
	public double[] generateVertexUV(int texture, int point){
		int texX = texture % Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
		int texY = texture / Global.TEXTURE_ATLAS_WIDTH_TILES * TextureFX.tileWidthTerrain;
		double atlasUMin = (texX + uMin() * TextureFX.tileWidthTerrain) / terrainAtlasWidth;
		double atlasUMax = (texX + uMax() * TextureFX.tileWidthTerrain) / terrainAtlasWidth;
		double atlasVMin = (texY + (1 - vMin()) * TextureFX.tileWidthTerrain) / terrainAtlasWidth;
		double atlasVMax = (texY + (1 - vMax()) * TextureFX.tileWidthTerrain) / terrainAtlasWidth;
//		if (uMin() < 0.0 || uMax() > 1.0) { // Cap U value
//			atlasUMin = texX / terrainAtlasWidth;
//			atlasUMax = (texX + (TextureFX.tileWidthTerrain - 0.01f)) / terrainAtlasWidth;
//		}
//		if (vMin() < 0.0 || vMax() > 1.0) { // Cap V value
//			atlasVMin = texY / terrainAtlasWidth;
//			atlasVMax = (texY + (TextureFX.tileWidthTerrain - 0.01f)) / terrainAtlasWidth;
//		}

		String uvKey = vertexUVMap[point];
		double u = uvKey.charAt(0) == '-' ? atlasUMin : atlasUMax;
		double v = uvKey.charAt(1) == '-' ? atlasVMin : atlasVMax;
		return new double[]{u, v};
	}
	public boolean cullFace(int x, int y, int z, boolean renderOuterSide){
		return !renderOuterSide && side == cullFace;
	}
	public boolean useTint(){
		return faceData.tintindex >= 0;
	}
	public double[][] getVertexUVs(){
		return vertexUVs;
	}
	public double[] getVertexUV(int index){
		int val = index + faceData.rotation/90;
		if (val < 0) val += 4;
		return vertexUVs[val%4];
	}
	public boolean getFullBright(){
		return faceData.fullbright;
	}
}
