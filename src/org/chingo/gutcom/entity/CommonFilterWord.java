package org.chingo.gutcom.entity;

// Generated Apr 12, 2013 10:41:18 AM by Hibernate Tools 4.0.0

/**
 * CommonFilterWord generated by hbm2java
 */
public class CommonFilterWord implements java.io.Serializable
{

	private Integer wid;
	private String word;
	private byte level;

	public CommonFilterWord()
	{
	}

	public CommonFilterWord(String word, byte level)
	{
		this.word = word;
		this.level = level;
	}

	public Integer getWid()
	{
		return this.wid;
	}

	public void setWid(Integer wid)
	{
		this.wid = wid;
	}

	public String getWord()
	{
		return this.word;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

	public byte getLevel()
	{
		return this.level;
	}

	public void setLevel(byte level)
	{
		this.level = level;
	}

}
