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
    TextureRegion redAntQueeen = subregion(4, 1);

    TextureRegion ground1 = subregion(1, 0);
    TextureRegion stone1 = subregion(4, 0);
    TextureRegion bedrock1 = subregion(2, 0);
    TextureRegion support1 = subregion(5, 0);
    TextureRegion glass1 = subregion(6, 0);

    TextureRegion leaf1 = subregion(0, 2);

    TextureRegion questionMark = subregion(0, 3);


    public Tex() {

    }

    static Tex get() {
        return Tex;
    }

    private TextureRegion subregion(int col, int row) {
        return new TextureRegion(pixelArt, col * 16, row * 16, 16, 16);
    }

}
