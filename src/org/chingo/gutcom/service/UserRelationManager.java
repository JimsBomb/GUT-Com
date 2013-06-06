package org.chingo.gutcom.service;

import java.util.List;

import org.chingo.gutcom.bean.UserInfoBean;
import org.chingo.gutcom.domain.CommonSyslog;

public interface UserRelationManager
{
	/**
	 * 获取指定用户的关注列表，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 用户ID
	 * @param nickname 用户昵称
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为UserInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchFollowing(String uid, String nickname,
			long timestamp, String startRow, int pageSize);
	
	/**
	 * 获取指定用户的粉丝列表，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 用户ID
	 * @param nickname 用户昵称
	 * @param timestamp 上次更新获取时间戳
	 * @param startRow 查询起始行的rowKey
	 * @param pageSize 单页查询数量
	 * @return List中第一个对象为UserInfoBean列表，第二个为下一页的起始行rowKey（如果有的话）。无则返回null
	 */
	public List<Object> fetchFollower(String uid, String nickname,
			long timestamp, String startRow, int pageSize);
	
	/**
	 * 关注指定用户，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 被关注用户的ID
	 * @param nickname 被关注用户的昵称
	 * @param remark 备注名
	 * @param log 日志对象
	 * @return true-关注成功，false-关注失败
	 */
	public boolean follow(String uid, String nickname, String remark, CommonSyslog log);
	
	/**
	 * 取消关注指定用户，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 用户ID
	 * @param nickname 用户昵称
	 * @param log 日志对象
	 * @return true-取消成功，false-取消失败
	 */
	public boolean drop(String uid, String nickname, CommonSyslog log);
	
	/**
	 * 更新指定关注用户的备注，用户ID和昵称二选一，优先取用户ID的值
	 * @param uid 被关注用户的ID
	 * @param nickname 被关注用户的昵称
	 * @param remark 新备注名
	 * @param log 日志对象
	 * @return 更新后的用户Bean，更新失败则返回null
	 */
	public UserInfoBean remark(String uid, String nickname, String remark, CommonSyslog log);
}
