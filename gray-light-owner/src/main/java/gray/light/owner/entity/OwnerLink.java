package gray.light.owner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 表示一条链接：指定是在主页上引导的链接
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class OwnerLink {

    private Long id;

    private Long ownerId;

    private String url;

    private String name;

}
