package useless.dragonfly.debug.testentity;

import net.minecraft.core.entity.monster.EntityGhast;
import net.minecraft.core.world.World;

public class EntityDragon extends EntityGhast {
	public EntityDragon(World world) {
		super(world);
	}
	@Override
	public String getEntityTexture() {
		return "/assets/dragonfly/entity/dragontex2variant.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/dragonfly/entity/dragontex2variant.png";
	}
}
