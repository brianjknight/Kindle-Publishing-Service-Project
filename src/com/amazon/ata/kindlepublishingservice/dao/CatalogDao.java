package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.Book;
import com.amazon.ata.kindlepublishingservice.publishing.KindleFormattedBook;
import com.amazon.ata.kindlepublishingservice.utils.KindlePublishingUtils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.inject.Inject;

public class CatalogDao {

    private final DynamoDBMapper dynamoDbMapper;

    /**
     * Instantiates a new CatalogDao object.
     *
     * @param dynamoDbMapper The {@link DynamoDBMapper} used to interact with the catalog table.
     */
    @Inject
    public CatalogDao(DynamoDBMapper dynamoDbMapper) {
        this.dynamoDbMapper = dynamoDbMapper;
    }

    /**
     * Returns the latest version of the book from the catalog corresponding to the specified book id.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion getBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        return book;
    }

    /**
     * Does a soft delete of a book from the catalog database by setting the 'inactive' attribute to true.
     * Throws a BookNotFoundException if the latest version is not active or no version is found.
     * @param bookId Id associated with the book.
     * @return The corresponding CatalogItem from the catalog table.
     */
    public CatalogItemVersion removeBookFromCatalog(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null || book.isInactive()) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }

        book.setInactive(true);
        dynamoDbMapper.save(book);

        return book;
    }

    /**
     * Validating if a given bookId exists whether it is inactive OR active.
     * @param bookId id to check table for.
     */
    public void validateBookExists(String bookId) {
        CatalogItemVersion book = getLatestVersionOfBook(bookId);

        if (book == null) {
            throw new BookNotFoundException(String.format("No book found for id: %s", bookId));
        }
    }

    public CatalogItemVersion createOrUpdateBook(KindleFormattedBook kindleFormattedBook) {

        //first determine if we are adding a new book or updating an existing book
        CatalogItemVersion latestVersionOfBook = getLatestVersionOfBook(kindleFormattedBook.getBookId());

        CatalogItemVersion newVersion = new CatalogItemVersion();

        if (latestVersionOfBook != null) {
            int latestVersion = latestVersionOfBook.getVersion();
            latestVersionOfBook.setInactive(true);
            dynamoDbMapper.save(latestVersionOfBook);

            newVersion.setBookId(kindleFormattedBook.getBookId());
            newVersion.setVersion(latestVersion + 1);
            newVersion.setInactive(false);
            newVersion.setTitle(kindleFormattedBook.getTitle());
            newVersion.setAuthor(kindleFormattedBook.getAuthor());
            newVersion.setText(kindleFormattedBook.getText());
            newVersion.setGenre(kindleFormattedBook.getGenre());
            dynamoDbMapper.save(newVersion);

//            return newVersion;
        }
        else {
            String bookId = KindlePublishingUtils.generateBookId();
            newVersion.setBookId(bookId);
            newVersion.setVersion(1);
            newVersion.setInactive(false);
            newVersion.setTitle(kindleFormattedBook.getTitle());
            newVersion.setAuthor(kindleFormattedBook.getAuthor());
            newVersion.setText(kindleFormattedBook.getText());
            newVersion.setGenre(kindleFormattedBook.getGenre());
            dynamoDbMapper.save(newVersion);
        }

        return newVersion;
    }

    // Returns null if no version exists for the provided bookId
    private CatalogItemVersion getLatestVersionOfBook(String bookId) {
        if (bookId == null) {
            return null;
        }

        CatalogItemVersion book = new CatalogItemVersion();
        book.setBookId(bookId);

        DynamoDBQueryExpression<CatalogItemVersion> queryExpression = new DynamoDBQueryExpression<CatalogItemVersion>()
            .withHashKeyValues(book)
            .withScanIndexForward(false)
            .withLimit(1);

        List<CatalogItemVersion> results = dynamoDbMapper.query(CatalogItemVersion.class, queryExpression);
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }
}
