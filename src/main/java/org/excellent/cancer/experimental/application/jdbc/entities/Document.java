package org.excellent.cancer.experimental.application.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.With;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * 项目中的一篇文档
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class Document {

    @Id @With
    private final Long id;

    private String title;

    @With
    private final Date createdDate;

    private Date updatedDate;

    private String downloadUrl;
}
