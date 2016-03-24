package com.kingco.movierobot.crawl.dto;

import org.apache.commons.lang3.StringUtils;

public class CinemaDto {
    private String cinemaId;
    private String cinemaName;
    private String cimemaPhoneNum;
    private String cimemaAddress;

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCimemaPhoneNum() {
        return cimemaPhoneNum;
    }

    public void setCimemaPhoneNum(String cimemaPhoneNum) {
        this.cimemaPhoneNum = cimemaPhoneNum;
    }

    public String getCimemaAddress() {
        return cimemaAddress;
    }

    public void setCimemaAddress(String cimemaAddress) {
        this.cimemaAddress = cimemaAddress;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(this.getCinemaId()) && StringUtils.isNotEmpty(this.getCinemaName());
    }
}
