package useless.dragonfly.model.entity.processor;

import com.google.common.collect.Lists;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.Vertex;
import net.minecraft.core.util.helper.Direction;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import useless.dragonfly.utilities.Utilities;
import useless.dragonfly.utilities.vector.Vector3f;

import java.util.List;

public class BenchEntityCube {

	private static final float COMPARE_CONST = 0.001f;
	private Vertex[] corners;
	private List<Polygon> polygons;
	private Boolean mirror;
	private final float inflate;
	private final Vector3f size;
	private final Vector3f origin;
	private final List<Float> uv;
	@Nullable
	private final BenchFaceUVsItem faceUv;
	private final Vector3f rotation;
	private final Vector3f pivot;
	public BenchEntityCube(Vector3f origin, Vector3f pivot, Vector3f rotation, Vector3f size, float inflate, List<Float> uv, BenchFaceUVsItem faceUv, Boolean mirrored){
        this.origin = origin;
        this.pivot = pivot;
        this.rotation = rotation;
        this.size = size;
        this.inflate = inflate;
        this.uv = uv;
        this.faceUv = faceUv;
		this.mirror = mirrored;
    }

	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled = false;
	private int displayList = 0;

	public boolean isMirror() {
		return mirror != null && mirror;
	}

	public boolean isHasMirror() {
		return mirror != null;
	}

	public void setMirror(boolean mirror) {
		this.mirror = mirror;
	}

	public float getInflate() {
		return inflate;
	}

	public Vector3f getSize() {
		return size;
	}

	public Vector3f getOrigin() {
		return origin;
	}

	@Nullable
	public Vector3f getRotation() {
		return rotation;
	}

	@Nullable
	public Vector3f getPivot() {
		return pivot;
	}

	public List<Float> getUv() {
		return uv;
	}

	@Nullable
	public BenchFaceUVsItem getFaceUv() {
		return faceUv;
	}

