package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.enums.PublishingRecordStatus;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatus;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class GetPublishingStatusActivityTest {

    @Mock
    PublishingStatusDao publishingStatusDao;

    @InjectMocks
    GetPublishingStatusActivity getPublishingStatusActivity;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void execute_publishingRecordIdDoesNotExistNullList_throwsException() {
        //GIVEN
        String publishingRecordId = "publishingRecordId.123";
        GetPublishingStatusRequest getPublishingStatusRequest = new GetPublishingStatusRequest(publishingRecordId);
        when(publishingStatusDao.getPublishingStatuses(publishingRecordId)).thenReturn(null);

        //WHEN THEN
        assertThrows(PublishingStatusNotFoundException.class, () -> getPublishingStatusActivity.execute(getPublishingStatusRequest));
    }

    @Test
    public void execute_publishingRecordIdDoesNotExistEmptyList_throwsException() {
        //GIVEN
        String publishingRecordId = "publishingRecordId.123";
        GetPublishingStatusRequest getPublishingStatusRequest = new GetPublishingStatusRequest(publishingRecordId);
        List<PublishingStatusItem> publishingStatusItemList = new ArrayList<>();
        when(publishingStatusDao.getPublishingStatuses(publishingRecordId)).thenReturn(publishingStatusItemList);

        //WHEN THEN
        assertThrows(PublishingStatusNotFoundException.class, () -> getPublishingStatusActivity.execute(getPublishingStatusRequest));
    }

    @Test
    public void execute_publishingRecordIdExists_returnsGetPublishingStatusResponse() {
        //GIVEN
        String publishingRecordId = "publishingRecordId.123";
        String bookId = "book.123";
        String statusMessage = "test message";

        PublishingStatusItem item = new PublishingStatusItem();
        item.setPublishingRecordId(publishingRecordId);
        item.setBookId(bookId);
        item.setStatusMessage(statusMessage);
        item.setStatus(PublishingRecordStatus.QUEUED);
        GetPublishingStatusRequest getPublishingStatusRequest = new GetPublishingStatusRequest(publishingRecordId);
        List<PublishingStatusItem> publishingStatusItemList = new ArrayList<>(Arrays.asList(item));


        PublishingStatusRecord publishingStatusRecord = PublishingStatusRecord.builder()
                .withStatus(PublishingRecordStatus.QUEUED.toString())
                .withBookId(bookId)
                .withStatusMessage(statusMessage)
                .build();

        List<PublishingStatusRecord> publishingStatusRecordList = new ArrayList<>(Arrays.asList(publishingStatusRecord));
        GetPublishingStatusResponse expectedResponse = new GetPublishingStatusResponse(publishingStatusRecordList);

        when(publishingStatusDao.getPublishingStatuses(any())).thenReturn(publishingStatusItemList);
        //WHEN
        GetPublishingStatusResponse actualResponse = getPublishingStatusActivity.execute(getPublishingStatusRequest);

        //THEN
        assertEquals(expectedResponse, actualResponse, "Expected the GetPublishingStatusResponse to be equal.");
    }

}
