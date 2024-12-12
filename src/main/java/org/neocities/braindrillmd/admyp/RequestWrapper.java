package org.neocities.braindrillmd.admyp;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class RequestWrapper {
	private final CloseableHttpClient httpClient = HttpClients.createDefault();
	
	private static final Logger logger = LogManager.getLogger(RequestWrapper.class.getName());
	
	public List<SearchResult> getSearchResults (String querry) throws IOException{
		String pageLink = SiteConstants.SEARCH_PAGE_REDEIRECTED + querry;
		logger.debug("Performing search request: " + pageLink);
		
		Document searchPage = getPage(pageLink);
		Element body = searchPage.getElementById("bodyContent");
		Element table = body.getElementsByTag("table").first();
		Elements rows = table.getElementsByTag("tbody").first().getElementsByTag("tr");
		
		List<SearchResult> result = new LinkedList<>();
		
		logger.debug("Entries found: " + rows.size());
		
		for(Element row : rows) {
			SearchResult searchResult = new SearchResult();
			
			Elements columns = row.getElementsByTag("td");
			
			int columnNum = 0;
			for(Element column : columns) {
				switch(columnNum) {
				case 0:
					String imageLink = column.getElementsByTag("img").first().attr("src");
					searchResult.setImageLink(
							(imageLink.endsWith(SiteConstants.DEFAULT_IMAGE) ? SiteConstants.HOST : "") + imageLink
					);
					
					break;
				case 1:
					searchResult.setLink(SiteConstants.HOST + column.getElementsByTag("a").first().attr("href"));
					searchResult.setArtistName(column.getElementsByTag("a").first().text());
					
					break;
				case 2:
					searchResult.setSongCount(Integer.parseInt(column.text()));
					
					break;
				}
				
				columnNum++;
			}
			
			logger.trace("Entry: " + searchResult);
			
			result.add(searchResult);	
		}
		
		logger.trace("Search result: " + result);
		
		return result;
	}
	
	private Document getPage(String uri) throws IOException {
		Connection conn = Jsoup.connect(uri);
		return conn.get();
	}
	
	public Artist getArtistData (String uri) throws IOException{
		logger.debug("Acquiring artist data via URI: " + uri);
		
		String link = uri;
		Document document = getPage(link);
		
		Element bodyContent = document.getElementById("bodyContent");
		
		Artist artist = new Artist();
		
		String [] linkSplit = link.split("/");
		artist.setId(Integer.parseInt(linkSplit[linkSplit.length - 2]));
		artist.setLink(link);
		artist.setName(bodyContent.getElementsByTag("h1").text());
		
		Element mainDetails = bodyContent.getElementsByClass("main-details").first();
		
		artist.setPhoto(
				mainDetails.getElementsByClass("vis").first().getElementsByTag("img").first().attr("src")
				);
		
		Element dataTable = mainDetails.getElementsByClass("tbl").first().getElementsByTag("tbody").first();
		
		int rowCount = 0;
		for(Element row : dataTable.getElementsByTag("tr")) {
			switch(rowCount) {
				case 0:
					break;
				case 1:
					artist.setCountry(row.getElementsByTag("td").last().text());
					break;
				case 2:
					artist.setDescription(row.getElementById("inner_desc").text());
					break;
			}
			
			rowCount++;
		}
		
		logger.trace("Artist data acquired: " + artist);
		
		return artist;
	}
	
	public List<Album> getAlbumList(String artistLink) throws IOException{
		String link = artistLink + SiteConstants.ALBUMS_PAGE;
		
		logger.debug("Acquiring album list via: " + link);
		
		Document document = getPage(link);
		
		String[] breadcrumbs = document.getElementsByClass("breadcrumbs").first().text().split("/");
		String artistName = breadcrumbs[breadcrumbs.length - 2].trim();
		
		Element albumListDiv = document.getElementById("divAlbumsList");
		
		List<Album> albumList= new LinkedList<>();
		
		for(Element albumEl : albumListDiv.getElementsByClass("item ")) {
			Album album = new Album();
			album.setTypeId(Integer.parseInt(albumEl.attr("data-type")));
			album.setArtist(artistName);
			album.setImageLink(
					albumEl.getElementsByClass("vis").first().getElementsByTag("img").first().attr("src")
					);
			
			Element info = albumEl.getElementsByClass("info").first();
			
			Element titleHref = info.getElementsByClass("title").first().getElementsByTag("a").first();
			String albumRelLink = titleHref.attr("href");
			album.setLink(SiteConstants.HOST + albumRelLink);
			album.setTitle(titleHref.text());
			String[] linkSplit = albumRelLink.split("/");
			album.setId(Integer.parseInt(linkSplit[linkSplit.length - 2]));
			
			album.setGenre(info.getElementsByClass("tags").first().getElementsByTag("a").first().text());
			album.setYear(Integer.parseInt(
					info.getElementsByClass("tags").last().getElementsByTag("a").first().text()
					));
			
			album.setStatus(Album.STATUS.UNINITIALISED);
			
			albumList.add(album);
		}
	
		logger.trace("Got album list:\n" + albumList);
		return albumList;
	}
	
	public Album getAlbumData(String uri, int typeId) throws IOException {
		logger.debug("Acquiring album via: " + uri);
		Document document = getPage(uri);
		
		Album album = new Album();
		
		album.setTypeId(typeId);
		String[] linkSplit = uri.split("/");
		album.setId(Integer.parseInt(linkSplit[linkSplit.length - 2]));
		
		String[] breadcrumbs = document.getElementsByClass("breadcrumbs").first().text().split("/");
		String artistName = breadcrumbs[breadcrumbs.length - 2].trim();
		String albumTitle = breadcrumbs[breadcrumbs.length - 1].trim();
		album.setArtist(artistName);
		album.setTitle(albumTitle);
		album.setLink(uri);
		
		logger.debug(String.format("Found %s by %s", albumTitle, artistName));
		
		Element bodyContent = document.getElementById("bodyContent");
		
		Element mainDetails = bodyContent.getElementsByClass("main-details").first();
		album.setImageLink(mainDetails.getElementsByClass("vis").first().getElementsByTag("img").attr("src"));
		
		Element detailsTable = mainDetails.getElementsByClass("tbl").first().getElementsByTag("tbody").first();
		int rowCount = 0;
		for(Element row : detailsTable.getElementsByTag("tr")) {
			String textField = row.getElementsByTag("td").last().text();
			
			switch(rowCount) {
				case 0:
					album.setGenre(textField);
					break;
				case 2:
					album.setYear(Integer.parseInt(textField));
					break;
				case 3:
					album.setLabel(textField);
					break;
				case 4:
					album.setType(textField);
					break;
			}
			
			
			rowCount++;
		}
		
		int trackNumber = 1;
		Elements songsEl = bodyContent.getElementsByClass("player-inline ");
		List<Song> songs = new LinkedList<>();
		
		for(Element songEl : songsEl) {
			Song song = new Song();
			song.setTrackNumber(trackNumber++);
			song.setAlbum(album.getTitle());
			song.setArtist(album.getArtist());
			
			Element top = songEl.getElementsByClass("top").first();
			song.setId(Integer.parseInt(top.getElementsByClass("add-to-pl ico-plus  ").last().attr("data-id")));
			song.setPageLink(
					SiteConstants.HOST +
					songEl.getElementsByClass("top").first().getElementsByTag("a").attr("href")
					);
			String[] data = songEl.getElementsByClass("data").first().text().split("\\|");
			
			song.setLength(data[0].trim());
			song.setBitRate(data[1].trim().split("\s+")[0]);
			
			Element details = songEl.getElementsByClass("details").first();
			song.setSize(details.getElementsByClass("time").first().text().split("\s+")[0]); //sic!
			song.setTitle(details.getElementsByTag("p").first().text());
			
			song.setFileStatus(Song.FILE_STATUS.UNKNOWN);
			song.setdLinkStatus(Song.D_LINK_STATUS.UNINITIALISED);
			
			songs.add(song);
		}
		
		album.setSongs(songs);
		
		album.setStatus(Album.STATUS.UNINITIALISED);
		
		logger.trace("Acquired album data: " + album);
		
		return album;
	}
}
