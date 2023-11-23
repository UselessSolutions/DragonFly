package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Side;
import org.lwjgl.util.vector.Vector3f;
import useless.dragonfly.model.block.data.CubeData;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.utilities.Utilities;

import java.util.HashMap;

public class BlockCube {
	protected CubeData cubeData;
	protected float[] fromScaled;
	protected float[] toScaled;
	protected boolean[] outerFace;
	public final BlockModel parentModel;
	public HashMap<String, Vector3f> vertices = new HashMap<>();
	public HashMap<String, BlockFace> faces = new HashMap<>();
	public BlockCube(BlockModel model, CubeData cubeData){
		this.parentModel = model;
		this.cubeData = cubeData;
		outerFace = new boolean[6];
		fromScaled = new float[cubeData.from.length];
		for (int i = 0; i < cubeData.from.length; i++) {
			fromScaled[i] = cubeData.from[i]/ TextureFX.tileWidthTerrain;
		}
		toScaled = new float[cubeData.to.length];
		for (int i = 0; i < cubeData.to.length; i++) {
			toScaled[i] = cubeData.to[i]/TextureFX.tileWidthTerrain;
		}
		vertices.put("+++", new Vector3f(xMax(), yMax(), zMax()));
		vertices.put("++-", new Vector3f(xMax(), yMax(), zMin()));
		vertices.put("+-+", new Vector3f(xMax(), yMin(), zMax()));
		vertices.put("+--", new Vector3f(xMax(), yMin(), zMin()));
		vertices.put("-++", new Vector3f(xMin(), yMax(), zMax()));
		vertices.put("-+-", new Vector3f(xMin(), yMax(), zMin()));
		vertices.put("--+", new Vector3f(xMin(), yMin(), zMax()));
		vertices.put("---", new Vector3f(xMin(), yMin(), zMin()));

		if (cubeData.rotation != null){
			vertices.replaceAll((k, v) -> Utilities.rotatePoint(
				vertices.get(k),
				new Vector3f(cubeData.rotation.origin[0]/TextureFX.tileWidthTerrain, cubeData.rotation.origin[1]/TextureFX.tileWidthTerrain, cubeData.rotation.origin[2]/TextureFX.tileWidthTerrain),
				cubeData.rotation.axis,
				cubeData.rotation.angle));
		}

		for (String key: cubeData.faces.keySet()) {
			faces.put(key, new BlockFace(this, key));
		}
		for (int i = 0; i < outerFace.length; i++) {
			outerFace[i] = Utilities.equalFloats(getAxisPosition(Side.getSideById(i)), 0f) || Utilities.equalFloats(getAxisPosition(Side.getSideById(i)), 1f);
		}


	}
	public float getAxisPosition(Side side){
		switch (side){
			case TOP:
				return yMax();
			case BOTTOM:
				return yMin();
			case NORTH:
				return zMin();
			case SOUTH:
				return zMax();
			case WEST:
				return xMin();
			case EAST:
				return xMax();
		}
		throw new RuntimeException("Specified side does not exist on a cube!!!");
	}
	public boolean isOuterFace(Side side){
		return outerFace[side.getId()];
	}

	public BlockFace getFaceFromSide(Side side){
		return faces.get(ModelData.sideToKey.get(side));
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
