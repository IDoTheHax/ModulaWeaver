package net.idothehax.modulaweaver.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.PathUtil;
import net.minecraft.client.MinecraftClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextEditorScreen extends Screen {
    private TextFieldWidget textField;

    public TextEditorScreen(Text title) {
        super(title);
    }

    @Override
    protected void init() {
        super.init();
        // Initialize the text field
        this.textField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Text.of(""));

        this.addSelectableChild(this.textField);
        this.setFocused(this.textField);
        this.textField.setFocusUnlocked(true);
        this.textField.setEditableColor(0xFFFFFF);

        // Add a button to save the text
        this.addDrawableChild(ButtonWidget.builder(Text.of("Save"), button -> {
            saveTextToFile(this.textField.getText());
        }).dimensions(this.width / 2 - 100, this.height / 2 + 20, 200, 20).build());
    }

    private void saveTextToFile(String text) {
        Path customDirectory = Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "modulaweaver");
        try {
            PathUtil.createDirectories(customDirectory);
            Path filePath = customDirectory.resolve("MyClass.java");
            Files.writeString(filePath, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title.getString(), this.width / 2, 20, 0xFFFFFF);
        this.textField.render(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return this.textField.charTyped(chr, keyCode) || super.charTyped(chr, keyCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.textField.keyPressed(keyCode, scanCode, modifiers) || this.textField.isActive()) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void tick() {
        // Update the text field manually if necessary
        this.textField.setEditable(this.textField.isActive());
        super.tick();
    }
}