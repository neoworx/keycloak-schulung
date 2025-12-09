package de.akdb.iam.keycloak.extension.pnridpmapper;

import org.keycloak.broker.provider.AbstractIdentityProviderMapper;
import org.keycloak.broker.saml.SAMLIdentityProviderFactory;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;

public class PersonalnummerIdpMapper extends AbstractIdentityProviderMapper {
    @Override
    public String[] getCompatibleProviders() {
        return new String[]{
                SAMLIdentityProviderFactory.PROVIDER_ID
        };
    }

    @Override
    public String getDisplayCategory() {
        return "SAML";
    }

    @Override
    public String getDisplayType() {
        return "AKDB Personalnummer IDP Mapper";
    }

    @Override
    public String getHelpText() {
        return "Überträgt das Attribute 'pnr' in Abteilungsnummer und Mitarbeiternummer.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return List.of();
    }

    @Override
    public String getId() {
        return "akdb-personalnummer-idp-mapper";
    }
}
