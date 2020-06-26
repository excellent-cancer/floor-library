package gray.light.owner.business;

import gray.light.owner.entity.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class OwnerDetailsBo {

    private Long id;

    private String username;

    private String organization;

    public static OwnerDetailsBo of(Owner owner) {
        return new OwnerDetailsBo(
                owner.getId(),
                owner.getUsername(),
                owner.getOrganization()
        );
    }

}
