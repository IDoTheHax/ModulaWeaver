package net.idothehax.modulaweaver.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.idothehax.modulaweaver.screen.CustomGuiScreen;
import net.idothehax.modulaweaver.screen.TextEditorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.PathUtil;
import net.minecraft.world.level.storage.LevelStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModulaWeaverCommand {

    public static void register() {

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("modulaweaver")
                    .then(CommandManager.literal("testclass")
                            .executes(ModulaWeaverCommand::executeTestClassCommand)));
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("modulaweaver")
                    .then(CommandManager.literal("new")
                            .executes(ModulaWeaverCommand::openTextEditor)));
        });
    }

    private static int executeTestClassCommand(CommandContext<ServerCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();

        // Define your custom folder path
        Path modulaDirectory = Paths.get(client.runDirectory.getAbsolutePath(), "ModulaWeaver");


        try {
            // Create the custom directory if it doesn't exist
            PathUtil.createDirectories(modulaDirectory);

            // Example: Create a .java file inside the custom directory
            Path filePath = modulaDirectory.resolve("TestClass.java");

            // Example Java code content to write to the file
            String javaCode = """
                public class TestClass {
                    public static void main(String[] args) {
                        System.out.println("Hello, Minecraft Modding!");
                    }
                }
            """;

            // Write Java code content to the .java file
            Files.writeString(filePath, javaCode);

            context.getSource().sendFeedback(() -> Text.translatable("commands.modulaweaver.command.loadfile.success"), false);
            return 1;
        } catch (IOException e) {
            context.getSource().sendError(Text.translatable("commands.modulaweaver.command.loadfile.failed"));
            return 0;
        }
    }

    private static int openTextEditor(CommandContext<ServerCommandSource> context) {
        MinecraftClient client = MinecraftClient.getInstance();
        client.execute(() -> client.setScreen(new CustomGuiScreen()));
        return 1;
    }
}