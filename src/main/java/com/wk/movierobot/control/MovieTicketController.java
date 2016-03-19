package com.wk.movierobot.control;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.wk.movierobot.dao.MovieTicketInfoDao;
import com.wk.movierobot.model.MovieTicketInfo;

@Controller
public class MovieTicketController {
	
	@Autowired
	private MovieTicketInfoDao movieTicketDao;

	@RequestMapping(value = "/movie", method = RequestMethod.GET)
	public void hello(HttpServletRequest request, HttpServletResponse response) {
		List<MovieTicketInfo> info = movieTicketDao.getMovieTicketInfo2("美人鱼");
		
		
		
		response.setContentType("text/plain;charset=utf-8");
        response.setHeader("Content-Encoding", "gzip");
        GZIPOutputStream out;
        try {
            out = new GZIPOutputStream(response.getOutputStream());
            out.write(info.toString().getBytes());
            out.finish();
            out.flush();
        } catch (IOException e) {

        }
	}
}
