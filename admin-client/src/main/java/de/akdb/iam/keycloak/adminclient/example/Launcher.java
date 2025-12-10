package de.akdb.iam.keycloak.adminclient.example;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.RealmRepresentation;

public class Launcher {
    public static void main(String... args) {
        Keycloak adminClient = Keycloak.getInstance("http://localhost:8080", "master", "admin", "admin", "admin-cli");
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm("admin-example");
        if (adminClient.realm(realm.getRealm()) == null) {
            adminClient.realms().create(realm);
        } else {
            realm = adminClient.realm(realm.getRealm()).toRepresentation();
        }
        realm.setEnabled(true);
        adminClient.realm(realm.getRealm()).update(realm);

        adminClient.realm(realm.getRealm()).users().list().stream().forEach(user -> {
            adminClient.realm("admin-example").users().delete(user.getId());
            System.out.println("Deleted user: " + user.getUsername());
        });
    }
}
