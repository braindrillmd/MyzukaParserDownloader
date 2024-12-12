package org.neocities.braindrillmd.admyp;

public class Song {
	enum D_LINK_STATUS{
		OK,
		EXPIRED,
		UNINITIALISED,
	}
	
	enum FILE_STATUS{
		DOWNLOADED,
		PENDING,
		FAILED,
		UNKNOWN,
	}
	
	private int trackNumber;
	private int id;
	private String title;
	private String artist;
	private String album;
	private String link;
	private String pageLink;
	private String directLink;
	private String size;
	private String length;
	private String bitRate;
	private String path;
	private FILE_STATUS fileStatus;
	private D_LINK_STATUS dLinkStatus;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}
	
	public FILE_STATUS getFileStatus() {
		return fileStatus;
	}

	public void setFileStatus(FILE_STATUS fileStatus) {
		this.fileStatus = fileStatus;
	}

	public D_LINK_STATUS getdLinkStatus() {
		return dLinkStatus;
	}

	public void setdLinkStatus(D_LINK_STATUS dLinkStatus) {
		this.dLinkStatus = dLinkStatus;
	}
	
	@Override
	public String toString() {
		return "Song [trackNumber=" + trackNumber + ", id=" + id + ", title=" + title + ", artist=" + artist
				+ ", album=" + album + ", link=" + link + ", pageLink=" + pageLink + ", directLink=" + directLink
				+ ", size=" + size + ", length=" + length + ", bitRate=" + bitRate + ", path=" + path + ", fileStatus="
				+ fileStatus + ", dLinkStatus=" + dLinkStatus + "]";
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDirectLink() {
		return directLink;
	}
	public void setDirectLink(String directLink) {
		this.directLink = directLink;
	}
	public String getPageLink() {
		return pageLink;
	}

	public void setPageLink(String pageLink) {
		this.pageLink = pageLink;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getBitRate() {
		return bitRate;
	}
	public void setBitRate(String bitRate) {
		this.bitRate = bitRate;
	}
}
