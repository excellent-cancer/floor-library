package gray.light.owner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.type.Alias;

/**
 * 表示整个产品的拥有者
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
@Alias("Owner")
public class Owner {

    private Long id;

    private String username;

    private String organization;

    private Privilege superPrivilege;

}
