package com.amazon.ata.kindlepublishingservice.publishing;

import org.checkerframework.checker.signature.qual.InternalForm;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class BookPublishRequestManager {
    private Queue<BookPublishRequest> requestQueue = new LinkedList<>();

    @Inject
    public BookPublishRequestManager() {
    }

    public Queue<BookPublishRequest> getRequestQueue() {
        return requestQueue;
    }

    public BookPublishRequestManager(Queue<BookPublishRequest> requestQueue) {
        this.requestQueue = requestQueue;
    }

    public void addBookPublishingRequest(BookPublishRequest bookPublishRequest) {
        requestQueue.offer(bookPublishRequest);
    }

    public BookPublishRequest getBookPublishRequestToProcess() {
        return requestQueue.poll();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookPublishRequestManager that = (BookPublishRequestManager) o;
        return Objects.equals(requestQueue, that.requestQueue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestQueue);
    }

    @Override
    public String toString() {
        return "BookPublishRequestManager{" +
                "bookPublishRequestQueue=" + requestQueue +
                '}';
    }
}
