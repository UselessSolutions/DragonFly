package useless.dragonfly;

import net.minecraft.client.Minecraft;

public class DragonFlyClient {
	public static Minecraft minecraft;
	public static Minecraft getMinecraft(){
		if (minecraft != null){
			return minecraft;
		}
		minecraft = Minecraft.getMinecraft(Minecraft.class);
		return minecraft;
	}
}
