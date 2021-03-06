package org.mage.plugins.card.dl.sources;

import mage.client.util.CardLanguage;
import org.mage.plugins.card.dl.DownloadServiceInfo;
import org.mage.plugins.card.images.CardDownloadData;

import java.util.ArrayList;
import java.util.List;

/**
 * @author North, JayDi85
 */
public interface CardImageSource {

    CardImageUrls generateCardUrl(CardDownloadData card) throws Exception;

    CardImageUrls generateTokenUrl(CardDownloadData card) throws Exception;

    boolean prepareDownloadList(DownloadServiceInfo downloadServiceInfo, List<CardDownloadData> downloadList);

    String getNextHttpImageUrl();

    String getFileForHttpImage(String httpImageUrl);

    String getSourceName();

    float getAverageSize();

    int getTotalImages();

    default int getTokenImages() {
        return 0;
    }

    default boolean isTokenSource() {
        return false;
    }

    default boolean isLanguagesSupport() {
        return false;
    }

    default void setCurrentLanguage(CardLanguage cardLanguage) {
    }

    default CardLanguage getCurrentLanguage() {
        return CardLanguage.ENGLISH;
    }

    void doPause(String httpImageUrl);

    default List<String> getSupportedSets() {
        return new ArrayList<>();
    }

    default boolean isSetSupportedComplete(String setCode) {
        return true;
    }

    default boolean isCardImageProvided(String setCode, String cardName) {
        return false;
    }

    default boolean isTokenImageProvided(String setCode, String cardName, Integer tokenNumber) {
        return false;
    }
}
