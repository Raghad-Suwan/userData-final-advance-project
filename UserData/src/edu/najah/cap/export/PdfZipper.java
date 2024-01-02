package edu.najah.cap.export;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfZipper {
    public static void createZipFile(List<byte[]> pdfs, String zipFilePath) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            int index = 1;
            for (byte[] pdf : pdfs) {

                ZipEntry entry = new ZipEntry("Document_" + index + ".pdf");

                zos.putNextEntry(entry);
                zos.write(pdf);
                zos.closeEntry();
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}