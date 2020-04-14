package excellent.cancer.gray.light.jdbc.entities;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractTimestampPersistable<ID extends Serializable> {

    /**
     * 实体ID
     */
    @Id
    @GeneratedValue
    private ID id;

    /**
     * 创建日期
     */
    @CreatedDate
    private Date createdDate;

    /**
     * 最近修改日期
     */
    @LastModifiedDate
    private Date updatedDate;

}
