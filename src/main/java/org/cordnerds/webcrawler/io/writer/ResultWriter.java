package org.cordnerds.webcrawler.io.writer;

import java.util.Set;

/**
 * @author Amila Karunathilaka
 */
public interface ResultWriter {

    void write(String searchWord, String startUrl, Set<String> resultUrl);
}
