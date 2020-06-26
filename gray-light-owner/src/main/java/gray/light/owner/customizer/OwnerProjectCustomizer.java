package gray.light.owner.customizer;


import gray.light.definition.entity.Scope;
import gray.light.owner.business.OwnerProjectFo;
import gray.light.owner.entity.OwnerProject;

import java.util.Map;

public final class OwnerProjectCustomizer {

    private static final String PROPERTY_NAME = "name";

    private static final String PROPERTY_DESCRIPTION = "description";

    private static final String PROPERTY_HOME_PAGE = "homePage";

    public static OwnerProject uncheck(Object name, Object description) {
        return OwnerProject.builder().
                name((String) name).
                description((String) description).
                build();
    }

    public static OwnerProject extractFromMap(Long ownerId, Scope scope, Map<String, Object> properties) {
        OwnerProject.OwnerProjectBuilder builder = OwnerProject.builder();

        if (properties.containsKey(PROPERTY_NAME)) {
            builder.name((String) properties.get(PROPERTY_NAME));
        }

        if (properties.containsKey(PROPERTY_DESCRIPTION)) {
            builder.description((String) properties.get(PROPERTY_DESCRIPTION));
        }

        if (properties.containsKey(PROPERTY_HOME_PAGE)) {
            builder.homePage((String) properties.get(PROPERTY_HOME_PAGE));
        }

        return builder.
                ownerId(ownerId).
                scope(scope.getName()).
                build();
    }

    public static OwnerProject fromForm(OwnerProjectFo form, Scope scope) {
        return OwnerProject.
                builder().
                ownerId(form.getOwnerId()).
                name(form.getName()).
                description(form.getDescription()).
                homePage(form.getHomePage()).
                scope(scope.getName()).
                build();
    }

}
