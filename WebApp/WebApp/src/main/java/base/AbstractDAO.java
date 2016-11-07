package base;

import java.util.List;

import org.hibernate.Session;

public interface AbstractDAO<T extends BaseEntity>{

	public Session getCurrentSession();
	
	public T get(Long id);
	
	public T create(T entity);
	
	public List<T> findAll();
	
	public T update(T entity);
	
	public void delete(T entity);
	
	
	
}
