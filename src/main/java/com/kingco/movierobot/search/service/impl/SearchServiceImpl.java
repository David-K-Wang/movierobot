package com.kingco.movierobot.search.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kingco.movierobot.common.bo.MovieTicketInfo;
import com.kingco.movierobot.common.dao.SpiderTicketInfoDao;
import com.kingco.movierobot.search.form.SearchForm;
import com.kingco.movierobot.search.service.SearchService;
import com.kingco.movierobot.search.vo.SearchMovieGroupVo;
import com.kingco.movierobot.search.vo.SearchTicketVo;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SpiderTicketInfoDao searchDao;

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
