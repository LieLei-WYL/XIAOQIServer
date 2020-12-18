package com.xiaoqi.entitys;

public class Dialogue {
	private int id;
	private String sentence;
	private String time;
	private int type;//0表示发送，1表示接收
	
	public Dialogue() {
		super();
	}
	
	public Dialogue(int id, String sentence, String time, int type) {
		super();
		this.id = id;
		this.sentence = sentence;
		this.time = time;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Dialogue [id=" + id + ", sentence=" + sentence + ", time=" + time + ", type=" + type + "]";
	}

}
