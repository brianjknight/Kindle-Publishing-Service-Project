package com.amazon.ata.kindlepublishingservice.publishing;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.BookNotFoundException;

import javax.inject.Inject;
import javax.inject.Singleton;

//@Singleton
public final class BookPublishTask implements Runnable{

    BookPublishRequestManager bookPublishRequestManager;
    PublishingStatusDao publishingStatusDao;
    CatalogDao catalogDao;

    /**
     * Constructor. BookPublishTask will use methods in these 3 classes:
     * BookPublishRequestManager, PublishingStatusDao, & CatalogDao.
     * @param bookPublishRequestManager
     * @param publishingStatusDao
     * @param catalogDao
     */
    @Inject
    public BookPublishTask(BookPublishRequestManager bookPublishRequestManager, PublishingStatusDao publishingStatusDao, CatalogDao catalogDao) {
        this.bookPublishRequestManager = bookPublishRequestManager;
        this.publishingStatusDao = publishingStatusDao;
        this.catalogDao = catalogDao;
    }

    /**
     * Tasks to perform when the thread starts.
     */
    @Override
    public void run() {
        //Request to the manager using .poll().
        // If the queue is empty it returns null and run immediately returns with no action taken.
        BookPublishRequest request = bookPublishRequestManager.getBookPublishRequestToProcess();
        if (request == null) {
            return;
        }

        publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(),
                PublishingRecordStatus.IN_PROGRESS,
                request.getBookId());

        KindleFormattedBook kindleBook = KindleFormatConverter.format(request);

        try {
            CatalogItemVersion newBookVersion = catalogDao.createOrUpdateBook(kindleBook);

            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.SUCCESSFUL, newBookVersion.getBookId());
        }
        catch (BookNotFoundException e) {
            publishingStatusDao.setPublishingStatus(request.getPublishingRecordId(), PublishingRecordStatus.FAILED, request.getBookId(), e.getMessage());
        }

    }
}
