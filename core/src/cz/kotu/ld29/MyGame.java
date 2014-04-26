package cz.kotu.ld29;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import static cz.kotu.ld29.Tex.Tex;

public class MyGame extends ApplicationAdapter {

    SpriteBatch batch;

    ShapeRenderer shapeRenderer;

    Grid mGrid;

    Stage mStage;

    ExtendViewport mViewport;

    @Override
    public void create() {
        Tex.Tex = new Tex();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // emulated display size
        mViewport = new ExtendViewport(160, 120);
        mStage = new Stage(mViewport);

        Gdx.input.setInputProcessor(mStage);

        mGrid = new Grid();
        mStage.addActor(mGrid);
        Ant ant = new Ant();
        mStage.addActor(ant);

    }

    @Override
    public void resize(int width, int height) {
        mStage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        mStage.dispose();
    }

    @Override
    public void render() {

        float grey = 0.1f;

        Gdx.gl.glClearColor(grey, grey, grey, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        drawGrid();

        batch.setProjectionMatrix(mViewport.getCamera().combined);

        batch.begin();

//        batch.draw(badlogic, 0, 0);

//        for (Grid.Ant ant : mGrid.mFields) {
//            batch.draw(badlogic, ant.pos.x, ant.pos.y);
//        }

        batch.end();

        mStage.act(Gdx.graphics.getDeltaTime());
        mStage.draw();

    }

    public class Ant extends Actor {

        public static final float FLIP_DURATION = 0.25f;
        // grid position
        final Vec pos = new Vec();
        int hdir = 1;

        public Ant() {
            setSize(16, 16);
            setPosition(4 * mGrid.WP, 2 * mGrid.HP);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (getActions().size == 0) {
                Action nextAction = getNextAction();
                if (nextAction != null) {
                    addAction(nextAction);
                }
            } else {
                // just animate
            }
        }

        private Action getNextAction() {
            boolean doRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
            boolean doLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
            boolean doUp = Gdx.input.isKeyPressed(Input.Keys.UP);
            boolean doDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

            pos.set(MathUtils.round(getX() / mGrid.WP), MathUtils.round(getY() / mGrid.HP));

            boolean freeRight = relativeField(1, 0).isEmpty();
            boolean freeLeft = relativeField(-1, 0).isEmpty();
            boolean freeUp = relativeField(0, 1).isEmpty();
            boolean freeDown = relativeField(0, -1).isEmpty();

            if (doRight != doLeft) {
                int dir = ((doRight ? 1 : 0) + (doLeft ? -1 : 0));
                if (dir != hdir) {
                    hdir = dir;
//                  flipSide:
                    return Actions.delay(FLIP_DURATION);
                }
            }

            boolean onGround = !relativeField(0, -1).isEmpty();
            boolean climbing = !relativeField(hdir, 0).isEmpty();
            boolean onLedge = !relativeField(hdir, -1).isEmpty();

            doRight &= freeRight;
            doLeft &= freeLeft;
            doUp &= freeUp;
            doDown &= freeDown;

            int dx = (doRight != doLeft) ? ((doRight ? 1 : 0) + (doLeft ? -1 : 0)) : 0;
            int dy = (doUp != doDown) ? ((doUp ? 1 : 0) + (doDown ? -1 : 0)) : 0;
            if (!(onGround || climbing || onLedge)) {
                // fall
                return getMoveAction(0, -1);
            } else if (dx != 0) {
                if (onGround) {
                    // ok
                    return getMoveAction(dx, 0);
                } else {
                    if (onLedge) {
                        // ok - climbed over ledge
                        return getMoveAction(dx, 0);
                    }
                }
            } else if (dy < 0) {
                // descent
                return getMoveAction(0, -1);
            } else if (dy > 0) {
                if (climbing) {
                    // climb
                    return getMoveAction(0, 1);
                } else {
                    // can not climb
                    dy = 0;
                }
            }

            // grid coordinates
//            Grid.Field field = mGrid.getField(pos.x + dx, pos.y + dy);
//            Gdx.app.log("Ant", "g " + pos.x + ", " + pos.y + " d " + dx + ", " + dy + " " + field.isEmpty());

            return null;
        }

        private Action getMoveAction(int dx, int dy) {
            if (dx != 0 && dx != hdir) {
                // need to flip first
                hdir = dx;
//                  flipSide:
                return Actions.delay(FLIP_DURATION);
            } else {
                float duration = 0.5f;
                // animate move:
                return Actions.moveBy(mGrid.WP * dx, mGrid.HP * dy, duration);
            }
        }

        private Grid.Field relativeField(int dx, int dy) {
            return mGrid.getField(pos.x + dx, pos.y + dy);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            TextureRegion texture = (headsRight()) ? Tex.redAntRight : Tex.redAntLeft;
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }

        boolean headsRight() {
            return hdir > 0;
        }
    }

}
