package com.wk.movierobot.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "movie_ticket_info")
public class MovieTicketInfo implements Serializable{

	private static final long serialVersionUID = -1687051846278850936L;
		
	private long id;
	private String movieName;

	@Override
	public String toString() {
		return "MovieTicketInfo [id=" + id + ", movieName=" + movieName + "]";
	}

	public String getMovieName() {
		return movieName;
	}

	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}

	@Id
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
}
