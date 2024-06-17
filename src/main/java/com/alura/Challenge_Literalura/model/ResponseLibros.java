package com.alura.Challenge_Literalura.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseLibros {
    private List<Libro> results;

    public List<Libro> getResults() {
        return results;
    }

    public void setResults(List<Libro> results) {
        this.results = results;
    }
}


