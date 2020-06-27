package gray.light.note.business;

import gray.light.owner.entity.OwnerProject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NoteBo {

    private final OwnerProject note;

    public static NoteBo of(OwnerProject note) {
        return new NoteBo(note);
    }
}
