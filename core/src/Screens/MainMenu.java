package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

public class MainMenu implements Screen, InputProcessor {
    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture background, topLeaf, topLeafClicked, midLeaf, midLeafClicked, botLeaf, botLeafClicked, audio, noaudio;
    private boolean top = false, mid = false, bot = false;
    private float countert = 0, counterm = 0, counterb = 0;
    private BitmapFont font, fontsmall;
    private Music song;
    private Sound click;
    private boolean music, credits;
    private float progress;
    private boolean transitioning, exiting;
    private ShapeRenderer shaperenderer;

    public MainMenu(Zahrah game){
        transitioning = false;
        exiting = false;
        credits = false;
        progress = 0;
        Gdx.input.setInputProcessor(this);
        music = true;
        this.game = game;
        shaperenderer = new ShapeRenderer();
        gameCam = new OrthographicCamera();
        gamePort = new FitViewport(game.G_WIDTH, game.G_HEIGHT, gameCam);
        gameCam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2, 0);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Misc/8-BIT WONDER.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (int) Math.ceil(Gdx.graphics.getWidth() / 72.0);
        parameter.color = Color.BLUE;
        font = generator.generateFont(parameter);
        parameter.size = (int) Math.ceil(Gdx.graphics.getWidth() / 99.0);
        fontsmall = generator.generateFont(parameter);
        generator.dispose();
        audio = new Texture("Main Menu/audio.png");
        noaudio = new Texture("Main Menu/no-audio.png");
        background = new Texture("Main Menu/Background.png");
        topLeaf = new Texture("Main Menu/Top-Leaf.png");
        topLeafClicked = new Texture("Main Menu/Top-Leaf-clicked.png");
        midLeaf = new Texture("Main Menu/Mid-Leaf.png");
        midLeafClicked = new Texture("Main Menu/Mid-Leaf-clicked.png");
        botLeaf = new Texture("Main Menu/Bot-Leaf.png");
        botLeafClicked = new Texture("Main Menu/Bot-Leaf-clicked.png");
        song = Gdx.audio.newMusic(Gdx.files.internal("Music/Mystical Forest.mp3"));
        click = Gdx.audio.newSound(Gdx.files.internal("Main Menu/Click.wav"));
        song.play();
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
        if(music){
            game.batch.draw(audio, game.G_WIDTH - 30, 0, 30,30);
        }
        else{
            game.batch.draw(noaudio, game.G_WIDTH - 30, 0, 30,30);
        }
        fontsmall.draw(game.batch, "Exit", 155,130);
        fontsmall.draw(game.batch, "Credits", 230, 80);
        font.draw(game.batch,"Play", 35, 180);
        if(credits){
            game.batch.end();
            shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
            shaperenderer.setColor(4,74,38,1);
            shaperenderer.rect(100,50, game.G_WIDTH - 200, game.G_HEIGHT - 100);
            shaperenderer.end();
            game.batch.begin();
        }
        game.batch.end();
        if(transitioning || exiting){
            CloseScreen(dt);
        }


    }
    private void update(float dt){
        gameCam.update();
    }
    @Override
    public void resize(int i, int i1) {
        gamePort.update(i, i1);
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
        if(exiting && progress >= 1.5){
            this.dispose();
            Gdx.app.exit();
        }
        else if(transitioning && progress >= 1.5){
            song.stop();
            this.dispose();
            game.setScreen(new LevelMap(game, 0));
        }
        Gdx.gl.glDisable(GL20.GL_BLEND);
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
        song.dispose();
        click.dispose();
        font.dispose();
        fontsmall.dispose();
        topLeaf.dispose();
        topLeafClicked.dispose();
        midLeaf.dispose();
        midLeafClicked.dispose();
        botLeaf.dispose();
        botLeafClicked.dispose();
        audio.dispose();
        noaudio.dispose();
        background.dispose();
        shaperenderer.dispose();
    }

    @Override
    public boolean keyDown(int i) {
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
        if(i3 == Input.Buttons.LEFT && !credits){
            Vector3 mouse = gameCam.unproject(new Vector3(i, i1, 0));

            if(game.DEV_MODE) {
                System.out.println("Mouse click at X: " + mouse.x + " Y: " + mouse.y);
            }
            if(mouse.x > 6 && mouse.x < 120 && mouse.y < 225 && mouse.y > 125){
                click.play();
                top = true;
                transitioning = true;
            }
            else if(mouse.x > 120 && mouse.x < 215 && mouse.y < 175 && mouse.y > 80){
                click.play();
                mid = true;
                exiting = true;
            }
            else if(mouse.x > 215 && mouse.x < 330 && mouse.y > 30 && mouse.y < 135) {
                click.play();
                bot = true;
                credits = true;
            }
            else if(mouse.x > game.G_WIDTH - 50 && mouse.x < game.G_WIDTH && mouse.y < 50 && mouse.y > 0){
                if(music){
                    music = false;
                    song.stop();
                }
                else{
                    music = true;
                    song.play();
                }
            }
        }
        if(i3 == Input.Buttons.LEFT && credits){
            Vector3 mouse = gameCam.unproject(new Vector3(i, i1, 0));
            if(mouse.x > 100 && mouse.x < 300 && mouse.y > 100 && mouse.y < 200){
                credits = false;
            }
        }
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
