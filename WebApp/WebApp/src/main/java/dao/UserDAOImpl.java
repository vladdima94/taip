package dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import model.User;

@Repository
public class UserDAOImpl implements IUserDAO{

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public User get(Long id) {
		return getCurrentSession().get(User.class, id);
	}

	@Override
	public User create(User entity) {
		getCurrentSession().save(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findAll() {
		return getCurrentSession().createCriteria(User.class).list();
	}

	@Override
	public User update(User entity) {
		getCurrentSession().update(entity);
		return entity;
	}

	@Override
	public void delete(User entity) {
		if(entity != null)
			getCurrentSession().delete(entity);	
	}

}
