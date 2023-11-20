package useless.dragonfly.debug.testentity;

import net.minecraft.core.entity.monster.EntityHuman;
import net.minecraft.core.world.World;

public class EntityWarden extends EntityHuman {
	public EntityWarden(World world) {
		super(world);
	}
	@Override
	public String getEntityTexture() {
		return "/assets/dragonfly/entity/warden.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/dragonfly/entity/warden.png";
	}
}
