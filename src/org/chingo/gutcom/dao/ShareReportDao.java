package org.chingo.gutcom.dao;

import java.io.Serializable;
import java.util.List;

import org.chingo.gutcom.domain.ShareReport;

public interface ShareReportDao
{
	public void save(ShareReport conf);
	public void update(ShareReport conf);
	public void delete(ShareReport conf);
	public void delete(Serializable id);
	public ShareReport get(Serializable id);
	public List<ShareReport> findAll();
}
