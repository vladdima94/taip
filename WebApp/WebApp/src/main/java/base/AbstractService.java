package base;

import java.util.List;

public interface AbstractService<T extends BaseEntity>{
	
	public T get(Long id);
	
	public T create(T entity);
	
	public List<T> findAll();
	
	public T update(T entity);
	
	public void delete(T entity);
	
}
