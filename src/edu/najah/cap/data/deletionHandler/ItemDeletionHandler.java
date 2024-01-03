package edu.najah.cap.data.deletionHandler;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

public interface ItemDeletionHandler<T> {
    void handleDeletion(T item) throws SystemBusyException, BadRequestException, NotFoundException;
}

