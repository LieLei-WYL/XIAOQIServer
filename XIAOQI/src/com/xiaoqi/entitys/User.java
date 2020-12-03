package com.xiaoqi.entitys;

public class User {
	private String phone;
	private String password;
	private String avatar;
	private String name;
	private String gender;
	private String birthday;
	private String area;
	private String profile;
	private String background;
	private String search;
	
	
	
	public User() {
	}

	public User(String phone, String password, String avatar, String name, String gender, String birthday, String area,
			String profile, String background, String search) {
		super();
		this.phone = phone;
		this.password = password;
		this.avatar = avatar;
		this.name = name;
		this.gender = gender;
		this.birthday = birthday;
		this.area = area;
		this.profile = profile;
		this.background = background;
		this.search = search;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String toString() {
		return "User [phone=" + phone + ", password=" + password + ", avatar=" + avatar + ", name=" + name + ", gender="
				+ gender + ", birthday=" + birthday + ", area=" + area + ", profile=" + profile + ", background="
				+ background + ", search=" + search + "]";
	}

}
