package com.kingco.movierobot.search.vo;

import java.util.List;

import com.kingco.movierobot.search.form.SearchForm;

public class SearchVo {
    private List<SearchMovieGroupVo> movies;
    private SearchForm searchForm;

    public SearchForm getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(SearchForm searchForm) {
        this.searchForm = searchForm;
    }

    public List<SearchMovieGroupVo> getMovies() {
        return movies;
    }

    public void setMovies(List<SearchMovieGroupVo> movies) {
        this.movies = movies;
    }
}
