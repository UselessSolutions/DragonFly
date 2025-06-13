package org.useless.test_entity;

import net.minecraft.core.util.collection.NamespaceID;
import turniplabs.halplibe.helper.EntityHelper;

import static org.useless.DragonFly.MOD_ID;

public class ModEntity {

	public String entityKey(String string) {
		return MOD_ID + ".entity." + string;
	}

	public void initializeEntities() {
		EntityHelper.createEntity(TestMob.class, NamespaceID.getPermanent(MOD_ID, "test"), entityKey("test"));
	}
}
