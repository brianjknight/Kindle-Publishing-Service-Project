package com.amazon.ata.kindlepublishingservice.publishing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookPublishRequestManagerTest {
    BookPublishRequestManager bookPublishRequestManager;

    @Test
    void addBookPublishingRequest_providedRequest_itemAddedToQueue() {
        //GIVEN
        bookPublishRequestManager = new BookPublishRequestManager();
        BookPublishRequest bookPublishRequest = BookPublishRequest.builder()
                .withAuthor("Test Author")
                .withBookId("book.123")
                .build();
        int initialSize = bookPublishRequestManager.getRequestQueue().size();

        //WHEN
        bookPublishRequestManager.addBookPublishingRequest(bookPublishRequest);
        int endSize = bookPublishRequestManager.getRequestQueue().size();
        BookPublishRequest addedRequest = bookPublishRequestManager.getRequestQueue().peek();

        //THEN
        assertEquals(initialSize + 1, endSize, "Expected and item to be added to the queue.");
        assertEquals(bookPublishRequest, addedRequest, "Expected the added item to be equal.");
    }

    @Test
    void getBookPublishRequestToProcess_nonEmptyQueue_itemRemovedFromQueue() {
        //GIVEN
        BookPublishRequest bookPublishRequestOne = BookPublishRequest.builder()
                .withAuthor("Author One")
                .withBookId("book.1")
                .build();
        BookPublishRequest bookPublishRequestTwo = BookPublishRequest.builder()
                .withAuthor("Author Two")
                .withBookId("book.2")
                .build();

        Queue<BookPublishRequest> requestQueue = new LinkedList<>();
        requestQueue.offer(bookPublishRequestOne);
        requestQueue.offer(bookPublishRequestTwo);
        bookPublishRequestManager = new BookPublishRequestManager(requestQueue);
        int initialSize = bookPublishRequestManager.getRequestQueue().size();

        //WHEN
        BookPublishRequest result = bookPublishRequestManager.getBookPublishRequestToProcess();
        int endSize = bookPublishRequestManager.getRequestQueue().size();

        //THEN
        assertEquals(initialSize - 1, endSize, "Expected the queue size to be less by one.");
        assertEquals(bookPublishRequestOne, result, "Expected the item polled from the queue to be equal to the first item added.");
    }

    @Test
    void getBookPublishRequestToProcess_emptyQueue_returnsNull() {
        //GIVEN
        bookPublishRequestManager = new BookPublishRequestManager();

        //WHEN
        BookPublishRequest result = bookPublishRequestManager.getBookPublishRequestToProcess();

        //THEN
        assertNull(result, "Expected a null result getting an item from an empty queue.");
    }

}
