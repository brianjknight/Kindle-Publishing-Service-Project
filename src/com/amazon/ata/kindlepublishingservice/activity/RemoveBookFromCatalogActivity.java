package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.CatalogItemConverter;
import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.models.requests.RemoveBookFromCatalogRequest;
import com.amazon.ata.kindlepublishingservice.models.response.RemoveBookFromCatalogResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;

public class RemoveBookFromCatalogActivity {
    CatalogDao catalogDao;

    @Inject
    public RemoveBookFromCatalogActivity(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    /**
     * Does a soft delete of the Book associated with the provided bookId setting its inactive attribute to true.
     *
     * @param removeBookFromCatalogRequest request object containing the bookId to "remove".
     * @return response object
     */
    public RemoveBookFromCatalogResponse execute(RemoveBookFromCatalogRequest removeBookFromCatalogRequest) {

        CatalogItemVersion catalogItem = catalogDao.removeBookFromCatalog(removeBookFromCatalogRequest.getBookId());

        return RemoveBookFromCatalogResponse.builder()
                .withBook(CatalogItemConverter.toBook(catalogItem))
                .build();
    }
}
