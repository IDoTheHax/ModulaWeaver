package net.idothehax.modulaweaver.client.option;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModulaWeaverKeybinds {
    public static KeyBinding openWindowKey;

    public static void register() {
        openWindowKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.modulaweaver.open_window",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.modulaweaver.window"
        ));
    }
}