package useless.dragonfly.model.entity.processor;

import com.google.gson.annotations.SerializedName;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/Description.java
 */
public class Description {
	@SerializedName("texture_height")
	private int textureHeight;

	@SerializedName("texture_width")
	private int textureWidth;

	public int getTextureHeight() {
		return textureHeight;
	}

	public int getTextureWidth() {
		return textureWidth;
	}
}
