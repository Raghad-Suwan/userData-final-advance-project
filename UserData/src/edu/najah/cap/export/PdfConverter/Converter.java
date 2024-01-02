package edu.najah.cap.export.PdfConverter;


import java.util.List;

public interface Converter<T> {
    byte[] export(List<T> dataList);
}

