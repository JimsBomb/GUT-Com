package org.chingo.gutcom.dao;

public interface TestDao
{
	public void save(Object o);
	public Object get(Class c, java.io.Serializable id);
	public Object getOne(Class c);
	public void delete(Object o);
}
