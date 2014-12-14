package Logic;

/**
 * Created by jorgelima on 12-12-2014.
 */
public class CloneableObject implements Cloneable {
    public CloneableObject clone() {
        try {
            return (CloneableObject)super.clone();
        } catch (CloneNotSupportedException err) {
            return null;
        }
    }
}
