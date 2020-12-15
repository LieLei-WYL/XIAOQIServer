package com.xiaoqi.entitys;

import java.util.List;

import com.xiaoqi.entitys.Note;

public class NoteInfo {
	private List<Note> notes;

	/**
	 * @return the cakes
	 */
	public List<Note> getNotes() {
		return notes;
	}

	/**
	 * @param cakes the cakes to set
	 */
	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "NoteInfo [notes=" + notes + "]";
	}
}
