package de.akdb.iam.keycloak.extension.samlattributeprotocollmapper;

import com.google.auto.service.AutoService;
import lombok.extern.jbosslog.JBossLog;
import org.keycloak.dom.saml.v2.assertion.AttributeStatementType;
import org.keycloak.dom.saml.v2.assertion.AttributeType;
import org.keycloak.models.AuthenticatedClientSessionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.ProtocolMapperModel;
import org.keycloak.models.UserSessionModel;
import org.keycloak.protocol.ProtocolMapper;
import org.keycloak.protocol.saml.mappers.AbstractSAMLProtocolMapper;
import org.keycloak.protocol.saml.mappers.SAMLAttributeStatementMapper;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ProviderConfigurationBuilder;
import org.keycloak.saml.common.constants.JBossSAMLURIConstants;

import java.util.List;
import java.util.Objects;

@JBossLog
@AutoService(ProtocolMapper.class)
public class SamlAttributeMapper extends AbstractSAMLProtocolMapper implements SAMLAttributeStatementMapper {

    public static final String CONFIG_SOURCE = "source";
    public static final String CONFIG_TARGET = "target";
    public static final List<ProviderConfigProperty> CONFIG_PROPERTIES = ProviderConfigurationBuilder.create()
            .property()
            .name(CONFIG_SOURCE)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("Quell-Attribut")
            .helpText("Benutzerdefiniertes Benutzer-Attribut das abgebildet werden soll.")
            .add()
            .property()
            .name(CONFIG_TARGET)
            .type(ProviderConfigProperty.STRING_TYPE)
            .label("Ziel-Attribut")
            .helpText("Name des SAML-Attributs im Token.")
            .add()
            .build();

    @Override
    public String getDisplayCategory() {
        return "SAML";
    }

    @Override
    public String getDisplayType() {
        return "AKDB SAML Attribute Mapper";
    }

    @Override
    public void transformAttributeStatement(AttributeStatementType attributeStatementType, ProtocolMapperModel protocolMapperModel, KeycloakSession keycloakSession, UserSessionModel userSessionModel, AuthenticatedClientSessionModel authenticatedClientSessionModel) {
        // Config auslesen
        String source = protocolMapperModel.getConfig().get(CONFIG_SOURCE);
        String target = protocolMapperModel.getConfig().get(CONFIG_TARGET);

        if (Objects.isNull(source) || Objects.isNull(target)) {
            log.debugf("Aborted mapping due to missing source or target name");
            return;
        }

        // User-Wert holen
        String value = userSessionModel.getUser().getFirstAttribute(source);
        if (Objects.isNull(value)) {
            log.debugf("Aborted due to missing value for source attribute '%s'", source);
            return;
        }

        // SAML-Attribute setzen
        AttributeType attributeType = new AttributeType("Custom");
        attributeType.setName(target);
        attributeType.setFriendlyName("Attribute " + target);
        attributeType.setNameFormat(JBossSAMLURIConstants.ATTRIBUTE_FORMAT_BASIC.get());
        attributeType.addAttributeValue(value);
        attributeStatementType.addAttribute(new AttributeStatementType.ASTChoiceType(attributeType));
    }

    @Override
    public String getHelpText() {
        return "Attribute in SAML-Token einfügen.";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return CONFIG_PROPERTIES;
    }

    @Override
    public String getId() {
        return "akdb-saml-attribute-protocol-mapper";
    }
}
