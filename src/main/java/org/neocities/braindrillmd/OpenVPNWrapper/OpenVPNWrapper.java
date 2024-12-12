package org.neocities.braindrillmd.OpenVPNWrapper;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class OpenVPNWrapper {
	static final int STARTING_IDENTITY_CHECK_INTERVAL = 8000;
	static final int IDENTITY_CHECK_STEP = 2000;
	static final int IDENTITY_CHECK_MINIMUM_INTERVAL = 2000;
	static final int IDENTITY_CHECK_MAX_ATTEMPTS = 25;
	
	static final Logger logger = LogManager.getLogger(OpenVPNWrapper.class.getName());
	
	static Process process;
	static String outputFileName;
	static boolean connectionEstablished = false;
	
	public boolean connect(String configFile, String openVPNPath, IdentityChecker identityChecker) {
		return connect(configFile, null, openVPNPath, identityChecker);
	}
	
	public static boolean connect(String configFile, String cridentials, String openVPNPath, IdentityChecker identityChecker) {
		
            ProcessBuilder pb = cridentials != null ? 
            		new ProcessBuilder(openVPNPath, "--config", configFile,"--auth-user-pass", cridentials) :
            		new ProcessBuilder(openVPNPath, "--config", configFile);
            
            if(outputFileName != null && !"".equals(outputFileName)) {
            	pb.redirectErrorStream(false).redirectOutput(Redirect.appendTo(new File(outputFileName)));
            }
            try {
            	Identity initialIdentity = identityChecker.getIdentity();
            	
            	if(initialIdentity == null) {
            		logger.error("Unable to get initial identity!");
            		return false;
            	}
            	
            	logger.debug("Opening VPN connection. Current identity: " + initialIdentity);
            	
            	process = pb.start();
            	
            	int tries = 0;
            	int currentTimoutInterval = STARTING_IDENTITY_CHECK_INTERVAL;
            	while(true){
            		if(tries >= IDENTITY_CHECK_MAX_ATTEMPTS) {
            			throw new OpenVPNWrapperTooManyAttempts();
            		}
            		Identity newIdentity = identityChecker.getIdentity();
            		if(identityChecker.areIdentitiesEqual(initialIdentity, newIdentity) ) {
            			logger.debug("VPN connection seems not to be established yet. Timeout: " + currentTimoutInterval + 
            					". Tries: " + (tries + 1) + "/" + IDENTITY_CHECK_MAX_ATTEMPTS);
            			Thread.sleep(currentTimoutInterval);
            			if(currentTimoutInterval > IDENTITY_CHECK_MINIMUM_INTERVAL) {
            				currentTimoutInterval = currentTimoutInterval - IDENTITY_CHECK_STEP;
            			}
            			tries++;
            		}else {
            			connectionEstablished = true;
            			logger.debug("Connection established. Current identity: " + newIdentity);
            			break;
            		}
            	}
			} catch (IOException e) {
				logger.error("IO error");
				return false;
			} catch (OpenVPNWrapperTooManyAttempts e) {
				logger.error("Max IP connection retries count reached");
				return false;
			} catch (InterruptedException e) {
				logger.error("Something wnt wrong, lol");
				return false;
			} catch (InvalidIdentitiesComparison e) {
				logger.error("Unable to check identities. Possible service mulfunction or insufficient data");
				return false;
			}
        
            return true;
    }
	
	public static String getOutputFileName() {
		return outputFileName;
	}

	public static void setOutputFileName(String outputFileName) {
		OpenVPNWrapper.outputFileName = outputFileName;
	}
	
	public static boolean connectionIsAlive() {
		return connectionEstablished;
	}

	public static void disconnect() {
		// Connection could be closed at this point, but using openvpn you never know
		if(process != null) {
			logger.debug("Closing VPN connection...");
			process.destroy();
		}else {
			logger.warn("No VPN connection to close...");
		}
	}
}