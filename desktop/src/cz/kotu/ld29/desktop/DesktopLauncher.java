package cz.kotu.ld29.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import cz.kotu.ld29.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Ant Slaves :: by Tomas Kotula (Kotucz) :: for Ludum Dare 29";
        config.addIcon("icon32.png", Files.FileType.Internal);
		new LwjglApplication(new MyGame(), config);
	}
}
