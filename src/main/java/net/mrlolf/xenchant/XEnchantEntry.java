package net.mrlolf.xenchant;

import net.fabricmc.api.ModInitializer;
import net.mrlolf.xenchant.util.ModRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class XEnchantEntry implements ModInitializer {
	public static final String MOD_ID = "xenchant";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModRegistries.registerModStuff();
	}
}
