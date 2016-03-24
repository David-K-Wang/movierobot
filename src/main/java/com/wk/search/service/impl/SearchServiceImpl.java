package com.wk.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wk.movierobot.model.MovieTicketInfo;
import com.wk.search.dao.SearchDao;
import com.wk.search.form.SearchForm;
import com.wk.search.service.SearchService;
import com.wk.search.vo.SearchMovieGroupVo;
import com.wk.search.vo.SearchTicketVo;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao searchDao;

    @Override
    public List<SearchMovieGroupVo> search(SearchForm form) {
        List<SearchMovieGroupVo> movieGroupVos = new ArrayList<SearchMovieGroupVo>();
        List<MovieTicketInfo> tickets = searchDao.queryMovieTicketInfo(form.getFromDate(), form.getToDate(),
                form.getMovieName(), form.getCinemaName());
        if (CollectionUtils.isNotEmpty(tickets)) {
            String lastMovieName = "";
            List<SearchTicketVo> movieTickets = null;
            for (MovieTicketInfo info : tickets) {
                if (!info.getMovieName().equals(lastMovieName)) {
                    movieTickets = new ArrayList<SearchTicketVo>();
                    SearchMovieGroupVo movieGroup = new SearchMovieGroupVo();
                    movieGroupVos.add(movieGroup);
                    movieGroup.setMovieName(info.getMovieName());
                    movieGroup.setMoviePicUrl(info.getMoviePicUrl());
                    movieGroup.setMovieDesc(info.getMovieDesc());
                    movieGroup.setTicketList(movieTickets);
                }
                SearchTicketVo vo = new SearchTicketVo();
                vo.setTime(info.getMovieTime());
                vo.setMovieName(info.getMovieName());
                vo.setCinemaName(info.getCinemaName());
                vo.setCinemaHall(info.getHall());
                vo.setPrice(info.getCurrentPrice());
                vo.setOriginalPrice(info.getOriginalPrice());
                vo.setSourceUrl(info.getSourceUrl());
                vo.setSourceType(info.getSourceType());
                movieTickets.add(vo);
                lastMovieName = info.getMovieName();
            }
        }
        return movieGroupVos;
    }
}
