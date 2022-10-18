package com.amazon.ata.kindlepublishingservice.dao;

import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.CatalogItemVersion;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;

import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class PublishingStatusDaoTest {

    @Mock
    private DynamoDBMapper dynamoDbMapper;

    @Mock
    private PaginatedQueryList<PublishingStatusItem> list;

    @InjectMocks
    private PublishingStatusDao publishingStatusDao;

    @BeforeEach
    public void setup(){
        initMocks(this);
    }

    @Test
    public void setPublishingStatus2_successful_bookIdPresent() {
        // GIVEN
        String publishingId = "publishingstatus.123";
        String bookId = "book.123";

        // WHEN
        PublishingStatusItem status = publishingStatusDao.setPublishingStatus(publishingId,
                PublishingRecordStatus.SUCCESSFUL, bookId);

        // THEN
        verify(dynamoDbMapper).save(any(PublishingStatusItem.class));
        assertEquals(publishingId, status.getPublishingRecordId(), "Expected saved status to have the " +
                "correct publishing status id.");
        assertEquals(PublishingRecordStatus.SUCCESSFUL, status.getStatus(), "Expected saved status to have" +
                " the correct publishing status.");
        assertNotNull(status.getBookId(), "BookId should be present for successfully published book.");
        assertNotNull(status.getStatusMessage() , "Each status record should have a message.");
    }

    @Test
    public void setPublishingStatus2_queued_statusSaved() {
        // GIVEN
        String publishingId = "publishingstatus.123";

        // WHEN
        PublishingStatusItem status = publishingStatusDao.setPublishingStatus(publishingId,
                PublishingRecordStatus.QUEUED, null);

        // THEN
        verify(dynamoDbMapper).save(any(PublishingStatusItem.class));
        assertEquals(publishingId, status.getPublishingRecordId(), "Expected saved status to have the " +
                "correct publishing status id.");
        assertEquals(PublishingRecordStatus.QUEUED, status.getStatus(), "Expected saved status to have" +
                " the correct publishing status.");
        assertNotNull(status.getStatusMessage() , "Each status record should have a message.");
        assertNull(status.getBookId(), "Expected bookId to be null in the status, when a bookId is not provided.");
    }

    @Test
    public void setPublishingStatus2_additionalMessage_statusSaved() {
        // GIVEN
        String publishingId = "publishingstatus.123";
        String bookId = "book.123";

        // WHEN
        PublishingStatusItem status = publishingStatusDao.setPublishingStatus(publishingId,
                PublishingRecordStatus.FAILED, bookId, "Failed due to...");

        // THEN
        verify(dynamoDbMapper).save(any(PublishingStatusItem.class));
        assertEquals(publishingId, status.getPublishingRecordId(), "Expected saved status to have the " +
                "correct publishing status id.");
        assertEquals(PublishingRecordStatus.FAILED, status.getStatus(), "Expected saved status to have" +
                " the correct publishing status.");
        assertNotNull(status.getStatusMessage() , "Each status record should have a message.");
        assertTrue(status.getStatusMessage().contains("Additional Notes"), "If a message is provided it should be" +
                "included in the status message as 'Additional Notes'");
        assertTrue(status.getStatusMessage().contains("Failed due to..."), "If a message is provided it should be" +
                "included in the status message.");
    }

    @Test
    public void getPublishingStatuses_noItemsForStatusId_returnsNullList() {
        //GIVEN
        String publishingId = "publishingstatus.123";
        ArgumentCaptor<DynamoDBQueryExpression> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        when(dynamoDbMapper.query(eq(PublishingStatusItem.class),any(DynamoDBQueryExpression.class))).thenReturn(list);

        //WHEN
        List<PublishingStatusItem> result = publishingStatusDao.getPublishingStatuses(publishingId);

        //THEN
        assertTrue(result.size() == 0, "Expected an empty list for publishingId with not items in table.");

        verify(dynamoDbMapper).query(eq(PublishingStatusItem.class), captor.capture());
        PublishingStatusItem queriedItem = (PublishingStatusItem) captor.getValue().getHashKeyValues();
        assertEquals(publishingId, queriedItem.getPublishingRecordId());
    }

    @Test
    public void getPublishingStatuses_twoItemsForSameId_returnListTwoItems() {
        //GIVEN
        ArgumentCaptor<DynamoDBQueryExpression> captor = ArgumentCaptor.forClass(DynamoDBQueryExpression.class);
        String publishingId = "publishingstatus.123";
        String bookId = "book.123";

        PublishingStatusItem itemOne = new PublishingStatusItem();
        itemOne.setPublishingRecordId(publishingId);
        itemOne.setBookId(bookId);
        itemOne.setStatus(PublishingRecordStatus.QUEUED);

        PublishingStatusItem itemTwo = new PublishingStatusItem();
        itemTwo.setPublishingRecordId(publishingId);
        itemTwo.setBookId(bookId);
        itemTwo.setStatus(PublishingRecordStatus.IN_PROGRESS);


        when(dynamoDbMapper.query(eq(PublishingStatusItem.class), any(DynamoDBQueryExpression.class))).thenReturn(list);
        when(list.get(0)).thenReturn(itemOne);
        when(list.get(1)).thenReturn(itemTwo);
        when(list.size()).thenReturn(2);

        //WHEN
        List<PublishingStatusItem> result = publishingStatusDao.getPublishingStatuses(publishingId);

        //THEN
        assertTrue(result.size() == 2, "Expected the same list size to be returned.");
        assertEquals(result.get(0).getPublishingRecordId(), publishingId,"Expected the first items from the list to have the same publishing Id." );
        assertEquals(result.get(1).getPublishingRecordId(), publishingId,"Expected the second items from the list to have the same publishing Id." );
        assertNotEquals(result.get(0).getStatus(), result.get(1).getStatus(), "Expected different the partitions keys/PublishingRecordStatus to be different.");

        verify(dynamoDbMapper).query(eq(PublishingStatusItem.class), captor.capture());
        PublishingStatusItem queriedItem = (PublishingStatusItem) captor.getValue().getHashKeyValues();
        assertEquals(publishingId, queriedItem.getPublishingRecordId(), "Expected the queried item to have matching publishingId.");
    }
}
