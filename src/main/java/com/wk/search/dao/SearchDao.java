package com.wk.search.dao;

import java.util.Date;
import java.util.List;

import com.wk.movierobot.model.MovieTicketInfo;

public interface SearchDao {

    List<MovieTicketInfo> queryMovieTicketInfo(Date fromDate, Date toDate, String movieName, String cinemaName);
}
