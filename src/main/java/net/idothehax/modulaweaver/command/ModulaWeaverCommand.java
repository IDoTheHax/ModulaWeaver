package net.idothehax.modulaweaver.command;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.idothehax.modulaweaver.ModulaWeaver;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.function.Supplier;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

public class ModulaWeaverCommand {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("modulaweaver")
                    .executes(ModulaWeaverCommand::executeCommand));
        });
    }

    private static int executeCommand(CommandContext<ServerCommandSource> context) {
        context.getSource().sendFeedback(() -> Text.translatable("commands.modulaweaver.command.executed"), false);
        return 1;
    }
}