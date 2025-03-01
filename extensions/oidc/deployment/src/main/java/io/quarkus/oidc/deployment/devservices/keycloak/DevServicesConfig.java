package io.quarkus.oidc.deployment.devservices.keycloak;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;

import io.quarkus.oidc.deployment.DevUiConfig;
import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

@ConfigGroup
public class DevServicesConfig {

    /**
     * If DevServices has been explicitly enabled or disabled.
     * <p>
     * When DevServices is enabled Quarkus will attempt to automatically configure and start
     * Keycloak when running in Dev or Test mode and when Docker is running.
     */
    @ConfigItem(defaultValue = "true")
    public boolean enabled = true;

    /**
     * The container image name to use, for container based DevServices providers.
     *
     * Image with a Quarkus based distribution is used by default.
     * Image with a WildFly based distribution can be selected instead, for example:
     * 'quay.io/keycloak/keycloak:19.0.3-legacy'.
     * <p>
     * Note Keycloak Quarkus and Keycloak WildFly images are initialized differently.
     * By default, Dev Services for Keycloak will assume it is a Keycloak Quarkus image if the image version does not end with a
     * '-legacy'
     * string.
     * Set 'quarkus.keycloak.devservices.keycloak-x-image' to override this check.
     */
    @ConfigItem(defaultValue = "quay.io/keycloak/keycloak:20.0.3")
    public String imageName;

    /**
     * If Keycloak-X image is used.
     *
     * By default, Dev Services for Keycloak will assume a Keycloak-X image is used if the image name contains a 'keycloak-x'
     * string.
     * Set 'quarkus.keycloak.devservices.keycloak-x-image' to override this check which may be necessary if you build custom
     * Keycloak-X or Keycloak images.
     * You do not need to set this property if the default check works.
     */
    @ConfigItem
    public Optional<Boolean> keycloakXImage;

    /**
     * Indicates if the Keycloak container managed by Quarkus Dev Services is shared.
     * When shared, Quarkus looks for running containers using label-based service discovery.
     * If a matching container is found, it is used, and so a second one is not started.
     * Otherwise, Dev Services for Keycloak starts a new container.
     * <p>
     * The discovery uses the {@code quarkus-dev-service-label} label.
     * The value is configured using the {@code service-name} property.
     * <p>
     * Container sharing is only used in dev mode.
     */
    @ConfigItem(defaultValue = "true")
    public boolean shared;

    /**
     * The value of the {@code quarkus-dev-service-keycloak} label attached to the started container.
     * This property is used when {@code shared} is set to {@code true}.
     * In this case, before starting a container, Dev Services for Keycloak looks for a container with the
     * {@code quarkus-dev-service-keycloak} label
     * set to the configured value. If found, it will use this container instead of starting a new one. Otherwise, it
     * starts a new container with the {@code quarkus-dev-service-keycloak} label set to the specified value.
     * <p>
     * Container sharing is only used in dev mode.
     */
    @ConfigItem(defaultValue = "quarkus")
    public String serviceName;

    /**
     * The comma-separated list of class or file system paths to Keycloak realm files which will be used to initialize Keycloak.
     * The first value in this list will be used to initialize default tenant connection properties.
     */
    @ConfigItem
    public Optional<List<String>> realmPath;

    /**
     * The JAVA_OPTS passed to the keycloak JVM
     */
    @ConfigItem
    public Optional<String> javaOpts;

    /**
     * Show Keycloak log messages with a "Keycloak:" prefix.
     */
    @ConfigItem(defaultValue = "false")
    public boolean showLogs;

    /**
     * Keycloak start command.
     * Use this property to experiment with Keycloak start options, see {@link https://www.keycloak.org/server/all-config}.
     * Note it will be ignored when loading legacy Keycloak WildFly images.
     */
    @ConfigItem
    public Optional<String> startCommand;

    /**
     * The Keycloak realm name.
     * This property will be used to create the realm if the realm file pointed to by the 'realm-path' property does not exist,
     * default value is 'quarkus' in this case.
     * If the realm file pointed to by the 'realm-path' property exists then it is still recommended to set this property
     * for Dev Services for Keycloak to avoid parsing the realm file in order to determine the realm name.
     *
     */
    @ConfigItem
    public Optional<String> realmName;

    /**
     * Indicates if the Keycloak realm has to be created when the realm file pointed to by the 'realm-path' property does not
     * exist.
     *
     * Disable it if you'd like to create a realm using Keycloak Administration Console
     * or Keycloak Admin API from {@linkplain io.quarkus.test.common.QuarkusTestResourceLifecycleManager}.
     */
    @ConfigItem(defaultValue = "true")
    public boolean createRealm;

    /**
     * The Keycloak users map containing the username and password pairs.
     * If this map is empty then two users, 'alice' and 'bob' with the passwords matching their names will be created.
     * This property will be used to create the Keycloak users if the realm file pointed to by the 'realm-path' property does
     * not exist.
     */
    @ConfigItem
    public Map<String, String> users;

    /**
     * The Keycloak user roles.
     * If this map is empty then a user named 'alice' will get 'admin' and 'user' roles and all other users will get a 'user'
     * role.
     * This property will be used to create the Keycloak roles if the realm file pointed to by the 'realm-path' property does
     * not exist.
     */
    @ConfigItem
    public Map<String, List<String>> roles;

    /**
     * Grant type.
     *
     * @deprecated Use {@link DevUiConfig#grant}.
     */
    @Deprecated
    public Grant grant = new Grant();

    @ConfigGroup
    public static class Grant {
        public static enum Type {
            /**
             * 'client_credentials' grant
             */
            CLIENT("client_credentials"),
            /**
             * 'password' grant
             */
            PASSWORD("password"),

            /**
             * 'authorization_code' grant
             */
            CODE("code"),

            /**
             * 'implicit' grant
             */
            IMPLICIT("implicit");

            private String grantType;

            private Type(String grantType) {
                this.grantType = grantType;
            }

            public String getGrantType() {
                return grantType;
            }
        }

        /**
         * Grant type which will be used to acquire a token to test the OIDC 'service' applications
         */
        @ConfigItem(defaultValue = "code")
        public Type type = Type.CODE;
    }

    /**
     * Optional fixed port the dev service will listen to.
     * <p>
     * If not defined, the port will be chosen randomly.
     */
    @ConfigItem
    public OptionalInt port;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DevServicesConfig that = (DevServicesConfig) o;
        // grant.type is not checked since it only affects which grant is used by the Dev UI provider.html
        // and as such the changes to this property should not cause restarting a container
        return enabled == that.enabled
                && Objects.equals(imageName, that.imageName)
                && Objects.equals(port, that.port)
                && Objects.equals(realmPath, that.realmPath)
                && Objects.equals(realmName, that.realmName)
                && Objects.equals(users, that.users)
                && Objects.equals(javaOpts, that.javaOpts)
                && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, imageName, port, realmPath, realmName, users, roles);
    }
}
