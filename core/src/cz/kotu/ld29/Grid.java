package cz.kotu.ld29;

import java.util.Random;

/**
 * @author tkotula
 */
public class Grid {

    int width = 64;
    int height = 48;

    Field get(int x, int y) {
        Field field = new Field();
        field.color = new Random().nextInt();
        return field;
    }

    class Field {
        int color;
    }

}
