package edu.najah.cap.data.export.PdfConvertor;


import java.util.List;

public interface Converter<T> {
    byte[] convert(List<T> dataList);

}