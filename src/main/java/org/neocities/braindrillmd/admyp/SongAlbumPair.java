package org.neocities.braindrillmd.admyp;

public class SongAlbumPair {
	@Override
	public String toString() {
		return "SongAlbumPair [song=" + song + ", album=" + album + "]";
	}
	public Song getSong() {
		return song;
	}
	public void setSong(Song song) {
		this.song = song;
	}
	public Album getAlbum() {
		return album;
	}
	public void setAlbum(Album album) {
		this.album = album;
	}
	private Song song;
	private Album album;
}
