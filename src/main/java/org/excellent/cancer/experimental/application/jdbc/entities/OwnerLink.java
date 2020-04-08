package org.excellent.cancer.experimental.application.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.relational.core.mapping.Table;

/**
 * 表示一条链接：指定是在主页上引导的链接
 *
 * @author XyParaCrim
 */
@Data
@AccessType(AccessType.Type.PROPERTY)
@AllArgsConstructor
public class OwnerLink {

    private String url;

    private String name;

}
