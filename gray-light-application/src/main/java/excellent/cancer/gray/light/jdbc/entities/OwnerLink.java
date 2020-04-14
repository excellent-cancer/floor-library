package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 表示一条链接：指定是在主页上引导的链接
 *
 * @author XyParaCrim
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class OwnerLink extends AbstractPersistable<Long> {

    private Long ownerId;

    private String url;

    private String name;

}
