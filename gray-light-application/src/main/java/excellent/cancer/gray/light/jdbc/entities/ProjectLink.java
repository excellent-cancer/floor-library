package excellent.cancer.gray.light.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.AccessType;

/**
 * 表示一条链接：指定是在项目上引导的链接
 *
 * @author XyParaCrim
 */
@Data
@AccessType(AccessType.Type.PROPERTY)
@AllArgsConstructor
public class ProjectLink {

    private String url;

    private String name;

}
