package com.kingco.movierobot.search.form;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SearchForm {
    private Date fromDate;
    private Date toDate;
    private String movieName;
    private String cinemaName;

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getFromDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.format(getFromDate());
        } catch (Exception e) {
            return "";
        }
    }

    public String getToDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return sdf.format(getToDate());
        } catch (Exception e) {
            return "";
        }
    }
}
