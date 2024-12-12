package org.neocities.braindrillmd.admyp;

import java.io.File;
import java.nio.file.Files;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileManager {
	private static final String USER_AGENT = "Mozilla/5.0 (platform; rv:geckoversion) Gecko/geckotrail Firefox/firefoxversion";
	private static final Logger logger = LogManager.getLogger(FileManager.class.getName());
	
	public static boolean downloadSong(Song song, Album album) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		try {
			HttpGet request = new HttpGet(song.getLink());
	        request.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
	        
	        try (CloseableHttpResponse response = httpClient.execute(request)) {
	        	logger.trace("Request sent: " + request);

	            HttpEntity entity = response.getEntity();
	            Header headers = entity.getContentType();
	            
	            int responseCode = response.getStatusLine().getStatusCode();
	            
	            logger.trace("Response code: " + responseCode );
	            logger.trace("Response: " + entity);
	            logger.trace("Headers: " + headers);
	           
	            String filepath = PathBuilder.getSongPath(song, album);
	            File file = new File(filepath);
	            
	            file.getParentFile().mkdirs();
	            
	            if (entity != null) {
	            	Files.copy(
	            			entity.getContent(), 
	            			file.toPath());
	            }
	            
	            return true;
	        }
		}catch(Exception e) {
			logger.error("Oh no! " + e);
			
			return false;
		}
	}
}
