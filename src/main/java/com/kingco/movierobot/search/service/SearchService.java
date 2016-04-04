package com.kingco.movierobot.search.service;

import java.util.List;

import com.kingco.movierobot.search.form.SearchForm;
import com.kingco.movierobot.search.vo.SearchMovieGroupVo;

public interface SearchService {
    List<SearchMovieGroupVo> search(SearchForm form);
}
