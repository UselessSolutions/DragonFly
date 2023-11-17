package useless.dragonfly.testentity;

import net.minecraft.core.entity.monster.EntityZombie;
import net.minecraft.core.world.World;

public class EntityZombieTest extends EntityZombie {
	public EntityZombieTest(World world) {
		super(world);
	}

	@Override
	public String getEntityTexture() {
		return "/assets/dragonfly/entity/zombie_test.png";
	}

	@Override
	public String getDefaultEntityTexture() {
		return "/assets/dragonfly/entity/zombie_test.png";
	}
}
