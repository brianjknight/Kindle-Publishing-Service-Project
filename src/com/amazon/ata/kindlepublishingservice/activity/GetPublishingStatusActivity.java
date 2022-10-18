package com.amazon.ata.kindlepublishingservice.activity;

import com.amazon.ata.kindlepublishingservice.converters.PublishingStatusRecordCoralConverter;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.exceptions.PublishingStatusNotFoundException;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;
import com.amazon.ata.kindlepublishingservice.models.requests.GetPublishingStatusRequest;
import com.amazon.ata.kindlepublishingservice.models.response.GetPublishingStatusResponse;
import com.amazonaws.services.lambda.runtime.Context;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class GetPublishingStatusActivity {
    private PublishingStatusDao publishingStatusDao;

    @Inject
    public GetPublishingStatusActivity(PublishingStatusDao publishingStatusDao) {
        this.publishingStatusDao = publishingStatusDao;
    }

    public GetPublishingStatusResponse execute(GetPublishingStatusRequest publishingStatusRequest) {
        List<PublishingStatusItem> publishingStatusItems = publishingStatusDao.getPublishingStatuses(publishingStatusRequest.getPublishingRecordId());

        if (publishingStatusItems == null || publishingStatusItems.size() == 0) {
            throw new PublishingStatusNotFoundException("No publish status records were found for id: " + publishingStatusRequest.getPublishingRecordId());
        }

        List<PublishingStatusRecord> publishingStatusRecordList = PublishingStatusRecordCoralConverter.toCoral(publishingStatusItems);

        GetPublishingStatusResponse getPublishingStatusResponse = new GetPublishingStatusResponse(publishingStatusRecordList);

        return getPublishingStatusResponse;
    }
}
