package useless.dragonfly.model.block.processed;

import net.minecraft.client.render.TextureFX;
import net.minecraft.core.util.helper.Side;
import useless.dragonfly.model.block.data.CubeData;
import useless.dragonfly.model.block.data.ModelData;
import useless.dragonfly.utilities.Utilities;
import useless.dragonfly.utilities.vector.Vector3f;

import java.util.HashMap;

public class BlockCube {
	protected CubeData cubeData;
	protected Vector3f fromScaled;
	protected Vector3f toScaled;
	protected boolean[] outerFace;
	public final BlockModel parentModel;
	protected HashMap<String, Vector3f> vertices = new HashMap<>();
	protected HashMap<String, BlockFace> faces = new HashMap<>();
	public BlockCube(BlockModel model, CubeData cubeData){
		this.parentModel = model;
		this.cubeData = cubeData;
		outerFace = new boolean[6];

		fromScaled = new Vector3f(cubeData.from[0] / 16f, cubeData.from[1] / 16f, cubeData.from[2] / 16f);
		toScaled = new Vector3f(cubeData.to[0] / 16f, cubeData.to[1] / 16f, cubeData.to[2] / 16f);

		if (cubeData.rotation != null && cubeData.rotation.rescale){
			float angle = cubeData.rotation.angle;
			String axis = cubeData.rotation.axis;
			final float modRange = 90;
			float x = Math.abs(((angle - modRange/2) % modRange) - modRange/2);
			float slope = (float) Math.tan(Math.toRadians(x));
			float scalar = (float) (Math.sqrt(Math.pow((-0.5 * slope + 0.5), 2) + Math.pow((0.5 * slope + 0.5), 2)) * Math.sqrt(2));
			float xf = fromScaled.getX();
			float xt = toScaled.getX();
			float yf = fromScaled.getY();
			float yt = toScaled.getY();
			float zf = fromScaled.getZ();
			float zt = toScaled.getZ();
			Vector3f fromOrigin = (Vector3f) Vector3f.sub(fromScaled, Vector3f.origin, null).scale(scalar);
			Vector3f toOrigin = (Vector3f) Vector3f.sub(toScaled, Vector3f.origin, null).scale(scalar);
			Vector3f.add(fromOrigin, Vector3f.origin, fromOrigin);
			Vector3f.add(toOrigin, Vector3f.origin, toOrigin);
			fromScaled = fromOrigin;
			toScaled = toOrigin;
			switch (axis){
				case "x":
					fromScaled.setX(xf);
					toScaled.setX(xt);
					break;
				case "y":
					fromScaled.setY(yf);
					toScaled.setY(yt);
					break;
				case "z":
					fromScaled.setZ(zf);
					toScaled.setZ(zt);
					break;
			}
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
	public boolean isOuterFace(Side side, int rotationX, int rotationY){
		return outerFace[rotateSide(side, rotationX, rotationY).getId()];
	}
	public Side rotateSide(Side side, int rotationX, int rotationY){
		Side keySide = side;
		Side[] yRot = new Side[]{Side.EAST, Side.SOUTH, Side.WEST, Side.NORTH};
		Side[] xRot = new Side[]{Side.TOP, Side.NORTH, Side.BOTTOM, Side.SOUTH};

		int indexY = -1;
		for (int i = 0; i < yRot.length; i++) {
			if (keySide == yRot[i]){
				indexY = i;
			}
		}
		if (indexY != -1){
			keySide = yRot[(indexY + rotationY/90) % yRot.length];
		}

		int indexX = -1;
		for (int i = 0; i < xRot.length; i++) {
			if (keySide == xRot[i]){
				indexX = i;
			}
		}
		if (indexX != -1){
			keySide = xRot[(indexX + (rotationX)/90) % xRot.length];
		}
		return keySide;
	}

	public BlockFace getFaceFromSide(Side side, int rotationX, int rotationY){
		return faces.get(ModelData.sideToKey.get(rotateSide(side, rotationX, rotationY)));
	}
	public HashMap<String, BlockFace> getFaces(){
		return faces;
	}
	public float xMin(){
		return fromScaled.getX();
	}
	public float yMin(){
		return fromScaled.getY();
	}
	public float zMin(){
		return fromScaled.getZ();
	}
	public float xMax(){
		return toScaled.getX();
	}
	public float yMax(){
		return toScaled.getY();
	}
	public float zMax(){
		return toScaled.getZ();
	}
	public Vector3f getMin(){
		return fromScaled;
	}
	public Vector3f getMax(){
		return toScaled;
	}
	public boolean shade() {return cubeData.shade;}

}
