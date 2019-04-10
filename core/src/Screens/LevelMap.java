package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

public class LevelMap implements Screen {

    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture background;

    public LevelMap(Zahrah game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(game.G_WIDTH, game.G_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        background = new Texture("Map/Test.png");
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        update(dt);
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(background,0,0);
        game.batch.end();
    }

    private void update(float dt){
        handleinput(dt);
    }

    private void handleinput(float dt){
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
        }
    }
    @Override
    public void resize(int i, int i1){
            gamePort.update(i, i1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
