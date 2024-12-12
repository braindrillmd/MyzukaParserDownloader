package org.neocities.braindrillmd.admyp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.ParseException;
import org.neocities.braindrillmd.OpenVPNWrapper.IdentityChecker;
import org.neocities.braindrillmd.OpenVPNWrapper.IdentityChecker2ip;
import org.neocities.braindrillmd.OpenVPNWrapper.OpenVPNWrapper;

public class Admyp {
	public static final String OPENVPN_CONFIG_FILE = "D:\\Test\\OpenVPN\\USA_freeopenvpn_udp.ovpn";
	public static final String OPENVPN_CONFIG_FILE2 = "D:\\Test\\OpenVPN\\37.26.3.139_udp_38785.ovpn";
	public static final String OPENVPN_CRIDENTIALS_FILE = "D:\\Test\\OpenVPN\\cridentials.txt";
	public static final String OPEN_VPN_PATH = "C:\\Program Files\\OpenVPN\\bin\\openvpn.exe\\";
	public static final String LOG_FILE_PATH = "D:\\Test\\OpenVPN\\log.txt";
	
	public static void main(String[] args) {
		IdentityChecker identityChecker = new IdentityChecker2ip();
		OpenVPNWrapper.setOutputFileName(LOG_FILE_PATH);
		OpenVPNWrapper.connect(OPENVPN_CONFIG_FILE, OPENVPN_CRIDENTIALS_FILE, OPEN_VPN_PATH, identityChecker);
		//OpenVPNWrapper.connect(OPENVPN_CONFIG_FILE2, OPEN_VPN_PATH, identityChecker);
		
		RequestWrapper wrapper = new RequestWrapper();
		
		try {
			List<SearchResult> searchResults = wrapper.getSearchResults("The Honorary Title");
			Artist artist = wrapper.getArtistData(searchResults.get(0).getLink());
			List<Album> albumList  = wrapper.getAlbumList(artist.getLink());
			//Album album = wrapper.getAlbumData(albumList.get(0).getLink(), albumList.get(0).getTypeId());
			
			
			List<Album> newAlbumList = new ArrayList<>(albumList.size());
			int count = 0;
			for(Album album : albumList) {
				newAlbumList.add(count++, wrapper.getAlbumData(album.getLink(), album.getTypeId()));
			}
			
			artist.setAlbums(newAlbumList);
			
			//System.out.print(artist);
			
			Crawler.crawl(artist, null, true);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		OpenVPNWrapper.disconnect();
	}

}
