package gray.light.error;

import gray.light.owner.entity.Owner;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;

import java.util.List;

/**
 * 定义获取到的Owner不是唯一的异常
 *
 * @author XyParaCrim
 */
@CommonsLog
public class UniqueOwnerException extends RuntimeException {

    public UniqueOwnerException(String message) {
        super(message);
    }

    /**
     * 从多个owner中提取唯一owner
     *
     * @param owners 所有的owners
     * @return 唯一owner
     * @throws UniqueOwnerException 当存在零个或者多个owner时
     */
    public static Owner extractOwnerRequireUniqueOwner(@NonNull List<Owner> owners) {
        if (owners.size() != 1) {
            if (!owners.isEmpty()) {
                log.error("Obtained owner's data：");
                owners.forEach(log::error);
            }

            throw new UniqueOwnerException("The number of obtained owners" + owners);
        }

        return owners.get(0);
    }

}
