package cz.kotu.ld29;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.*;

/**
 * @author tkotula
 */
public class Grid extends Actor {

    public static final float EMPTY_PROBABILITY = 0.4f;
    final int SURFACE_HEIGHT = 10;

    final int width = 40;
    final int height = 30;
    // pixels per world unit
    final int WP = 16;
    final int HP = 16;
    private final List<Field> mFields = new ArrayList<Field>();
    private final Random random = new Random();
    private Field mOutsideField;

    public Grid() {

        mOutsideField = new Field();
        // not walkable
        mOutsideField.color = 1;
//        mOutsideField.type = FieldType.BEDROCK;
        mOutsideField.mStore.content = FieldType.BEDROCK;

        FieldType[] randomTypes = {FieldType.VOID, FieldType.VOID, FieldType.VOID, FieldType.GROUND, FieldType.GROUND, FieldType.BEDROCK};
        FieldType[] randomFullTypes = {FieldType.GROUND, FieldType.GROUND, FieldType.STONE, FieldType.BEDROCK};

        //        for (int i = 0; i < width * height; i++) {
        // this order is somehow important!
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Field field = new Field();
                mFields.add(field);

                if (SURFACE_HEIGHT < y) {
                    // fresh air
                    field.mStore.content = FieldType.VOID;
                } else {
                    float emptyProb = random.nextFloat();
                    if ((emptyProb < EMPTY_PROBABILITY)) {
                        field.mStore.content = FieldType.VOID;
                        if (random.nextFloat() < 0.1f) {
                            field.mStore.content = FieldType.LEAF;
                        } else if (random.nextFloat() < 0.1f) {
                            field.mStore.content = FieldType.SUPPORT;
                        }
                    } else {
                        field.mStore.content = randomFullTypes[random.nextInt(randomFullTypes.length)];
                    }
                }
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // clear from previous step
        for (Field field : mFields) {
            field.occupants.clear();
        }
        // update which fields contains ants
        for (Actor actor : getStage().getActors()) {
            if (actor instanceof MyGame.Ant) {
                MyGame.Ant ant = (MyGame.Ant) actor;
                getField(ant.pos).occupants.add(ant);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int border = 2;
        for (int y = 0 - border; y < height + border; y++) {
            for (int x = 0 - border; x < width + border; x++) {
                Grid.Field field = getField(x, y);

                TextureRegion texture = getTextureForType(field.mStore);
                if (texture != null) {
                    batch.draw(texture, x * WP, y * HP);
                }

            }
        }
    }

    TextureRegion getTextureForType(Store store) {
        if (store.isEmpty()) {
            return null;
        }
        switch (store.content) {
            case GROUND:
                return Tex.get().ground1;
            case BEDROCK:
                return Tex.get().bedrock1;
            case STONE:
                return Tex.get().stone1;
            case SUPPORT:
                return Tex.get().support1;
            case LEAF:
                return Tex.get().leaf1;
            default:
                return Tex.get().questionMark;
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

    Field getField(Vec pos) {
        return getField(pos.x, pos.y);
    }

    Field getField(int x, int y) {
        if (0 <= x && x < this.width && 0 <= y && y < height) {
            return mFields.get(x + y * width);
        } else {
            return mOutsideField;
        }
    }

    class Field {

        public Body lightBox;
        int color = random.nextInt();
        //        FieldType type = FieldType.VOID;
        Set<MyGame.Ant> occupants = new HashSet<MyGame.Ant>();
        Store mStore = new Store();

        boolean isEmpty() {
            if (!occupants.isEmpty()) {
                return false;
            }
            if (mStore.isEmpty()) {
                return true;
            }
            switch (mStore.content) {
                case VOID:
                case SUPPORT:
                case LEAF:
                    return true;
                case GROUND:
                case BEDROCK:
                default:
                    return false;
            }
        }

        boolean blocksLight() {
            if (mStore.isEmpty()) {
                return false;
            }
            switch (mStore.content) {
                case GROUND:
                case STONE:
                case BEDROCK:
                    return true;

                case VOID:
                case SUPPORT:
                case LEAF:
                default:
                    return false;
            }
        }
    }

}