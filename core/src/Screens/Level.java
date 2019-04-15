package Screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

public abstract class Level implements Screen, InputProcessor {
    private int location;
    private TiledMap map;
    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;

    private Level(){

        map = new TmxMapLoader().load("ProjectNeon.tmx");
    }
}
