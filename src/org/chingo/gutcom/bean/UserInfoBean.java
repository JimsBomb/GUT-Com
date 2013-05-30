package org.chingo.gutcom.bean;

import org.chingo.gutcom.domain.CommonUser;

public class UserInfoBean implements java.io.Serializable
{
	private String uid; // 用户ID
	private String nickname; // 用户昵称，唯一
	private String email; // 邮箱地址，唯一
	private String studentnum = ""; // 绑定学号，唯一
	private byte status = 0; // 用户状态
	private long regdate; // 注册时间戳
	private String realname = ""; // 真实姓名
	private String college = ""; // 所在学院
	private String major = ""; // 就读专业
	private String classname = ""; // 班级名称
	private byte gender = 0; // 性别
	private String birth = ""; // 出生日期
	private String constellation = ""; // 星座
	private String zodiac = ""; // 生肖
	private String bloodtype = ""; // 血型
	private String qq = ""; // QQ号码
	private String selfintro = ""; // 自我简介
	private String avatarurl = ""; // 标准头像地址
	private String bigavatarurl = ""; // 大头像地址
	private long lastlogin = 0L; // 上次登录时间戳
	private int weibocnt = 0; // 已发微博总数
	private int follower = 0; // 粉丝总数
	private int following = 0; // 关注总数
	private byte isfollowed = 0; // 是否已关注
	private String remark = ""; // 备注名
	private Latest latest = null; // 最后发表微博对象
	
	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getStudentnum()
	{
		return studentnum;
	}

	public void setStudentnum(String studentnum)
	{
		this.studentnum = studentnum;
	}

	public byte getStatus()
	{
		return status;
	}

	public void setStatus(byte status)
	{
		this.status = status;
	}

	public long getRegdate()
	{
		return regdate;
	}

	public void setRegdate(long regdate)
	{
		this.regdate = regdate;
	}

	public String getRealname()
	{
		return realname;
	}
	
	public void setRealname(String realname)
	{
		this.realname = realname;
	}


	public String getCollege()
	{
		return college;
	}


	public void setCollege(String college)
	{
		this.college = college;
	}


	public String getMajor()
	{
		return major;
	}

	public void setMajor(String major)
	{
		this.major = major;
	}

	public String getClassname()
	{
		return classname;
	}

	public void setClassname(String classname)
	{
		this.classname = classname;
	}

	public byte getGender()
	{
		return gender;
	}

	public void setGender(byte gender)
	{
		this.gender = gender;
	}

	public String getBirth()
	{
		return birth;
	}

	public void setBirth(String birth)
	{
		this.birth = birth;
	}

	public String getConstellation()
	{
		return constellation;
	}

	public void setConstellation(String constellation)
	{
		this.constellation = constellation;
	}

	public String getZodiac()
	{
		return zodiac;
	}

	public void setZodiac(String zodiac)
	{
		this.zodiac = zodiac;
	}

	public String getBloodtype()
	{
		return bloodtype;
	}

	public void setBloodtype(String bloodtype)
	{
		this.bloodtype = bloodtype;
	}

	public String getQq()
	{
		return qq;
	}

	public void setQq(String qq)
	{
		this.qq = qq;
	}

	public String getSelfintro()
	{
		return selfintro;
	}

	public void setSelfintro(String selfintro)
	{
		this.selfintro = selfintro;
	}

	public String getAvatarurl()
	{
		return avatarurl;
	}

	public void setAvatarurl(String avatarurl)
	{
		this.avatarurl = avatarurl;
	}

	public String getBigavatarurl()
	{
		return bigavatarurl;
	}

	public void setBigavatarurl(String bigavatarurl)
	{
		this.bigavatarurl = bigavatarurl;
	}

	public long getLastlogin()
	{
		return lastlogin;
	}

	public void setLastlogin(long lastlogin)
	{
		this.lastlogin = lastlogin;
	}

	public int getWeibocnt()
	{
		return weibocnt;
	}

	public void setWeibocnt(int weibocnt)
	{
		this.weibocnt = weibocnt;
	}

	public int getFollower()
	{
		return follower;
	}

	public void setFollower(int follower)
	{
		this.follower = follower;
	}

	public int getFollowing()
	{
		return following;
	}

	public void setFollowing(int following)
	{
		this.following = following;
	}

	public byte getIsfollowed()
	{
		return isfollowed;
	}

	public void setIsfollowed(byte isfollowed)
	{
		this.isfollowed = isfollowed;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public Latest getLatest()
	{
		return latest;
	}

	public void setLatest(Latest latest)
	{
		this.latest = latest;
	}
	
	public UserInfoBean()
	{
		
	}
	
	/**
	 * 根据CommonUser对象填充用户信息
	 * @param user 用户对象
	 */
	public UserInfoBean(CommonUser user)
	{
		this.uid = user.getUid();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.studentnum = user.getStudentnum();
		this.status = user.getStatus();
		this.regdate = user.getRegdate();
		this.realname = user.getRealname();
		this.college = user.getCollege();
		this.major = user.getMajor();
		this.classname = user.getClassname();
		this.gender = user.getGender();
		if(user.getBirthday()>0 && user.getBirthmonth()>0 && user.getBirthyear()>0)
		{
			this.birth = user.getBirthyear() + "年" + user.getBirthmonth()
					+ "月" + user.getBirthday() + "日";
		}
		this.constellation = user.getConstellation();
		this.zodiac = user.getZodiac();
		this.bloodtype = user.getBloodtype();
		this.qq = user.getQq();
		this.selfintro = user.getSelfintro();
		this.avatarurl = user.getAvatarurl();
		this.bigavatarurl = user.getBigavatarurl();
		this.weibocnt = user.getWeibocnt();
		this.follower = user.getFollower();
		this.following = user.getFollowing();
		this.lastlogin = user.getLastlogin();
	}


	/**
	 * 最后发表微博内部类
	 * @author Chingo.Org
	 *
	 */
	public class Latest
	{
		private String wid = ""; // 微博ID
		private String content = ""; // 内容
		
		public Latest()
		{
			
		}
		
		public Latest(String wid, String content)
		{
			this.wid = wid;
			this.content = content;
		}
		
		public String getWid()
		{
			return wid;
		}
		public void setWid(String wid)
		{
			this.wid = wid;
		}
		public String getContent()
		{
			return content;
		}
		public void setContent(String content)
		{
			this.content = content;
		}
	}
}
