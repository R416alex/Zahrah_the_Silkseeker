package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

public class MainMenu implements Screen {
    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture background, topLeaf, topLeafClicked, midLeaf, midLeafClicked, botLeaf, botLeafClicked;
    private boolean top = false, mid = false, bot = false;
    private float countert = 0, counterm = 0, counterb = 0;
    public MainMenu(Zahrah game){
        this.game = game;
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(game.G_WIDTH, game.G_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        background = new Texture("Background.png");
        topLeaf = new Texture("Top-Leaf.png");
        topLeafClicked = new Texture("Top-Leaf-clicked.png");
        midLeaf = new Texture("Mid-Leaf.png");
        midLeafClicked = new Texture("Mid-Leaf-clicked.png");
        botLeaf = new Texture("Bot-Leaf.png");
        botLeafClicked = new Texture("Bot-Leaf-clicked.png");
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
        if(bot){
            game.batch.draw(botLeafClicked,0,0);
            counterb += dt;
            if(counterb > 1){
                bot = false;
                counterb = 0;
            }
        }
        else {
            game.batch.draw(botLeaf, 0, 0);
        }
        if(mid) {
            game.batch.draw(midLeafClicked, 0, 0);
            counterm += dt;
            if(counterm > 1){
                mid = false;
                counterm = 0;
            }
        }
        else {
            game.batch.draw(midLeaf, 0, 0);
        }
        if(top){
            game.batch.draw(topLeafClicked,0,0);
            countert += dt;
            if(countert > 1){
                top = false;
                countert = 0;
            }
        }
        else {
            game.batch.draw(topLeaf, 0, 0);
        }
        game.batch.end();
    }
    private void update(float dt){
        handleInput(dt);
        gameCam.update();
    }
    @Override
    public void resize(int i, int i1) {
        gamePort.update(i, i1);
    }
    private void handleInput(float dt) {
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            Vector3 mouse = gameCam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));

            System.out.println(Gdx.input.getX() + " " + Gdx.input.getY());
            if(mouse.x > 6 && mouse.x < 120 && mouse.y < 225 && mouse.y > 125){
                top = true;
            }
            else if(mouse.x > 120 && mouse.x < 215 && mouse.y < 175 && mouse.y > 80){
                mid = true;
            }
            else if(mouse.x > 215 && mouse.x < 330 && mouse.y > 30 && mouse.y < 135) {
                bot = true;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
            Gdx.app.exit();
            //System.exit(1);
        }
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
