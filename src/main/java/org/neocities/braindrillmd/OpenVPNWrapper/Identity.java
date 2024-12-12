package org.neocities.braindrillmd.OpenVPNWrapper;

public class Identity {
	private String ipAdress;
	private String country;
	private String hostName;
	private String providerName;
	
	@Override
	public String toString() {
		return "Identity [ipAdress=" + ipAdress + ", country=" + country + ", hostName=" + hostName + ", providerName="
				+ providerName + "]";
	}

	
	public String getIpAdress() {
		return ipAdress;
	}
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}
