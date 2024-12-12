package org.neocities.braindrillmd.OpenVPNWrapper;

public interface IdentityChecker {
	Identity getIdentity();
	boolean areIdentitiesEqual(Identity identity1, Identity identity2) throws InvalidIdentitiesComparison;
}
