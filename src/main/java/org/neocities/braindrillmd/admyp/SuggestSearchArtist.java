package org.neocities.braindrillmd.admyp;

public class SuggestSearchArtist {
	String name;
	String link;
	String imageLink;
	int songsCount;
	
	@Override
	public String toString() {
		return "SuggestSearchArtist [name=" + name + ", link=" + link + ", imageLink=" + imageLink + ", songsCount="
				+ songsCount + "]";
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
	public String getImageLink() {
		return imageLink;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}
	public int getSongsCount() {
		return songsCount;
	}
	public void setSongsCount(int songsCount) {
		this.songsCount = songsCount;
	}
}
