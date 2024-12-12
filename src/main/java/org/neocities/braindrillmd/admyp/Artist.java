package org.neocities.braindrillmd.admyp;

import java.util.List;

public class Artist {
	private String name;
	private String link;
	private int id;
	private String country;
	private String photo;
	private String description;
	private List<Album> albums;
	
	@Override
	public String toString() {
		return "Artist [name=" + name + ", link=" + link + ", id=" + id + ", country=" + country + ", photo=" + photo
				+ ", description=" + description + ", albums=" + albums + "]";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<Album> getAlbums() {
		return albums;
	}
	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}
}
