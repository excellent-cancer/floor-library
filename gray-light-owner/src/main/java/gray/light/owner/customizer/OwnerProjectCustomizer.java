package gray.light.owner.customizer;


import gray.light.owner.entity.OwnerProject;

public final class OwnerProjectCustomizer {

    public static OwnerProject uncheck(Object name, Object description) {
        return OwnerProject.builder().
                name((String) name).
                description((String) description).
                build();
    }

}
