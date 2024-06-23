package net.idothehax.modulaweaver;

import net.fabricmc.api.ModInitializer;
import net.idothehax.modulaweaver.client.option.ModulaWeaverKeybinds;
import net.idothehax.modulaweaver.command.ModulaWeaverCommand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModulaWeaver implements ModInitializer {
    public static final String MOD_ID = "modulaweaver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Hello From ModulaWeaver");
        ModulaWeaverCommand.register();
        ModulaWeaverKeybinds.register();
    }
}
