package org.woehlke.simpleworklist.services;

import org.woehlke.simpleworklist.model.SearchResult;

/**
 * Created by tw on 14.02.16.
 */
public interface SearchService {
    SearchResult search(String searchterm);

    void resetSearchIndex();

}