package org.neocities.braindrillmd.admyp;

import java.util.List;

public class Album {
	enum STATUS{
		HAS_ERRORS,
		DOWNLOADED,
		UNINITIALISED,
	}
	
	private String title;
	private String link;
	private String artist;
	private int year;
	private String imageLink;
	private String type;
	private int typeId;
	private String genre;
	private String label;
	private int id;
	private List<Song> songs;
	private STATUS status;
	
	@Override
	public String toString() {
		return "Album [title=" + title + ", link=" + link + ", artist=" + artist + ", year=" + year + ", imageLink="
				+ imageLink + ", type=" + type + ", typeId=" + typeId + ", genre=" + genre + ", label=" + label
				+ ", id=" + id + ", songs=" + songs + ", status=" + status + "]";
	}
	
	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public List<Song> getSongs() {
		return songs;
	}
	public void setSongs(List<Song> songs) {
		this.songs = songs;
	}
}
