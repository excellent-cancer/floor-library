package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;

/**
 * 表示一条链接：指定是在主页上引导的链接
 *
 * @author XyParaCrim
 */
@Data
@AccessType(AccessType.Type.PROPERTY)
@AllArgsConstructor
public class OwnerLink {

    @Id
    private Long id;

    private Long ownerId;

    private String url;

    private String name;

}
