package net.idothehax.modulaweaver;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ConfigScreen;
import net.fabricmc.api.ModInitializer;
import net.idothehax.modulaweaver.client.option.ModulaWeaverKeybinds;
import net.idothehax.modulaweaver.command.ModulaWeaverCommand;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ModulaWeaver implements ModInitializer {
    public static final String MOD_ID = "modulaweaver";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MinecraftClient MC;

    private Path modulaFolder;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello From ModulaWeaver");

        MC = MinecraftClient.getInstance();
        ModulaWeaverCommand.register();
        ModulaWeaverKeybinds.register();

        modulaFolder = createModulaFolder();
    }

    private Path createModulaFolder() {
        Path dotMinecraftFolder = MC.runDirectory.toPath().normalize();
        Path modulaFolder = dotMinecraftFolder.resolve("ModulaWeaver");

        try {
            LOGGER.info("Created ModulaWeaver Folder");
            Files.createDirectories(modulaFolder);
        } catch(IOException e) {
            throw new RuntimeException("Couldn't create .minecraft/ModulaWeaver folder.", e);
        }

        return modulaFolder;
    }
}
