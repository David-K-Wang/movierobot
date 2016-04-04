package com.kingco.movierobot.search.control;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kingco.movierobot.common.util.TextUtil;
import com.kingco.movierobot.search.form.SearchForm;
import com.kingco.movierobot.search.service.SearchService;
import com.kingco.movierobot.search.vo.SearchMovieGroupVo;
import com.kingco.movierobot.search.vo.SearchVo;

@Controller
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search")
    public String test(HttpServletRequest request, Map<String, Object> model) {
        SearchForm form = new SearchForm();
        form.setFromDate(parseDate(request.getParameter("fromDate")));
        if (form.getFromDate() == null || form.getFromDate().before(new Date())) {
            form.setFromDate(new Date());
        }
        form.setToDate(parseDate(request.getParameter("toDate")));
        form.setCinemaName(parseUtf8(request.getParameter("cinemaName")));
        form.setMovieName(parseUtf8(request.getParameter("movieName")));
        List<SearchMovieGroupVo> vos = this.searchService.search(form);
        SearchVo result = new SearchVo();
        result.setMovies(vos);
        result.setSearchForm(form);
        model.put("result", result);
        return "search";
    }

    private Date parseDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return sdf.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }

    private String parseUtf8(String text) {
        return text != null ? text = TextUtil.iso8859ToUtf8(text) : "";
    }
}
