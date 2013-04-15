package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareFav;

public interface ShareFavDao
{
	public void save(ShareFav conf);
	public void update(ShareFav conf);
	public void delete(ShareFav conf);
	public void delete(Serializable id);
	public ShareFav get(Serializable id);
	public List<ShareFav> findAll();
}
