package net.idothehax.modulaweaver.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
    public static File loadFile(Path mcDirectory, String fileName) {
        Path filePath = mcDirectory.resolve(fileName);
        if (Files.exists(filePath)) {
            return new File(filePath.toString());  /* Return the file if it exists */
        } else {
            return null; /* Return null if the file did not exist */
        }
    }

    public static File createFile(Path mcDirectory, String fileName) {
        Path filePath = mcDirectory.resolve(fileName);
        try {
            return Files.createFile(filePath).toFile();  /* Return the newly created file */
        } catch (IOException e) {
            /* Insert code to handle the exception here, such as logging the error message */
            return null;
        }
    }
}