package cz.kotu.ld29;

/**
 * @author tkotula
 */
public class Vec {

    public int x;
    public int y;

    public Vec() {
    }

    public Vec(int x, int y) {
        set(x, y);
    }

    public static Vec add(Vec pos, Vec dir) {
        return new Vec(pos.x + dir.x, pos.y + dir.y);
    }

    public void add(Vec dir) {
        this.x += dir.x;
        this.y += dir.y;
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Vec to) {
        set(to.x, to.y);
    }

    @Override
    public String toString() {
        return "Vec{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


}
