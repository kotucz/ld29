package cz.kotu.ld29;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * @author tkotula
 */
public class Tex {

    static Tex Tex;
    Texture badlogic = new Texture("badlogic.jpg");
    Texture pixelArt = new Texture("pixelart.png");
    TextureRegion blackAnt = subregion(1, 1);
    //    TextureRegion redAnt = subregion(2, 1);
    TextureRegion redAntRight = subregion(3, 1);
    TextureRegion redAntLeft = new TextureRegion(redAntRight) {{
        flip(true, false);
    }};

    TextureRegion ground1 = subregion(1, 0);
    TextureRegion bedrock1 = subregion(2, 0);


    public Tex() {

    }

    static Tex get() {
        return Tex;
    }

    private TextureRegion subregion(int col, int row) {
        return new TextureRegion(pixelArt, col * 16, row * 16, 16, 16);
    }

}
