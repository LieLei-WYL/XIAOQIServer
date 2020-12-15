package com.xiaoqi.entitys;

public class Comment {
	private int commentId;
	private int noteId;
    private String phone;
    private String avatar;
    private String name;
    private String content;
    private String date;
    private String likes;

    public Comment() {
    }

	public Comment(int noteId, String phone, String content, String date) {
		super();
		this.noteId = noteId;
		this.phone = phone;
		this.content = content;
		this.date = date;
	}

	public Comment(int commentId, int noteId, String phone, String content, String date, String likes) {
		super();
		this.commentId = commentId;
		this.noteId = noteId;
		this.phone = phone;
		this.content = content;
		this.date = date;
		this.likes = likes;
	}

	public Comment(int commentId, int noteId, String phone, String avatar, String name, String content, String date,
			String likes) {
		super();
		this.commentId = commentId;
		this.noteId = noteId;
		this.phone = phone;
		this.avatar = avatar;
		this.name = name;
		this.content = content;
		this.date = date;
		this.likes = likes;
	}

	public int getCommentId() {
		return commentId;
	}

	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}

	public int getNoteId() {
		return noteId;
	}

	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", noteId=" + noteId + ", phone=" + phone + ", avatar=" + avatar
				+ ", name=" + name + ", content=" + content + ", date=" + date + ", likes=" + likes + "]";
	}

}