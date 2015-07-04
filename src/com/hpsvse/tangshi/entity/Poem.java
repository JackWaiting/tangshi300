package com.hpsvse.tangshi.entity;

public class Poem {

	private int id;
	private String title;
	private String auth;
	private String type;
	private String content;
	private String desc;

	public Poem() {
		super();
	}

	public Poem(int id, String title, String auth, String type, String content,
			String desc) {
		super();
		this.id = id;
		this.title = title;
		this.auth = auth;
		this.type = type;
		this.content = content;
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
