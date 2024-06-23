package net.idothehax.modulaweaver.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CustomGuiScreen extends Screen {
    private List<TextFieldWidget> textFields;
    private ButtonWidget saveButton;
    private int scrollOffset = 0;
    private static final int LINES = 20; // Number of visible lines
    private static final int LINE_HEIGHT = 12; // Height of each line
    private static final int EDITOR_WIDTH = 300; // Width of the editor

    public CustomGuiScreen() {
        super(Text.literal("Custom Text Editor"));
    }

    @Override
    protected void init() {
        textFields = new ArrayList<>();
        int startY = (this.height - LINES * LINE_HEIGHT) / 2;

        for (int i = 0; i < LINES; i++) {
            TextFieldWidget textField = new TextFieldWidget(
                    this.textRenderer,
                    (this.width - EDITOR_WIDTH) / 2,
                    startY + i * LINE_HEIGHT,
                    EDITOR_WIDTH,
                    LINE_HEIGHT,
                    Text.literal("")
            );
            textField.setMaxLength(32767);
            textFields.add(textField);
            this.addDrawableChild(textField);
        }

        this.saveButton = ButtonWidget.builder(Text.literal("Save"), button -> {
                    saveTextToFile(getAllText());
                })
                .dimensions(this.width / 2 - 50, startY + LINES * LINE_HEIGHT + 10, 100, 20)
                .build();
        this.addDrawableChild(this.saveButton);
    }

    private String getAllText() {
        StringBuilder sb = new StringBuilder();
        for (TextFieldWidget textField : textFields) {
            sb.append(textField.getText()).append("\n");
        }
        return sb.toString();
    }

    private void saveTextToFile(String text) {
        Path customDirectory = Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "modulaweaver");
        try {
            Files.createDirectories(customDirectory);
            Path filePath = customDirectory.resolve("MyFile.txt");
            Files.writeString(filePath, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawTextWithShadow(this.textRenderer, Text.literal("Custom Text Editor"),
                this.width / 2 - 50, (this.height - LINES * LINE_HEIGHT) / 2 - 20, 0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // GLFW_KEY_ESCAPE
            this.close();
            return true;
        }
        if (keyCode == 265 && scrollOffset > 0) { // GLFW_KEY_UP
            scrollOffset--;
            updateTextFieldContents();
        }
        if (keyCode == 264) { // GLFW_KEY_DOWN
            scrollOffset++;
            updateTextFieldContents();
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void updateTextFieldContents() {
        // Implement logic to update text fields based on scroll offset
        // This is a placeholder and would need to be implemented based on how you store the full text
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount > 0 && scrollOffset > 0) {
            scrollOffset--;
            updateTextFieldContents();
        } else if (verticalAmount < 0) {
            scrollOffset++;
            updateTextFieldContents();
        }
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}