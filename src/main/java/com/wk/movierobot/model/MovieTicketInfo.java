package com.wk.movierobot.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "movie_ticket_info")
public class MovieTicketInfo implements Serializable {

    private static final long serialVersionUID = -1687051846278850936L;

    private String id = "";
    private String sourceType = "";
    private String movieName = "";
    private String cinemaName = "";
    private String cityName = "";
    private String bizDistrictName = "";
    private int currentPrice;
    private int originalPrice;
    private String moviePicUrl = "";
    private String sourceUrl = "";
    private String hall = "";
    private String movieDesc = "";
    private Date movieTime = new Date();
    private Date updateTime = new Date();

    @Override
    public String toString() {
        return "MovieTicketInfo [sourceType=" + sourceType + ", movieName=" + movieName + ", cinemaName=" + cinemaName
                + ", cityName=" + cityName + ", bizDistrictName=" + bizDistrictName + ", currentPrice=" + currentPrice
                + ", originalPrice=" + originalPrice + ", moviePicUrl=" + moviePicUrl + ", sourceUrl=" + sourceUrl
                + ", movieTime=" + movieTime + ", updateTime=" + updateTime + "]";
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBizDistrictName() {
        return bizDistrictName;
    }

    public void setBizDistrictName(String bizDistrictName) {
        this.bizDistrictName = bizDistrictName;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(int originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getMoviePicUrl() {
        return moviePicUrl;
    }

    public void setMoviePicUrl(String moviePicUrl) {
        this.moviePicUrl = moviePicUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getMovieTime() {
        return movieTime;
    }

    public void setMovieTime(Date movieTime) {
        this.movieTime = movieTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMovieDesc() {
        return movieDesc;
    }

    public void setMovieDesc(String movieDesc) {
        this.movieDesc = movieDesc;
    }
}
