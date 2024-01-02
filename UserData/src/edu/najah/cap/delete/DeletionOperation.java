package edu.najah.cap.delete;

interface DeletionOperation<T> {
    void delete(T item);
}