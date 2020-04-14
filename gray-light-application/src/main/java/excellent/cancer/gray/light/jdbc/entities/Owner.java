package excellent.cancer.gray.light.jdbc.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

/**
 * 表示整个产品的拥有者
 *
 * @author XyParaCrim
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Owner extends AbstractPersistable<Long> {

    private String username;

    private String organization;

}
