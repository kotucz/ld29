package cz.kotu.ld29;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
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
        mStage.addActor(new Ant());

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

        TextureRegion region;

        public Ant() {
            region = Tex.get().redAnt;
            setSize(16, 16);
            setPosition(32, 32);
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
