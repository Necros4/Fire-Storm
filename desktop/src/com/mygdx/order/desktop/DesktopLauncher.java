package com.mygdx.order.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.order.Drop;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Big Order";
        config.height = 480;
        config.width = 800;

        new LwjglApplication(new Drop(0, 0, 800 ,480), config);
    }
}
