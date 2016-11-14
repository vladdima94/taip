package daoPackage;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import model.User;

import dao.UserDAOImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:h2.xml"})
@Transactional
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class JUserDAO {
	
	@Autowired
	private UserDAOImpl userDAO;
	
	@Test
	public void testGet() {
		User user = userDAO.get(1L);
		Assert.assertNotNull(user);
	}

	@Test
	public void testCreate() {
		User user = new User();
		user.setUsername("username");
		user.setPassword("password");
		user.setEmail("email@email.com");
		
		User createdUser = userDAO.create(user);
		Assert.assertNotNull(createdUser);
	}

	@Test
	public void testFindAll() {
		List<User> users = userDAO.findAll();
		if(users == null)
			fail("No users");
		Assert.assertNotEquals(users.size(), 0);
	}

	@Test
	public void testUpdate() {
		User user = userDAO.get(1L);
		user.setUsername(user.getUsername() + " updated");
		user.setPassword(user.getPassword() + " updated");
		User updatedUser = userDAO.update(user);
		
		if(updatedUser == null)
			fail("Could not update the user!");
		Assert.assertEquals(user, updatedUser);
	}

	@Test
	public void testDelete() {
		User user = userDAO.get(1L);
		userDAO.delete(user);
		User deletedUser = userDAO.get(1L);
		Assert.assertNull(deletedUser);
	}

}
