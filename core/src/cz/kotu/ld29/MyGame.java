package cz.kotu.ld29;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MyGame extends ApplicationAdapter {

    SpriteBatch batch;
	Texture img;
    ShapeRenderer shapeRenderer;

    Grid grid;

    // pixels per world unit
    final int WP = 10;
    final int HP = 10;
    private Grid.Field field;


    @Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        shapeRenderer = new ShapeRenderer();
        grid = new Grid();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawGrid();

		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();

    }

    private void drawGrid() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Color color = new Color();
        for (int y = 0; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                field = grid.get(x, y);
                shapeRenderer.setColor(color.set(field.color));
                shapeRenderer.rect(x * WP, y * HP, WP, HP);
            }
        }
        shapeRenderer.end();
    }
}
