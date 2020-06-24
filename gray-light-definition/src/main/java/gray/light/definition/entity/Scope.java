package gray.light.definition.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 记录该应用所有范围，例如文档，笔记等
 *
 * @author XyParaCrim
 */
@Data
@AllArgsConstructor
public class Scope {

    private String name;

    private String description;

    public final static Scope CODING = new Scope("coding", "");

    public final static Scope DOCUMENT = new Scope("document", "");

    public final static Scope NOTE = new Scope("note", "");

    public final static Scope BLOG = new Scope("blog", "");

}
