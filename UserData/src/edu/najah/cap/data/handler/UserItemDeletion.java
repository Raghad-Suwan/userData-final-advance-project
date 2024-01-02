package edu.najah.cap.data.handler;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UserItemDeletion {
    public static <T> void deleteItems(String userName, List<T> items, ItemDeletionHandler<T> deletionHandler, String itemName) throws SystemBusyException, BadRequestException, NotFoundException {
        List<T> itemsToDelete = new ArrayList<>();
        if(items != null) {
            itemsToDelete.addAll(items);
        }else {
            System.out.println("No " + itemName + " found for the user: " + userName);
        }
        Iterator<T> iterator = itemsToDelete.iterator();
        while (iterator.hasNext()) {
            T item = iterator.next();
            try {
                deletionHandler.handleDeletion(item);
                iterator.remove();
                System.out.println("< " + itemName.substring(0, 1).toUpperCase() + itemName.substring(1) + " Deleted >");
            } catch (SystemBusyException | BadRequestException | NotFoundException e) {
                System.err.println("An error in item deletion " + itemName + ": " + e.getMessage());
            }
        }
    }
}