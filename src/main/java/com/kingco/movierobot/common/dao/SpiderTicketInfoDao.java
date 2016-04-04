package com.kingco.movierobot.common.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.kingco.movierobot.common.bo.MovieTicketInfo;

@Repository("spiderTicketInfoDao")
public class SpiderTicketInfoDao extends HibernateDaoSupport {

    @Autowired
    public void setSessionFactoryOverride(SessionFactory sessionFactory) {
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
            public List<MovieTicketInfo> doInHibernate(Session s) throws HibernateException, SQLException {
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

    public void saveMovieTicketInfo(final MovieTicketInfo info) {
        getHibernateTemplate().execute(new HibernateCallback<Object>() {

            @Override
            public Integer doInHibernate(Session s) throws HibernateException, SQLException {
                info.setId(info.getSourceType() + "_" + info.getCinemaName() + "_" + info.getHall() + "_"
                        + info.getMovieTime().getTime());
                getHibernateTemplate().merge(info);
                return 1;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public List<MovieTicketInfo> queryMovieTicketInfo(final Date fromDate, final Date toDate, final String movieName,
            final String cinemaName) {
        return (List<MovieTicketInfo>) getHibernateTemplate().executeFind(new HibernateCallback<Object>() {

            @Override
            public List<MovieTicketInfo> doInHibernate(Session s) throws HibernateException, SQLException {
                Criteria c = s.createCriteria(MovieTicketInfo.class);
                if (StringUtils.isNotEmpty(movieName)) {
                    c.add(Restrictions.like("movieName", "%" + movieName + "%"));
                }

                if (StringUtils.isNotEmpty(cinemaName)) {
                    c.add(Restrictions.like("cinemaName", "%" + cinemaName + "%"));
                }

                if (fromDate != null) {
                    c.add(Restrictions.gt("movieTime", fromDate));
                }

                if (toDate != null) {
                    c.add(Restrictions.lt("movieTime", toDate));
                }
                c.addOrder(Order.asc("movieName"));
                c.addOrder(Order.asc("cinemaName"));
                c.addOrder(Order.asc("currentPrice"));

                List<MovieTicketInfo> list = c.list();
                if (CollectionUtils.isEmpty(list)) {
                    return null;
                } else {
                    return list;
                }
            }
        });
    }
}
