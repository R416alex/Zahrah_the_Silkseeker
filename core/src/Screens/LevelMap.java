package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

public class LevelMap implements Screen, InputProcessor {

    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture background, trunks;
    private float progress;
    private boolean opening,closing, right, left;
    private ShapeRenderer shaperenderer;
    private int loc;
    private int[] worlds = {52, 148, 257, 335};
    private float x;

    LevelMap(Zahrah game, int level) {
        loc = level;
        x = worlds[loc];
        right = false;
        left = false;
        progress = 0;
        opening = true;
        closing = false;
        Gdx.input.setInputProcessor(this);
        shaperenderer = new ShapeRenderer();
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(game.G_WIDTH, game.G_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        background = new Texture("Map/Background.png");
        trunks = new Texture("Map/Trunks.png");

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
        game.batch.draw(background, 0, 0);
        game.batch.end();

        shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        shaperenderer.setColor(Color.ORANGE);
        shaperenderer.rect(gameCam.project(new Vector3(x,0,0)).x, 75, 32,48);
        shaperenderer.end();

        game.batch.begin();
        game.batch.draw(trunks, 0, 0);

        game.batch.end();


        if (opening) {
            OpenScreen(dt);
        }
        if (closing) {
            CloseScreen(dt);
        }
    }

    private void update(float dt) {
        if(game.DEV_MODE){
            System.out.println(x+ " "+ loc + " " + right + " " + left);
        }

        if(right){
            x += dt * 100;
            if(x >= worlds[loc]){
                x = worlds[loc];
                right = false;
            }
        } else if(left){
            x -= dt * 100;
            if(x <= worlds[loc]){
                x = worlds[loc];
                left = false;
            }
        }
        gameCam.update();
    }

    private void OpenScreen(float dt) {
        progress += dt;
        if (game.DEV_MODE) {
            System.out.println(progress);
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        shaperenderer.setColor(new Color(0f, 0f, 0f, 1f * (1 - (progress / 1.4f))));
        shaperenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaperenderer.end();
        if (opening && progress >= 1.5) {
            progress = 0;
            opening = false;
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }
    private void CloseScreen(float dt){
        progress += dt;
        if(game.DEV_MODE) {
            System.out.println(progress);
        }
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
        shaperenderer.setColor(new Color(0f, 0f, 0f, 1f * (progress/1.4f)));
        shaperenderer.rect(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        shaperenderer.end();
        if(closing && progress >= 1.5){
            this.dispose();
            game.setScreen(new Level1(game));
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }


    @Override
    public void resize(int i, int i1) {
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
        background.dispose();
        trunks.dispose();
        shaperenderer.dispose();
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.D && loc != 3 && !right && !left) {
            right = true;
            loc += 1;
        } else if (i == Input.Keys.A && loc != 0 && !left && !right)
        {
            left = true;
            loc -= 1;
        }
        if(!right && !left && i == Input.Keys.SPACE){
            if(loc == 1){
                closing = true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        if(i == Input.Keys.ESCAPE){
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }
}
