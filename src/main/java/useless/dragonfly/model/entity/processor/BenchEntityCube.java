package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.client.GLAllocation;
import net.minecraft.client.render.Polygon;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.Vertex;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class BenchEntityCube {

	private static final float COMPARE_CONST = 0.001f;
	private Vertex[] corners;
	private Polygon[] faces;


	@Expose
	@SerializedName("mirror")
	private boolean mirror;
	@Expose
	@SerializedName("inflate")
	private float inflate;

	@Expose
	@SerializedName("size")
	private List<Float> size;

	@Expose
	@SerializedName("origin")
	private List<Float> origin;

	@Expose
	@SerializedName("uv")
	private List<Float> uv;

	@Expose
	@SerializedName("rotation")
	private List<Float> rotation;

	@Expose
	@SerializedName("pivot")
	private List<Float> pivot;

	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;
	private boolean compiled = false;
	private int displayList = 0;

	public boolean isMirror() {
		return mirror;
	}

	public float getInflate() {
		return inflate;
	}

	public List<Float> getSize() {
		return size;
	}

	public List<Float> getOrigin() {
		return origin;
	}

	@Nullable
	public List<Float> getRotation() {
		return rotation;
	}

	@Nullable
	public List<Float> getPivot() {
		return pivot;
	}

	public List<Float> getUv() {
		return uv;
	}

	public void addBox(int texWidth, int texHeight, float x, float y, float z, boolean flipBottomUV) {
		if (faces == null) {
			this.corners = new Vertex[8];
			this.faces = new Polygon[6];

			float texU = uv.get(0);
			float texV = uv.get(1);
			float minX = x;
			float minY = y;
			float minZ = z;
			float maxX = size.get(0) + x;
			float maxY = size.get(1) + y;
			float maxZ = size.get(2) + z;
			float sizeX = size.get(0);
			float sizeY = size.get(1);
			float sizeZ = size.get(2);
			minX -= inflate;
			minY -= inflate;
			minZ -= inflate;
			maxX += inflate;
			maxY += inflate;
			maxZ += inflate;

			if (this.mirror) {
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
			this.faces[0] = new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMaxXMinYMinZ, ptvMaxXMaxYMinZ, ptvMaxXMaxYMaxZ}, (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX + sizeZ), (int) (texV + sizeZ + sizeY), texWidth, texHeight);
			this.faces[1] = new Polygon(new Vertex[]{ptvMinXMinYMinZ, ptvMinXMinYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ}, (int) texU, (int) (texV + sizeZ), (int) (texU + sizeZ), (int) (texV + sizeZ + sizeY), texWidth, texHeight);
			this.faces[2] = new Polygon(new Vertex[]{ptvMaxXMinYMaxZ, ptvMinXMinYMaxZ, ptvMinXMinYMinZ, ptvMaxXMinYMinZ}, (int) (texU + sizeZ), (int) texV, (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ), texWidth, texHeight);
			if (flipBottomUV) {
				this.faces[3] = new Polygon(new Vertex[]{ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) (texU + sizeZ + sizeX), (int) texV, (int) (texU + sizeZ + sizeX + sizeX), (int) (texV + sizeZ), texWidth, texHeight);
				this.faces[3].invertNormal = true;
			} else {
				this.faces[3] = new Polygon(new Vertex[]{ptvMaxXMaxYMinZ, ptvMinXMaxYMinZ, ptvMinXMaxYMaxZ, ptvMaxXMaxYMaxZ}, (int) (texU + sizeZ + sizeX), (int) texV, (int) (texU + sizeZ + sizeX + sizeX), (int) (texV + sizeZ), texWidth, texHeight);
			}
			this.faces[4] = new Polygon(new Vertex[]{ptvMaxXMinYMinZ, ptvMinXMinYMinZ, ptvMinXMaxYMinZ, ptvMaxXMaxYMinZ}, (int) (texU + sizeZ), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX), (int) (texV + sizeZ + sizeY), texWidth, texHeight);
			this.faces[5] = new Polygon(new Vertex[]{ptvMinXMinYMaxZ, ptvMaxXMinYMaxZ, ptvMaxXMaxYMaxZ, ptvMinXMaxYMaxZ}, (int) (texU + sizeZ + sizeX + sizeZ), (int) (texV + sizeZ), (int) (texU + sizeZ + sizeX + sizeZ + sizeX), (int) (texV + sizeZ + sizeY), texWidth, texHeight);
			if (this.mirror) {
				for (Polygon face : this.faces) {
					face.flipFace();
				}
			}
		}
	}

	public void compileDisplayList(float f) {
		if (this.faces != null) {
			this.displayList = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(this.displayList, 4864);
			Tessellator tessellator = Tessellator.instance;
			for (int i = 0; i < this.faces.length; ++i) {
				this.faces[i].draw(tessellator, f);
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
