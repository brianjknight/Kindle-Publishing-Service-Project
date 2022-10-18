package com.amazon.ata.kindlepublishingservice.converters;

import com.amazon.ata.coral.converter.CoralConverterUtil;
import com.amazon.ata.kindlepublishingservice.dynamodb.models.PublishingStatusItem;
import com.amazon.ata.kindlepublishingservice.models.PublishingStatusRecord;

import java.util.List;

public class PublishingStatusRecordCoralConverter {

    private PublishingStatusRecordCoralConverter() {}

    /**
     * Converts the given PublishingStatusItem list into corresponding PublishingStatusRecord list.
     * @param publishingStatusItemList list to convert.
     * @return converted PublishingStatusRecord object list.
     */
    public static List<PublishingStatusRecord> toCoral(List<PublishingStatusItem> publishingStatusItemList) {
        return CoralConverterUtil.convertList(publishingStatusItemList, PublishingStatusRecordCoralConverter::toCoral);
    }

    /**
     * Converts a single PublishingStatusItem from DDB table to a PublishingStatusRecord model.
     * @param publishingStatusItem given object to convert.
     * @return Coral PublishingStatusRecord object
     */
    public static PublishingStatusRecord toCoral(PublishingStatusItem publishingStatusItem) {
        return PublishingStatusRecord.builder()
                .withStatusMessage(publishingStatusItem.getStatusMessage())
                .withBookId(publishingStatusItem.getBookId())
                .withStatus(publishingStatusItem.getStatus().toString())
                .build();
    }
}
