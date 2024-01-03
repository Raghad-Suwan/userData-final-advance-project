package edu.najah.cap.data.export;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PdfZipper {

    public static byte[] createZipFile(List<byte[]> pdfs) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            int index = 1;
            for (byte[] pdf : pdfs) {
                ZipEntry entry = new ZipEntry("Document_" + index + ".pdf");
                zos.putNextEntry(entry);
                zos.write(pdf);
                zos.closeEntry();
                index++;
            }
            zos.finish();
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }}