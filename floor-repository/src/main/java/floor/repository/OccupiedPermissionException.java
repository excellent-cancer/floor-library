package floor.repository;

/**
 * 尝试获取许可，但是许可一直被占有
 *
 * @author XyParaCrim
 */
public class OccupiedPermissionException extends RuntimeException {

    public OccupiedPermissionException(String message) {
        super(message);
    }
}
