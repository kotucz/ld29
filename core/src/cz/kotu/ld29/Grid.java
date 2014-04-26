package cz.kotu.ld29;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author tkotula
 */
public class Grid extends Actor {

    final int width = 40;
    final int height = 30;
    final Rectangle mGridRect = new Rectangle(0, 0, width, height);
    // pixels per world unit
    final int WP = 16;
    final int HP = 16;
    private final List<Field> mFields = new ArrayList<Field>();
    private final TextureRegion mTextureGround;
    private final Random random = new Random();
    private Field mOutsideField;

    public Grid() {
        mTextureGround = Tex.get().ground1;

        mOutsideField = new Field();
        // not walkable
        mOutsideField.color = 1;
        mOutsideField.type = FieldType.BEDROCK;

        FieldType[] randomTypes = {FieldType.VOID, FieldType.VOID, FieldType.VOID, FieldType.GROUND, FieldType.GROUND, FieldType.BEDROCK};

        for (int i = 0; i < width * height; i++) {
            Field field = new Field();

            field.type = randomTypes[random.nextInt(randomTypes.length)];
            mFields.add(field);
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Grid.Field field = getField(x, y);

                TextureRegion texture = null;
                switch (field.type) {
                    case GROUND:
                        texture = Tex.get().ground1;
                        break;
                    case BEDROCK:
                        texture = Tex.get().bedrock1;
                        break;
                }
                if (texture != null) {
                    batch.draw(texture, x * WP, y * HP);
                }

            }
        }
    }

    private void drawGrid(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color color = new Color();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Grid.Field field = getField(x, y);
                shapeRenderer.setColor(color.set(field.color));
                shapeRenderer.rect(x * WP, y * HP, WP, HP);
            }
        }
        shapeRenderer.end();
    }

    Field getField(int x, int y) {
        if (mGridRect.contains(x, y)) {
            return mFields.get(x + y * width);
        } else {
            return mOutsideField;
        }
    }

    enum FieldType {
        VOID,
        GROUND,
        // non-destructible
        BEDROCK
    }

    class Field {

        int color = random.nextInt();
        FieldType type = FieldType.VOID;

        boolean isEmpty() {
            switch (type) {
                case VOID:
                    return true;
                case GROUND:
                case BEDROCK:
                default:
                    return false;
            }
        }
    }

}
