package com.amazon.ata.kindlepublishingservice.testApp;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.kindlepublishingservice.activity.GetBookActivity;
import com.amazon.ata.kindlepublishingservice.activity.GetPublishingStatusActivity;
import com.amazon.ata.kindlepublishingservice.activity.RemoveBookFromCatalogActivity;
import com.amazon.ata.kindlepublishingservice.activity.SubmitBookForPublishingActivity;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationsServiceClient;
import com.amazon.ata.kindlepublishingservice.converters.PublishingStatusRecordCoralConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.models.BookRecommendation;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.SubmitBookForPublishingRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.kindlepublishingservice.models.response.SubmitBookForPublishingResponse;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequest;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MyTestApp {
    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
        CatalogDao catalogDao = new CatalogDao(mapper);
        PublishingStatusDao publishingStatusDao = new PublishingStatusDao(mapper);
        RecommendationsService recommendationsService = new RecommendationsService();
        RecommendationsServiceClient recommendationsServiceClient = new RecommendationsServiceClient(recommendationsService);
        BookPublishRequestManager bookPublishRequestManager = new BookPublishRequestManager();

        //---------------------------------------------------------------------------------------------
        // Test to get a book from the database.

//        GetBookRequest getBookRequest = new GetBookRequest();
//        getBookRequest.setBookId("book.b3750190-2a30-4ca8-ae1b-73d0d202dc41");
//
//        GetBookActivity getBookActivity = new GetBookActivity(catalogDao, recommendationsServiceClient);
//        GetBookResponse getBookResponse = getBookActivity.execute(getBookRequest);
//        System.out.println("-".repeat(100));
//        System.out.println("The book is: \n\t" + getBookResponse.getBook());
//        List<BookRecommendation> recommendations = getBookResponse.getRecommendations();
//        System.out.println("Recommendations for " + getBookResponse.getBook().getTitle() + " are:");
//        for (BookRecommendation br : recommendations) {
//            System.out.println("\t" + br);
//        }
//        System.out.println("-".repeat(100));
        //---------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------
        // Test to remove a book from the database.
//        RemoveBookFromCatalogRequest removeBookFromCatalogRequest = new RemoveBookFromCatalogRequest();
//        removeBookFromCatalogRequest.setBookId("book.ac510a76-008c-4478-b9f3-c277d74fa305");
//
//        RemoveBookFromCatalogActivity removeBookFromCatalogActivity = new RemoveBookFromCatalogActivity(catalogDao);
//        RemoveBookFromCatalogResponse removeBookFromCatalogResponse = removeBookFromCatalogActivity.execute(removeBookFromCatalogRequest);
//        System.out.println("-".repeat(100));
//        System.out.println("Return from removeBookFromCatalogActivity.execute(removeBookFromCatalogRequest):");
//        System.out.println("\t" + removeBookFromCatalogResponse);
//
//        System.out.println("-".repeat(100));
        //---------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------
        // Test to view order of result from the DDB query scan.

//        CatalogItemVersion book = new CatalogItemVersion();
//        book.setBookId("book.ac510a76-008c-4478-b9f3-c277d74fa305");
//
//        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression<CatalogItemVersion>()
//                .withHashKeyValues(book)
//                .withScanIndexForward(false);
//
//
//        List<CatalogItemVersion> results = mapper.query(CatalogItemVersion.class, queryExpression);
//        System.out.println("-".repeat(100));
//        System.out.println("Test to view order of result from the DDB query scan operation. For multiple versions the most recent inactive version is first in the list.");
//        for (CatalogItemVersion c : results) {
//            System.out.println(c);
//        }
//        System.out.println("-".repeat(100));
        //---------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------
        // Test SubmitBookForPublishingActivity
//        Queue<BookPublishRequest> requestQueue = new LinkedList<>();
//        BookPublishRequestManager bookPublishRequestManager = new BookPublishRequestManager(requestQueue);
//        SubmitBookForPublishingActivity submitBookForPublishingActivity = new SubmitBookForPublishingActivity(publishingStatusDao, catalogDao, bookPublishRequestManager);
//
//        // submit a publishing request for a  new book with no bookId
//        SubmitBookForPublishingRequest submitRequestNewBook = SubmitBookForPublishingRequest.builder()
//                .withAuthor("Author 1")
//                .withGenre("FANTASY")
//                .withText("Epilogue: ........")
//                .withTitle("Stories of an AI")
//                .build();
//
//        SubmitBookForPublishingResponse resultNewBook = submitBookForPublishingActivity.execute(submitRequestNewBook);
//
//        // submit a publishing request for an existing ACTIVE book
//        SubmitBookForPublishingRequest submitRequestActiveBook = SubmitBookForPublishingRequest.builder()
//                .withAuthor("Author 1")
//                .withGenre("FANTASY")
//                .withText("Epilogue: ........")
//                .withTitle("Stories of an AI")
//                .build();
//
//        SubmitBookForPublishingResponse resultActiveBook = submitBookForPublishingActivity.execute(submitRequestActiveBook);
//
//        // submit a publishing request for an existing INACTIVE book
//        SubmitBookForPublishingRequest submitRequestInactiveBook = SubmitBookForPublishingRequest.builder()
//                .withAuthor("Brian Knight")
//                .withGenre("FANTASY")
//                .withText("Once upon a time in a land far, far away...")
//                .withTitle("Confessions of a Computer Programmer")
//                .build();
//
//        SubmitBookForPublishingResponse resultInactiveBook = submitBookForPublishingActivity.execute(submitRequestInactiveBook);
//
//        System.out.println("resultNewBook: \n\t" + resultNewBook);
//        for (BookPublishRequest request : requestQueue) {
//            System.out.println(request);
//        }
//        System.out.println("-".repeat(100));
//
//        System.out.println("resultActiveBook: \n\t" + resultActiveBook);
//        for (BookPublishRequest request : requestQueue) {
//            System.out.println(request);
//        }
//        System.out.println("-".repeat(100));
//
//        System.out.println("resultInactiveBook: \n\t" + resultInactiveBook);
//        for (BookPublishRequest request : requestQueue) {
//            System.out.println(request);
//        }
//        System.out.println("-".repeat(100));
        //---------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------
        // Test PublishingStatusRecordCoralConverter
//        PublishingStatusItem item1_1 = new PublishingStatusItem();
//        item1_1.setPublishingRecordId("publish.item1_1");
//        item1_1.setStatus(PublishingRecordStatus.QUEUED);
//        item1_1.setBookId("book1");
//        item1_1.setStatusMessage("publish submitted");
//
//        PublishingStatusItem item1_2 = new PublishingStatusItem();
//        item1_2.setPublishingRecordId("publish.item1_2");
//        item1_2.setStatus(PublishingRecordStatus.IN_PROGRESS);
//        item1_2.setBookId("book1");
//        item1_2.setStatusMessage("publishing is in progress");
//
//        PublishingStatusItem item1_3 = new PublishingStatusItem();
//        item1_3.setPublishingRecordId("publish.item1_3");
//        item1_3.setStatus(PublishingRecordStatus.SUCCESSFUL);
//        item1_3.setBookId("book1");
//        item1_3.setStatusMessage("publish successful");
//
//        List<PublishingStatusItem> publishingStatusItemList = new ArrayList<>();
//        publishingStatusItemList.add(item1_1);
//        publishingStatusItemList.add(item1_2);
//        publishingStatusItemList.add(item1_3);
//
//        List<PublishingStatusRecord> publishingRecordStatusList = PublishingStatusRecordCoralConverter.toCoral(publishingStatusItemList);
//
//        System.out.println("Print out publishingStatusItemList: ");
//        for (PublishingStatusItem item : publishingStatusItemList) {
//            System.out.println(item);
//        }
//        System.out.println("-".repeat(100));
//        System.out.println("Print out publishingRecordStatusList: ");
//        for (PublishingStatusRecord record : publishingRecordStatusList) {
//            System.out.println(record);
//        }

        //---------------------------------------------------------------------------------------------


        //---------------------------------------------------------------------------------------------
        //Test PublishingStatusDao getPublishingStatuses


        //---------------------------------------------------------------------------------------------

        //---------------------------------------------------------------------------------------------
        //Test MT4 implementing Runnable Interface
        // To test, submit a book publish request by calling SubmitBookForPublishing.
        // You should then get back a publishingRecordId, which you’ll use to check the status by calling GetPublishingStatus.

//        SubmitBookForPublishingActivity submitBookForPublishingActivity = new SubmitBookForPublishingActivity(
//                                                                                    publishingStatusDao,
//                                                                                    catalogDao,
//                                                                                    bookPublishRequestManager);
//
//        SubmitBookForPublishingRequest submitRequestNewVersion = SubmitBookForPublishingRequest.builder()
//                .withBookId("book.02468")
//                .withTitle("Title of My Book (2nd Edition)")
//                .withAuthor("Brian Knight")
//                .withText("This is a work in progress....more text for version 2.....")
//                .withGenre("FANTASY")
//                .build();
//
//        SubmitBookForPublishingResponse response = submitBookForPublishingActivity.execute(submitRequestNewVersion);
//        System.out.println("SubmitBooForPublishingResponse >> response.getPublishRecordId() = " + response.getPublishingRecordId());
            //Successfully creates item in DDB table with status QUEUED.

        //After submit activity call to PublishingStatusDao getPublishingStatuses
//        List<PublishingStatusItem> publishingStatusItemList = publishingStatusDao.getPublishingStatuses("publishingstatus.fedb19c1-5c28-461d-b1c7-b0c7d6538ca4");
//        for (PublishingStatusItem item : publishingStatusItemList) {
//            System.out.println(item);
//        }
            //That works after submitting publish request

//        publishingStatusDao.setPublishingStatus("publishingstatus.fedb19c1-5c28-461d-b1c7-b0c7d6538ca4",
//                PublishingRecordStatus.IN_PROGRESS, "book.02468");
            //successfully adds a new publishingRecordItem; new ID with updated status.

        GetPublishingStatusActivity getPublishingStatusActivity = new GetPublishingStatusActivity(publishingStatusDao);
        GetPublishingStatusRequest request = GetPublishingStatusRequest.builder()
                .withPublishingRecordId("publishingstatus.fedb19c1-5c28-461d-b1c7-b0c7d6538ca4")
                .build();
        GetPublishingStatusResponse response = getPublishingStatusActivity.execute(request);
        System.out.println(response);

        //---------------------------------------------------------------------------------------------

    }
}
