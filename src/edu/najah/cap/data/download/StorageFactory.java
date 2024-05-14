package edu.najah.cap.data.download;

import java.util.Scanner;




public class StorageFactory {
    private static IFileStorage fileStorageService;
    public static IFileStorage createStorageServiceInstance(int serviceChoice) {
        Scanner scanner = new Scanner(System.in);

        if (serviceChoice == 1) {
            fileStorageService = (IFileStorage) new GoodleDrive();
        } else if (serviceChoice == 2) {
            fileStorageService = (IFileStorage) new DropBox();
        } else {
            System.out.println("Invalid choice");
            scanner.close();

        }
        return fileStorageService;
    }}
