package org.neocities.braindrillmd.admyp;

public class SearchResult {
	private String artistName;
	private String link;
	private String imageLink;
	private int songCount;
	
	@Override
	public String toString() {
		return "SearchResult [artistName=" + artistName + ", link=" + link + ", imageLink=" + imageLink + ", songCount="
				+ songCount + "]";
	}
	
	public String getArtistName() {
		return artistName;
	}
	public void setArtistName(String artistName) {
		this.artistName = artistName;
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
	public void setImageLink(String umageLink) {
		this.imageLink = umageLink;
	}
	public int getSongCount() {
		return songCount;
	}
	public void setSongCount(int songCount) {
		this.songCount = songCount;
	}
}
