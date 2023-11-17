package useless.dragonfly.model.entity.processor;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;


/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/BonesItem.java
 */
public class BenchEntityBones {
	@SerializedName("cubes")
	private List<BenchEntityCube> cubes;

	@SerializedName("name")
	private String name;

	@SerializedName("pivot")
	private List<Float> pivot;

	@SerializedName("rotation")
	private List<Float> rotation;

	@SerializedName("parent")
	private String parent;

	@SerializedName("mirror")
	private boolean mirror;

	private final List<BenchEntityBones> children = Lists.newArrayList();
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;


	@Nullable
	public List<BenchEntityCube> getCubes() {
		return cubes;
	}

	public String getName() {
		return name;
	}

	public List<Float> getPivot() {
		return pivot;
	}

	public List<Float> getRotation() {
		return rotation;
	}

	public String getParent() {
		return parent;
	}

	public boolean isMirror() {
		return mirror;
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

	public void addChild(BenchEntityBones benchEntityBones) {
		children.add(benchEntityBones);
	}

	public List<BenchEntityBones> getChildren() {
		return children;
	}
}
