package org.neocities.braindrillmd.admyp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Crawler {
	public final static Set<Integer> JUNK_TYPES = Set.of(7, 13, 14);
	
	private static final Logger logger = LogManager.getLogger(Crawler.class.getName());
	
	public static void crawl(Artist artist, List<Integer> albumIds, boolean omitJunk) {
		logger.debug(String.format(
				"Scanning artist %s (id=%d) with %d albums in total",
				artist.getName(),
				artist.getId(),
				artist.getAlbums().size()
				));
		
		if(albumIds != null && !albumIds.isEmpty()) {
			logger.debug("Album to download: " + albumIds);
		}else {
			logger.debug("All albums will be downloaded.");
		}
		
		logger.debug("Junk " + (omitJunk ? "will" : "won't") + " be filtered");
		
		List<Album> albumsToCrawl = new ArrayList<>();
		
		for(Album album : artist.getAlbums()) {
			if (omitJunk && JUNK_TYPES.contains(album.getTypeId())) {
				logger.debug(String.format("Album %s (id=%d) ommited", album.getTitle(), album.getId()));
				continue;
			}
			if(albumIds != null && albumIds.contains(album.getId())) {
				logger.debug(String.format(
						"Album %s (id=%d) ommited because of not being in teh list", 
						album.getTitle(), 
						album.getId()));
				continue;
			}
			
			albumsToCrawl.add(album);
		}
		albumsToCrawl.parallelStream().forEach(Crawler::crawlParallel);
	}
	
	public static void crawl(Album album) {
		logger.debug(String.format("Scanning album %s (id=%d) by %s", album.getTitle(), album.getId(), album.getArtist()));
		
		boolean noErrors = true;
		for(Song song: album.getSongs()) {
			noErrors = noErrors && crawl(song, album);
		}
		
		if(noErrors) {
			album.setStatus(Album.STATUS.DOWNLOADED);
		}else {
			album.setStatus(Album.STATUS.HAS_ERRORS);
		}
	}
	
	public static void crawlParallel(SongAlbumPair pair) {
		logger.trace("crawl(Song song, Album album) wrapper");
		crawl(pair.getSong(), pair.getAlbum());
	}
	
	public static void crawlParallel(Album album) {
		logger.debug(
				String.format("Scanning album %s (id=%d) by %s in parallel mode",
						album.getTitle(), album.getId(),
						album.getArtist()));
		
		List<SongAlbumPair> songAlbumPair = new ArrayList<>();
		
		for(Song song : album.getSongs()) {
			SongAlbumPair pair = new SongAlbumPair();
			pair.setAlbum(album);
			pair.setSong(song);
			
			songAlbumPair.add(pair);
		}
		
		songAlbumPair.parallelStream().parallel().forEach(Crawler::crawlParallel);
	}

	public static boolean crawl(Song song, Album album) {
		try {
			song.setFileStatus(Song.FILE_STATUS.PENDING);
			logger.trace(
					String.format("Scanning song %s (id=%d) by %s from %s, page: %s",
							song.getTitle() , song.getId(),
							song.getArtist(), song.getAlbum(),
							song.getPageLink()
							));
			
			Connection conn = Jsoup.connect(song.getPageLink());
			Document songPage = conn.get();
			Element player = songPage.getElementsByClass("player-inline rbt").first();
			song.setLink(
					SiteConstants.HOST +
					player.getElementsByClass("top").first().getElementsByTag("a").first().attr("href")
					);
			song.setdLinkStatus(Song.D_LINK_STATUS.OK);
			
			logger.trace("Trying to fetch a file from: " + song.getLink());
			
			song.setPath(PathBuilder.getSongPath(song, album));
			if(FileManager.downloadSong(song, album)) {
				song.setFileStatus(Song.FILE_STATUS.DOWNLOADED);
				logger.trace("Got us a song: " + song);
				
				return true;
			}else {
				song.setFileStatus(Song.FILE_STATUS.FAILED);
				logger.trace("Something went wrong with: " + song);
				
				return false;
			}
			
		}catch(Exception e) {
			song.setFileStatus(Song.FILE_STATUS.FAILED);
			logger.error("We're expiriencing an oopsie! ", e.getMessage());
			logger.debug(song);
			
			return false;
		}
	}
}
