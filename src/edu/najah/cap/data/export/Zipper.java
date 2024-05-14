package edu.najah.cap.data.export;

import java.io.IOException;
import java.util.List;

public interface Zipper {
    void createZipFile(List<byte[]> files, String zipFilePath) throws IOException;
}

