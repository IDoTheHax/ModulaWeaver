package net.idothehax.modulaweaver.client.imgui;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.extension.texteditor.TextEditor;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ModulaEditor {

    private final TextEditor editor;
    private final String saveText;
    private String oldSource;
    private SaveCallback saveCallback;
    private String fileName;

    private final ImBoolean open;

    public ModulaEditor(@Nullable String saveText) {
        this.editor = new TextEditor();
        this.editor.setShowWhitespaces(false);
        this.saveText = saveText != null ? saveText : "Save";
        this.oldSource = null;
        this.saveCallback = null;

        this.open = new ImBoolean(false);
    }

    public boolean hasTextChanged() {
        return this.oldSource != null && !this.oldSource.equals(this.editor.getText());
    }

    public void save() {
        Map<Integer, String> errors = new HashMap<>();
        if (this.saveCallback != null) {
            this.saveCallback.save(this.editor.getText(), errors::put);
        }
        if (errors.isEmpty()) {
            this.oldSource = this.editor.getText();
            saveTextToFile(this.editor.getText());
        }
        this.editor.setErrorMarkers(errors);
    }

    private void saveTextToFile(String text) {
        Path filePath = getFilePath();
        try {
            Files.writeString(filePath, text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getFilePath() {
        return Paths.get(MinecraftClient.getInstance().runDirectory.getAbsolutePath(), "ModulaWeaver", this.fileName);
    }

    public void show(@Nullable String fileName, String source) {
        this.editor.setText(source);
        this.fileName = fileName != null ? fileName : "untitled.java";
        this.oldSource = this.editor.getText();
        this.editor.setErrorMarkers(Collections.emptyMap());
        this.open.set(true);
        ImGui.setWindowFocus("###editor");
        ImGui.setWindowCollapsed("###editor", false);
    }

    public void hide() {
        if (this.hasTextChanged()) {
            this.open.set(true);
            ImGui.pushID(this.hashCode());
            ImGui.openPopup("###save_confirm");
            ImGui.popID();
        } else {
            this.oldSource = null;
            this.open.set(false);
        }
    }

    public void renderWindow() {
        int flags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoSavedSettings;
        if (this.hasTextChanged()) {
            flags |= ImGuiWindowFlags.UnsavedDocument;
        }

        if (!this.open.get()) {
            return;
        }

        int id = this.hashCode();
        ImGui.pushID(id);
        ImGui.setNextWindowSizeConstraints(800, 600, Float.MAX_VALUE, Float.MAX_VALUE);
        if (ImGui.begin((this.fileName != null ? "Editor: " + this.fileName : "Editor") + "###editor" + id, this.open, flags)) {
            this.render();
        }

        if (!this.open.get()) {
            this.hide();
        }

        ImGui.end();
        ImGui.popID();
    }

    public void render() {
        ImGui.pushID(this.hashCode());
        if (this.open.get()) {
            if (!this.hasTextChanged()) {
                this.editor.setErrorMarkers(Collections.emptyMap());
            }

            if (ImGui.beginMenuBar()) {
                boolean immutable = this.editor.isReadOnly();
                if (ImGui.menuItem("Read-only mode", "", immutable)) {
                    this.editor.setReadOnly(!immutable);
                }
                if (ImGui.menuItem("Show Whitespace", "", this.editor.isShowingWhitespaces())) {
                    this.editor.setShowWhitespaces(!this.editor.isShowingWhitespaces());
                }

                if (this.saveText != null) {
                    if (ImGui.menuItem(this.saveText)) {
                        this.save();
                    }
                }

                ImGui.separator();

                ImGui.beginDisabled(immutable);
                {
                    ImGui.beginDisabled(!this.editor.canUndo());
                    if (ImGui.menuItem("Undo", "ALT-Backspace")) {
                        this.editor.undo(1);
                    }
                    ImGui.endDisabled();

                    ImGui.beginDisabled(!this.editor.canRedo());
                    if (ImGui.menuItem("Redo", "Ctrl-Y")) {
                        this.editor.redo(1);
                    }
                    ImGui.endDisabled();
                }
                ImGui.endDisabled();
                ImGui.separator();

                ImGui.beginDisabled(!this.editor.hasSelection());
                if (ImGui.menuItem("Copy", "Ctrl-C")) {
                    this.editor.copy();
                }
                ImGui.endDisabled();

                ImGui.beginDisabled(immutable);
                {
                    ImGui.beginDisabled(!this.editor.hasSelection());
                    if (ImGui.menuItem("Cut", "Ctrl-X")) {
                        this.editor.cut();
                    }
                    if (ImGui.menuItem("Delete", "Del")) {
                        this.editor.delete();
                    }
                    ImGui.endDisabled();

                    ImGui.beginDisabled(ImGui.getClipboardText() == null);
                    if (ImGui.menuItem("Paste", "Ctrl-V")) {
                        this.editor.paste();
                    }
                    ImGui.endDisabled();
                }
                ImGui.endDisabled();

                ImGui.endMenuBar();
            }

            int cposX = this.editor.getCursorPositionLine();
            int cposY = this.editor.getCursorPositionColumn();

            String overwrite = this.editor.isOverwrite() ? "Ovr" : "Ins";
            String canUndo = this.editor.canUndo() ? "*" : " ";

            ImGui.text(cposX + ":" + cposY + " " + this.editor.getTotalLines() + " lines | " + overwrite + " | " + canUndo);

            this.editor.render("TextEditor");
        }

        ImVec2 center = ImGui.getMainViewport().getCenter();
        ImGui.setNextWindowPos(center.x, center.y, ImGuiCond.Appearing, 0.5f, 0.5f);

        if (ImGui.beginPopupModal(this.saveText + "?###save_confirm", ImGuiWindowFlags.AlwaysAutoResize)) {
            ImGui.text("Your changes have not been saved.\nThis operation cannot be undone!");
            ImGui.separator();

            ImGui.setItemDefaultFocus();
            if (ImGui.button(this.saveText)) {
                this.save();
                this.hide();
                ImGui.closeCurrentPopup();
            }

            ImGui.sameLine();
            if (ImGui.button("Discard")) {
                this.oldSource = null;
                this.hide();
                ImGui.closeCurrentPopup();
            }

            ImGui.sameLine();
            if (ImGui.button("Cancel")) {
                ImGui.closeCurrentPopup();
            }

            ImGui.endPopup();
        }

        ImGui.popID();
    }

    public TextEditor getEditor() {
        return this.editor;
    }

    public void setSaveCallback(@Nullable SaveCallback saveCallback) {
        this.saveCallback = saveCallback;
    }

    @FunctionalInterface
    public interface SaveCallback {
        void save(String source, BiConsumer<Integer, String> errorConsumer);
    }
}