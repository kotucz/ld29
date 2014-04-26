package cz.kotu.ld29;

/**
 * @author tkotula
 */
public class Vec {

    public int x;
    public int y;

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec to) {
        set(to.x, to.y);
    }

}
