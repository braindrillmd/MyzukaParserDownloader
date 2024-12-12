package org.neocities.braindrillmd.OpenVPNWrapper;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

public class IdentityChecker2ip implements IdentityChecker {
	private static final Logger logger = LogManager.getLogger(IdentityChecker2ip.class.getName());
	
	private static final String ipCheckSiteHost = "https://2ip.ru";
	private static final String TWOIP_IP_DIV_CLASS = "ip-info";
	private static final String TWOIP_IP_DIV_INNER_ID = "d_clip_button";
	private static final String HTML_SPAN_TAG = "span"; //go figure!
	private static final String TWOIP_ADDITIONAL_DATA_DIV = "user-data";
	private static final String HTML_I_TAG = "i";
	private static final String HTML_DIV_TAG = "div";
	private static final String TWOIP_ADDITIONAL_DATA_ROW = "data_item copy-info-details";
	private static final String TWOIP_DEVICE_ICON_CLASS = "ip-icon-device-desktop";
	private static final String TWOIP_COUNTRY_ID = "ip-info-country";
	private static final String LOCATION_REDUNDEN_APPENDIX = 
			"\u0020\u0423\u0442\u043e\u0447\u043d\u0438\u0442\u044c\u003f";
	private static final String TWOIP_PROVIDER_DIV_CLASS = "value value-custom";
	
	protected Document getIPCheckPageContent() throws IOException {
		Connection conn = Jsoup.connect(ipCheckSiteHost);
		Document doc = conn.get();
	   
		return doc;
	}
	
	@Override
	public Identity getIdentity() {
		try {
			Identity identity = new Identity();
			
			Document document = getIPCheckPageContent();
			Elements ipDiv = document.body().getElementsByClass(TWOIP_IP_DIV_CLASS);
			String ip = ipDiv.first().getElementById(TWOIP_IP_DIV_INNER_ID).
					getElementsByTag(HTML_SPAN_TAG).first().text();
			identity.setIpAdress(ip);
			
			Elements additionalData = document.body().getElementsByClass(TWOIP_ADDITIONAL_DATA_DIV);
			Elements addDataElements = additionalData.first().getElementsByClass(TWOIP_ADDITIONAL_DATA_ROW);
			for(Element element : addDataElements) {
				Element lineDevice = element.getElementsByTag(HTML_I_TAG).first();
				Element lineCountry = element.getElementById(TWOIP_COUNTRY_ID);
				Element lineProvider = element.getElementsByClass(TWOIP_PROVIDER_DIV_CLASS).first();
				
				if(lineDevice != null && lineDevice.hasClass(TWOIP_DEVICE_ICON_CLASS)){
					identity.setHostName(element.getElementsByTag(HTML_DIV_TAG).get(2).text());
				}
				if(lineCountry != null){
					identity.setCountry(lineCountry.text().replace(LOCATION_REDUNDEN_APPENDIX, " ").trim());
				}
				if(lineProvider != null){
					identity.setProviderName(lineProvider.text());
				}
			}
			
			return identity;
			
		} catch (IOException e) {
			logger.error("Failed getting connection to " + ipCheckSiteHost);
			return null;
		} catch (NullPointerException e) {
			logger.error("Possible parse error for service \"" + ipCheckSiteHost + "\"");
			logger.debug(e.getMessage());
			return null;
		}
	}
	
	@Override
	public boolean areIdentitiesEqual(Identity identity1, Identity identity2) throws InvalidIdentitiesComparison {
		if(identity1 != null && identity2 != null &&
		   identity1.getIpAdress() != null && identity2.getIpAdress() != null) {
			return identity1.getIpAdress().equals(identity2.getIpAdress());
		}else {
			throw new InvalidIdentitiesComparison();
		}
	}
}
