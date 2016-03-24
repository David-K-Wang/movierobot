package com.kingco.movierobot.crawl.dto;

import org.apache.commons.lang.StringUtils;

public class FilmDto {

    private String filmId;
    private String filmName;
    private String filmDesc;
    private String filmPicUrl;

    public String getFilmId() {
        return filmId;
    }

    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getFilmDesc() {
        return filmDesc;
    }

    public void setFilmDesc(String filmDesc) {
        this.filmDesc = filmDesc;
    }

    public String getFilmPicUrl() {
        return filmPicUrl;
    }

    public void setFilmPicUrl(String filmPicUrl) {
        this.filmPicUrl = filmPicUrl;
    }

    public boolean isValid() {
        return StringUtils.isNotEmpty(this.getFilmId()) && StringUtils.isNotEmpty(this.getFilmName());
    }
}
