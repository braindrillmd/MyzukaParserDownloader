package org.neocities.braindrillmd.admyp;

public class PathBuilder {
	private static String storagePath = "D:\\Test\\Music";
	private static final String DLM = "\\";
	private static final String ALBUM_DLM = " - ";
	private static final String POSTFIX = ".mp3";
	private static final String SYMBOLS_TO_SANITIZE_REGEX = "[\\/:\\?<>\"\\|\\*]";
	
	public static String getSongPath(Song song, Album album) {
		return getAlbumPath(album) + DLM + 
				formatTrackNumber(song.getTrackNumber(), album.getSongs().size()) + ALBUM_DLM +
				song.getTitle().replaceAll(SYMBOLS_TO_SANITIZE_REGEX, ALBUM_DLM) + POSTFIX;
		
	}
	
	public static String getAlbumPath(Album album) {
		return storagePath + DLM + 
				album.getArtist().replaceAll(SYMBOLS_TO_SANITIZE_REGEX, ALBUM_DLM) + DLM + 
				album.getYear() + ALBUM_DLM + album.getTitle().replaceAll(SYMBOLS_TO_SANITIZE_REGEX, ALBUM_DLM);
	}
	
	public static String formatTrackNumber(int trackNum, int trackNumMax) {
		int addZeroes = String.valueOf(trackNumMax).length() - String.valueOf(trackNum).length();
		
		StringBuilder sbuilder = new StringBuilder();
		
		for(int i = 0; i < addZeroes; i++) {
			sbuilder.append("0");
		}
		
		if(trackNumMax < 10) {
			sbuilder.append("0");
		}
		
		return sbuilder.append(String.valueOf(trackNum)).toString();
	}

	public static String getStoragePath() {
		return storagePath;
	}

	public static void setStoragePath(String storagePath) {
		PathBuilder.storagePath = storagePath;
	}
}
