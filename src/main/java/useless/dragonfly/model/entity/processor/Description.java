package useless.dragonfly.model.entity.processor;

/*
 * Credit by 0999312! Thanks!
 * https://github.com/0999312/MMLib/blob/3e87210c9305a5724e06c492be503533a1ebcd59/src/main/java/cn/mcmod_mmf/mmlib/client/model/pojo/Description.java
 */
public class Description {
	private final int textureHeight;
	private final int textureWidth;
	public Description(int height, int width){
		this.textureHeight = height;
		this.textureWidth = width;
	}

	public int getTextureHeight() {
		return textureHeight;
	}

	public int getTextureWidth() {
		return textureWidth;
	}
}
