package com.wk.movierobot.dao;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.wk.movierobot.model.MovieTicketInfo;

@Repository("movieTicketInfoDao")
public class MovieTicketInfoDao extends HibernateDaoSupport{

	@Autowired  
    public void setSessionFactoryOverride(SessionFactory sessionFactory)  
    {  
        super.setSessionFactory(sessionFactory);  
    }  
	
	public MovieTicketInfo getMovieTicketInfo(String movieName) {
		List list = getHibernateTemplate().find("from MovieTicketInfo where movieName=?", movieName);
		
		
		
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else {
			return (MovieTicketInfo) list.get(0);
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public List<MovieTicketInfo> getMovieTicketInfo2(String movieName) {
		return (List<MovieTicketInfo>) getHibernateTemplate().executeFind(new HibernateCallback<Object>() {

			@Override
			public List<MovieTicketInfo> doInHibernate(Session s)
					throws HibernateException, SQLException {
				Criteria c = s.createCriteria(MovieTicketInfo.class);
				List list = c.list();
				if (CollectionUtils.isEmpty(list)) {
					return null;
				} else {
					return list;
				}
			}
		});
	}
}