	public void addBox(int texWidth, int texHeight, float x, float y, float z, boolean flipBottomUV) {
		if (faceUv != null) {
			this.corners = new Vertex[8];
			this.polygons = Lists.newArrayList();

			float minX = x;
			float minY = y;
			float minZ = z;
			float maxX = size.x + x;
			float maxY = size.y + y;
			float maxZ = size.z + z;
			minX -= inflate;
			minY -= inflate;
			minZ -= inflate;
			maxX += inflate;
			maxY += inflate;
			maxZ += inflate;

			if (this.isMirror()) {
				float temp = maxX;
				maxX = minX;
				minX = temp;
			}
			Vertex ptvMinXMinYMinZ = new Vertex(minX, minY, minZ, 0.0f, 0.0f);
			Vertex ptvMaxXMinYMinZ = new Vertex(maxX, minY, minZ, 0.0f, 8.0f);
			Vertex ptvMaxXMaxYMinZ = new Vertex(maxX, maxY, minZ, 8.0f, 8.0f);
			Vertex ptvMinXMaxYMinZ = new Vertex(minX, maxY, minZ, 8.0f, 0.0f);
			Vertex ptvMinXMinYMaxZ = new Vertex(minX, minY, maxZ, 0.0f, 0.0f);
			Vertex ptvMaxXMinYMaxZ = new Vertex(maxX, minY, maxZ, 0.0f, 8.0f);
			Vertex ptvMaxXMaxYMaxZ = new Vertex(maxX, maxY, maxZ, 8.0f, 8.0f);
			Vertex ptvMinXMaxYMaxZ = new Vertex(minX, maxY, maxZ, 8.0f, 0.0f);
			this.corners[0] = ptvMinXMinYMinZ;
			this.corners[1] = ptvMaxXMinYMinZ;
			this.corners[2] = ptvMaxXMaxYMinZ;
			this.corners[3] = ptvMinXMaxYMinZ;
			this.corners[4] = ptvMinXMinYMaxZ;
			this.corners[5] = ptvMaxXMinYMaxZ;
			this.corners[6] = ptvMaxXMaxYMaxZ;
			this.corners[7] = ptvMinXMaxYMaxZ;
			BenchEntityFace benchFace = faceUv.getFace(Direction.EAST);
			double texU = benchFace.getUv()[0];
			double texV = benchFace.getUv()[1];
			double uSize = benchFace.getUvSize()[0];
			double vSize = benchFace.getUvSize()[1];
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMaxXMinYMinZ, ptvMaxXMaxYMinZ, ptvMaxXMaxYMaxZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight));
			benchFace = faceUv.getFace(Direction.WEST);
			texU = benchFace.getUv()[0];
			texV = benchFace.getUv()[1];
			uSize = benchFace.getUvSize()[0];
			vSize = benchFace.getUvSize()[1];
			this.polygons.add(new Polygon(new Vertex[]{ptvMinXMinYMinZ, ptvMinXMinYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight));
			benchFace = faceUv.getFace(Direction.DOWN);
			texU = benchFace.getUv()[0];
			texV = benchFace.getUv()[1];
			uSize = benchFace.getUvSize()[0];
			vSize = benchFace.getUvSize()[1];
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMinXMinYMaxZ, ptvMinXMinYMinZ, ptvMaxXMinYMinZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight));
			benchFace = faceUv.getFace(Direction.UP);
			texU = benchFace.getUv()[0];
			texV = benchFace.getUv()[1];
			uSize = benchFace.getUvSize()[0];
			vSize = benchFace.getUvSize()[1];
			if (flipBottomUV) {
				Polygon polygon = new Polygon(new Vertex[]{ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight);
				polygon.invertNormal = true;
				this.polygons.add(polygon);
			} else {
				Polygon polygon = new Polygon(new Vertex[]{ptvMaxXMaxYMinZ, ptvMinXMaxYMinZ, ptvMinXMaxYMaxZ, ptvMaxXMaxYMaxZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight);
				this.polygons.add(polygon);
			}
			benchFace = faceUv.getFace(Direction.NORTH);
			texU = benchFace.getUv()[0];
			texV = benchFace.getUv()[1];
			uSize = benchFace.getUvSize()[0];
			vSize = benchFace.getUvSize()[1];
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMinZ, ptvMinXMinYMinZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight));
			benchFace = faceUv.getFace(Direction.SOUTH);
			texU = benchFace.getUv()[0];
			texV = benchFace.getUv()[1];
			uSize = benchFace.getUvSize()[0];
			vSize = benchFace.getUvSize()[1];
			this.polygons.add(new Polygon(new Vertex[]{ptvMinXMinYMaxZ, ptvMaxXMinYMaxZ, ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ}, (int) texU, (int) texV, (int) (texU + uSize), (int) (texV + vSize), texWidth, texHeight));
			if (this.isMirror()) {
				for (Polygon face : this.polygons) {
					face.flipFace();
				}
			}
		} else if (polygons == null) {
			this.corners = new Vertex[8];
			this.polygons = Lists.newArrayList();

			float texU = uv.get(0);
			float texV = uv.get(1);
			float minX = x;
			float minY = y;
			float minZ = z;
			float maxX = size.x + x;
			float maxY = size.y + y;
			float maxZ = size.z + z;
			float sizeX = size.x;
			float sizeY = size.y;
			float sizeZ = size.z;
			minX -= inflate;
			minY -= inflate;
			minZ -= inflate;
			maxX += inflate;
			maxY += inflate;
			maxZ += inflate;

			if (this.isMirror()) {
				float temp = maxX;
				maxX = minX;
				minX = temp;
			}
			Vertex ptvMinXMinYMinZ = new Vertex(minX, minY, minZ, 0.0f, 0.0f);
			Vertex ptvMaxXMinYMinZ = new Vertex(maxX, minY, minZ, 0.0f, 8.0f);
			Vertex ptvMaxXMaxYMinZ = new Vertex(maxX, maxY, minZ, 8.0f, 8.0f);
			Vertex ptvMinXMaxYMinZ = new Vertex(minX, maxY, minZ, 8.0f, 0.0f);
			Vertex ptvMinXMinYMaxZ = new Vertex(minX, minY, maxZ, 0.0f, 0.0f);
			Vertex ptvMaxXMinYMaxZ = new Vertex(maxX, minY, maxZ, 0.0f, 8.0f);
			Vertex ptvMaxXMaxYMaxZ = new Vertex(maxX, maxY, maxZ, 8.0f, 8.0f);
			Vertex ptvMinXMaxYMaxZ = new Vertex(minX, maxY, maxZ, 8.0f, 0.0f);
			this.corners[0] = ptvMinXMinYMinZ;
			this.corners[1] = ptvMaxXMinYMinZ;
			this.corners[2] = ptvMaxXMaxYMinZ;
			this.corners[3] = ptvMinXMaxYMinZ;
			this.corners[4] = ptvMinXMinYMaxZ;
			this.corners[5] = ptvMaxXMinYMaxZ;
			this.corners[6] = ptvMaxXMaxYMaxZ;
			this.corners[7] = ptvMinXMaxYMaxZ;
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMaxXMinYMinZ, ptvMaxXMaxYMinZ, ptvMaxXMaxYMaxZ}, (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX + sizeZ), (int) (texV + sizeZ + sizeY), texWidth, texHeight));
			this.polygons.add(new Polygon(new Vertex[]{ptvMinXMinYMinZ, ptvMinXMinYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ}, (int) texU, (int) (texV + sizeZ), (int) (texU + sizeZ), (int) (texV + sizeZ + sizeY), texWidth, texHeight));
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMinXMinYMaxZ, ptvMinXMinYMinZ, ptvMaxXMinYMinZ}, (int) (texU + sizeZ), (int) texV, (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ), texWidth, texHeight));
			if (flipBottomUV) {
				Polygon polygon = new Polygon(new Vertex[]{ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) (texU + sizeZ + sizeX), (int) texV, (int) (texU + sizeZ + sizeX + sizeX), (int) (texV + sizeZ), texWidth, texHeight);
				polygon.invertNormal = true;
				this.polygons.add(polygon);
			} else {
				Polygon polygon = new Polygon(new Vertex[]{ptvMaxXMaxYMinZ, ptvMinXMaxYMinZ, ptvMinXMaxYMaxZ, ptvMaxXMaxYMaxZ}, (int) (texU + sizeZ + sizeX), (int) texV, (int) (texU + sizeZ + sizeX + sizeX), (int) (texV + sizeZ), texWidth, texHeight);
				this.polygons.add(polygon);
			}
			this.polygons.add(new Polygon(new Vertex[]{ptvMaxXMinYMinZ, ptvMinXMinYMinZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) (texU + sizeZ), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ + sizeY), texWidth, texHeight));
			this.polygons.add(new Polygon(new Vertex[]{ptvMinXMinYMaxZ, ptvMaxXMinYMaxZ, ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ}, (int) (texU + sizeZ + sizeX + sizeZ), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX + sizeZ + sizeX), (int) (texV + sizeZ + sizeY), texWidth, texHeight));
			if (this.isMirror()) {
				for (Polygon face : this.polygons) {
					face.flipFace();
				}
			}
		}
	}

	private Polygon getTexturedQuad(Vertex[] positionsIn, float texWidth, float texHeight, Direction direction, BenchFaceUVsItem faces) {
		BenchEntityFace face = faces.getFace(direction);
		if (Utilities.equalFloat(face.getUvSize()[0], 0.0F) && Utilities.equalFloat(face.getUvSize()[1], 0.0F))
			return null;
		double u1 = face.getUv()[0];
		double v1 = face.getUv()[1];
		double u2 = u1 + face.getUvSize()[0];
		double v2 = v1 + face.getUvSize()[1];
		Polygon polygon = new Polygon(positionsIn, (int) u1, (int) v1, (int) u2, (int) v2, (int) texWidth, (int) texHeight);

		for (int i = 0; i < polygon.vertexPositions.length; ++i) {
			polygon.vertexPositions[i].vector3D.xCoord += direction.getOffsetX();
			polygon.vertexPositions[i].vector3D.yCoord += direction.getOffsetY();
			polygon.vertexPositions[i].vector3D.zCoord += direction.getOffsetZ();
		}

		return polygon;
	}

	public void compileDisplayList(float f) {
		if (this.polygons != null) {
			this.displayList = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(this.displayList, 4864);
			Tessellator tessellator = Tessellator.instance;
            for (Polygon polygon : this.polygons) {
                polygon.draw(tessellator, f);
            }
			GL11.glEndList();
			this.compiled = true;
		}
	}

	public void setRotationPoint(float x, float y, float z) {
		this.rotationPointX = x;
		this.rotationPointY = y;
		this.rotationPointZ = z;
	}

	public void setRotationAngle(float x, float y, float z) {
		this.rotateAngleX = x;
		this.rotateAngleY = y;
		this.rotateAngleZ = z;
	}

	private static boolean equalFloats(float a, float b) {
		return Math.abs(Float.compare(a, b)) < COMPARE_CONST;
	}

	public boolean isCompiled() {
		return compiled;
	}

	public int getDisplayList() {
		return displayList;
	}

}
