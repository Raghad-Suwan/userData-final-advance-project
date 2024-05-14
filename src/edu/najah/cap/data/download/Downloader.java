package edu.najah.cap.data.download;

import edu.najah.cap.data.export.PdfZipper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Downloader {

    private static final Logger logger = LoggerFactory.getLogger(Downloader.class);

    private static String downloadPath;
    private IFileStorage fileStorageService;

    public void initiateDownload(List<byte[]> pdfs) {
        Scanner scanner = new Scanner(System.in);

        logger.info("Initiating download/upload process");
        System.out.println("Choose an option:");
        System.out.println("1. Download locally");
        System.out.println("2. Upload to file storage service");
        int choice = scanner.nextInt();

        if (choice == 1) {
            downloadUserData(pdfs);
        } else if (choice == 2) {
            handleUpload(pdfs);
        } else {
            logger.warn("Invalid choice made in download/upload process");
            System.out.println("Invalid choice");
        }

        scanner.close();
    }

    private void downloadUserData(List<byte[]> pdfs) {
        logger.info("Downloading user data");
        byte[] zipFileBytes = PdfZipper.createZipFile(pdfs);
        generateDownLoadPath();
        saveFileToLocal(zipFileBytes);
    }

    private void saveFileToLocal(byte[] zipFileBytes) {
        try {
            FileOutputStream fos = new FileOutputStream(downloadPath);
            fos.write(zipFileBytes);
            fos.close();
            logger.info("File saved successfully at: {}", downloadPath);
            System.out.println("File saved successfully at: " + downloadPath);
        } catch (IOException e) {
            logger.error("Error saving file to local storage", e);
            e.printStackTrace();
        }
    }

    private void handleUpload(List<byte[]> pdfs) {
        logger.info("Handling file upload");
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose a file storage service:");
        System.out.println("1. Google Drive");
        System.out.println("2. Dropbox");
        int serviceChoice = scanner.nextInt();
        fileStorageService = StorageFactory.createStorageServiceInstance(serviceChoice);
        fileStorageService.uploadFile(PdfZipper.createZipFile(pdfs));

        scanner.close();
    }

    public static String generateDownLoadPath() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String zipFileName = "UserData_" + timeStamp + ".zip";

        String userHome = System.getProperty("user.home");
        downloadPath = userHome + "/Downloads/" + zipFileName;
        logger.info("Generated download path: {}", downloadPath);
        return downloadPath;
    }
}
