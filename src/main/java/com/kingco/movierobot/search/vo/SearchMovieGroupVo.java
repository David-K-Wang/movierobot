package com.kingco.movierobot.search.vo;

import java.util.List;

public class SearchMovieGroupVo {
    private String movieName;
    private String moviePicUrl;
    private String movieDesc;

    public String getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }

    private List<SearchTicketVo> ticketList;

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMoviePicUrl() {
        return moviePicUrl;
    }

    public void setMoviePicUrl(String moviePicUrl) {
        this.moviePicUrl = moviePicUrl;
    }

    public List<SearchTicketVo> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<SearchTicketVo> ticketList) {
        this.ticketList = ticketList;
    }
}
