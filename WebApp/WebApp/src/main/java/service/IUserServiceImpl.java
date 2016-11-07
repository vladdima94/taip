package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dao.IUserDAO;
import dao.UserDAOImpl;
import model.User;

@Service("userService")
@Transactional(rollbackFor = Exception.class)
public class IUserServiceImpl implements IUserService{

	@Autowired
	private IUserDAO userDAO;
	
	@Override
	public User get(Long id) {
		return userDAO.get(id);
	}

	@Override
	public User create(User entity) {
		return userDAO.create(entity);
	}

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public User update(User entity) {
		return userDAO.update(entity);
	}

	@Override
	public void delete(User entity) {
		userDAO.delete(entity);
	}

}
