package net.idothehax.modulaweaver;

import net.fabricmc.api.ModInitializer;
import net.idothehax.modulaweaver.config.ConfigHandler;

public class ModulaWeaver implements ModInitializer {
    @Override
    public void onInitialize() {
        ConfigHandler.loadConfig();
    }
}
