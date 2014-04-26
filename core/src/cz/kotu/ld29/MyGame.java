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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class MyGame extends ApplicationAdapter {

    SpriteBatch batch;

    ShapeRenderer shapeRenderer;

    Grid mGrid;

    Stage mStage;

    ExtendViewport mViewport;

    @Override
    public void create() {
        Tex.sInstance = new Tex();

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

        // grid position
        final Vec pos = new Vec();
        TextureRegion region;

        public Ant() {
            region = Tex.get().redAnt;
            setSize(16, 16);
            setPosition(4 * mGrid.WP, 2 * mGrid.HP);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (getActions().size == 0) {

                boolean doRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
                boolean doLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT);
                boolean doUp = Gdx.input.isKeyPressed(Input.Keys.UP);
                boolean doDown = Gdx.input.isKeyPressed(Input.Keys.DOWN);

                pos.set(MathUtils.round(getX() / mGrid.WP), MathUtils.round(getY() / mGrid.HP));

                boolean freeRight = (mGrid.getField(pos.x + 1, pos.y).isWalkable());
                boolean freeLeft = (mGrid.getField(pos.x - 1, pos.y).isWalkable());
                boolean freeUp = (mGrid.getField(pos.x, pos.y + 1).isWalkable());
                boolean freeDown = (mGrid.getField(pos.x, pos.y - 1).isWalkable());

                doRight &= freeRight;
                doLeft &= freeLeft;
                doUp &= freeUp;
                doDown &= freeDown;

                int dx = 0;
                int dy = 0;
                if (doRight != doLeft) {
                    dx = ((doRight ? 1 : 0) + (doLeft ? -1 : 0));
                } else if (doUp != doDown) {
                    dy = ((doUp ? 1 : 0) + (doDown ? -1 : 0));
                }

                // grid coordinates
                Grid.Field field = mGrid.getField(pos.x + dx, pos.y + dy);
                Gdx.app.log("Ant", "g " + pos.x + ", " + pos.y + " d " + dx + ", " + dy + " " + field.isWalkable());

                if ((dx != 0 || dy != 0) && field.isWalkable()) {
                    float duration = 0.5f;
                    addAction(Actions.moveBy(mGrid.WP * dx, mGrid.HP * dy, duration));
                }
            } else {
                // just animate
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }

}
