package org.chingo.gutcom.service;

import java.util.List;

import org.chingo.gutcom.domain.CommonUser;

public interface UserManager
{
	public List<CommonUser> findAllUser();
}
