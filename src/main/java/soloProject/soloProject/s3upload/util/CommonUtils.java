package soloProject.soloProject.s3upload.util;


import org.springframework.http.ContentDisposition;

import java.nio.charset.StandardCharsets;

public class CommonUtils {
    private static final String FILE_EXTENSION_SEPARATOR = ".";

    public static String buildFileName(String originalFileName) {
        int fileExtensionIndex = originalFileName.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String fileExtension = originalFileName.substring(fileExtensionIndex);
        String fileName = originalFileName.substring(0, fileExtensionIndex);
        String now = String.valueOf(System.currentTimeMillis());

        return fileName + now + fileExtension;
    }

    public static ContentDisposition createContentDisposition(String categoryWithFileName) {
        String fileName = categoryWithFileName;
        return ContentDisposition.builder("attachment")
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }
}