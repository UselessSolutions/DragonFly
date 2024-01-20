package useless.dragonfly.model.entity.processor;

import com.google.common.collect.Lists;
import useless.dragonfly.utilities.vector.Vector3f;

import javax.annotation.Nullable;
import java.util.List;


/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/BonesItem.java
 */
public class BenchEntityBones {
	private final List<BenchEntityCube> cubes;
	private final String name;
	private final Vector3f pivot;
	private final Vector3f rotation;
	private final String parent;
	private final boolean mirror;
	public BenchEntityBones(List<BenchEntityCube> cubes, String name, Vector3f pivot, Vector3f rotation, String parent, boolean mirror){
        this.cubes = cubes;
        this.name = name;
        this.pivot = pivot;
        this.rotation = rotation;
        this.parent = parent;
        this.mirror = mirror;
    }
	private final List<BenchEntityBones> children = Lists.newArrayList();
	public float rotationPointX;
	public float rotationPointY;
	public float rotationPointZ;
	public float rotateAngleX;
	public float rotateAngleY;
	public float rotateAngleZ;

	public float scaleX = 1;
	public float scaleY = 1;
	public float scaleZ = 1;


	@Nullable
	public List<BenchEntityCube> getCubes() {
		return cubes;
	}

	public String getName() {
		return name;
	}

	public Vector3f getPivot() {
		return pivot;
	}

	public Vector3f getRotation() {
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

	public void setScale(float x, float y, float z) {
		this.scaleX = x;
		this.scaleY = y;
		this.scaleZ = z;
	}

	public void resetPose() {
		this.rotationPointX = 0;
		this.rotationPointY = 0;
		this.rotationPointZ = 0;
		this.rotateAngleX = 0;
		this.rotateAngleY = 0;
		this.rotateAngleZ = 0;
		this.scaleX = 1;
		this.scaleY = 1;
		this.scaleZ = 1;
	}


	public void addChild(BenchEntityBones benchEntityBones) {
		children.add(benchEntityBones);
	}

	public List<BenchEntityBones> getChildren() {
		return children;
	}
}
