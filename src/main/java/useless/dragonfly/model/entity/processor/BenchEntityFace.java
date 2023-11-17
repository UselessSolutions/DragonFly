package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.SerializedName;


/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/FaceItem.java
 */
public class BenchEntityFace {

	@SerializedName("uv")
	private float[] uv;
	@SerializedName("uv_size")
	private float[] uvSize;

	public float[] getUv() {
		return uv;
	}

	public float[] getUvSize() {
		return uvSize;
	}

	public static BenchEntityFace empty() {
		BenchEntityFace face = new BenchEntityFace();
		face.uv = new float[]{0, 0};
		face.uvSize = new float[]{0, 0};
		return face;
	}
}
