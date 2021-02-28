package ru.gb.backend.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@UtilityClass
public class FileEncodingUtils {
    public byte[] getFileContentInBase64() {
        ClassLoader classLoader = FileEncodingUtils.class.getClassLoader();
        File inputFile = new File(Objects.requireNonNull(classLoader.getResource("pineapple.jpg")).getFile());
        byte[] fileContent = new byte[0];
        try {
            fileContent =   FileUtils.readFileToByteArray(inputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
