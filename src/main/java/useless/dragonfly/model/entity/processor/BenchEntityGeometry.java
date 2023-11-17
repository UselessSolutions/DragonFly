package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/GeometryModelNew.java
 */
public class BenchEntityGeometry {

	protected final HashMap<String, BenchEntityBones> modelMap = new HashMap<>();

	@SerializedName("description")
	private Description description;

	@SerializedName("bones")
	private BenchEntityBones[] bones;


	public int getWidth() {
		return description.getTextureWidth();
	}

	public int getHeight() {
		return description.getTextureHeight();
	}

	public BenchEntityBones[] getBones() {
		return bones;
	}

}
