package com.xiaoqi.entitys;

import java.util.List;

public class NoteDetail {
    private String avatar;
    private String name;
    private String images;
    private int followFlag;
    private int likeFlag;
    private int collectionFlag;
    private String likes;
    private String collections;
    private String comments;
    private List<Comment> commentList;

    public NoteDetail() {
    }

	public NoteDetail(String avatar, String name, String images, int followFlag, int likeFlag, int collectionFlag,
			String likes, String collections, String comments, List<Comment> commentList) {
		super();
		this.avatar = avatar;
		this.name = name;
		this.images = images;
		this.followFlag = followFlag;
		this.likeFlag = likeFlag;
		this.collectionFlag = collectionFlag;
		this.likes = likes;
		this.collections = collections;
		this.comments = comments;
		this.commentList = commentList;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public int getFollowFlag() {
		return followFlag;
	}

	public void setFollowFlag(int followFlag) {
		this.followFlag = followFlag;
	}

	public int getLikeFlag() {
		return likeFlag;
	}

	public void setLikeFlag(int likeFlag) {
		this.likeFlag = likeFlag;
	}

	public int getCollectionFlag() {
		return collectionFlag;
	}

	public void setCollectionFlag(int collectionFlag) {
		this.collectionFlag = collectionFlag;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getCollections() {
		return collections;
	}

	public void setCollections(String collections) {
		this.collections = collections;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	@Override
	public String toString() {
		return "NoteDetail [images=" + images + ", followFlag=" + followFlag + ", likeFlag=" + likeFlag
				+ ", collectionFlag=" + collectionFlag + ", likes=" + likes + ", collections=" + collections
				+ ", comments=" + comments + ", commentList=" + commentList + "]";
	}

}