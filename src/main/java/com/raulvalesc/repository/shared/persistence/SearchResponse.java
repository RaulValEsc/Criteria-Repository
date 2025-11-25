package com.raulvalesc.repository.shared.persistence;

import java.util.List;

public class SearchResponse<T> {
    public int total;

    public List<T> results;

    public SearchResponse(int total, List<T> results) {
        this.total = total;

        this.results = results;
    }
}
