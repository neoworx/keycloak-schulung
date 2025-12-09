package de.akdb.iam.keycloak.extension.pnridpmapper;

import com.google.auto.service.AutoService;
import org.keycloak.broker.provider.AbstractIdentityProviderMapper;
import org.keycloak.broker.provider.BrokeredIdentityContext;
import org.keycloak.broker.provider.IdentityProviderMapper;
import org.keycloak.broker.saml.SAMLEndpoint;
import org.keycloak.broker.saml.SAMLIdentityProviderFactory;
import org.keycloak.dom.saml.v2.assertion.AssertionType;
import org.keycloak.dom.saml.v2.assertion.AttributeStatementType;
import org.keycloak.models.IdentityProviderMapperModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.provider.ProviderConfigProperty;

import java.util.List;
import java.util.Optional;

@AutoService(IdentityProviderMapper.class)
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

    @Override
    public void importNewUser(KeycloakSession session, RealmModel realm, UserModel user, IdentityProviderMapperModel mapperModel, BrokeredIdentityContext context) {
        AssertionType assertion = (AssertionType) context.getContextData().get(SAMLEndpoint.SAML_ASSERTION);
        assertion.getAttributeStatements().stream().forEach(attributeStatement -> {
            Optional<AttributeStatementType.ASTChoiceType> pnrAttribute = attributeStatement.getAttributes().stream().filter(attribute -> "pnr".equals(attribute.getAttribute().getName())).findFirst();
            pnrAttribute.ifPresent(attribute -> {
                List<Object> valueList = attribute.getAttribute().getAttributeValue();
                String pnr = valueList.get(0).toString();
                String abteilungsnummer = pnr.substring(0, 1);
                String mitarbeiternummer = pnr.substring(1, 3);
                user.setSingleAttribute("abteilungsnummer", abteilungsnummer);
                user.setSingleAttribute("mitarbeiternummer", mitarbeiternummer);
            });
        });
    }
}
