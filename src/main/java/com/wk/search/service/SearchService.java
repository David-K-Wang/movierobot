package com.wk.search.service;

import java.util.List;

import com.wk.search.form.SearchForm;
import com.wk.search.vo.SearchMovieGroupVo;

public interface SearchService {
    List<SearchMovieGroupVo> search(SearchForm form);
}
