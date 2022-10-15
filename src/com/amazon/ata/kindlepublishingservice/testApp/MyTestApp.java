package com.amazon.ata.kindlepublishingservice.testApp;

import com.amazon.ata.aws.dynamodb.DynamoDbClientProvider;
import com.amazon.ata.kindlepublishingservice.activity.GetBookActivity;
import com.amazon.ata.kindlepublishingservice.activity.RemoveBookFromCatalogActivity;
import com.amazon.ata.kindlepublishingservice.clients.RecommendationsServiceClient;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.models.BookRecommendation;
import com.amazon.ata.kindlepublishingservice.models.requests.GetBookRequest;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetBookResponse;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazon.ata.recommendationsservice.RecommendationsService;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import java.util.List;

public class MyTestApp {
    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
        CatalogDao catalogDao = new CatalogDao(mapper);
        RecommendationsService recommendationsService = new RecommendationsService();
        RecommendationsServiceClient recommendationsServiceClient = new RecommendationsServiceClient(recommendationsService);

        // *****************************************************************************
        // Test to get a book from the database.

//        GetBookRequest getBookRequest = new GetBookRequest();
//        getBookRequest.setBookId("book.69c16130-60b5-485a-8326-7f79d3feb36d");
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
        // *****************************************************************************
        // Test to get a book from the database.

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
        // Test to view order of result from the DDB query scan.

        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId("book.ac510a76-008c-4478-b9f3-c277d74fa305");

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression<CatalogItemVersion>()
                .withHashKeyValues(book)
                .withScanIndexForward(false);


        List<CatalogItemVersion> results = mapper.query(CatalogItemVersion.class, queryExpression);
        System.out.println("-".repeat(100));
        System.out.println("Test to view order of result from the DDB query scan operation. For multiple versions the most recent inactive version is first in the list.");
        for (CatalogItemVersion c : results) {
            System.out.println(c);
        }
        System.out.println("-".repeat(100));

    }
}
