package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;
public class LevelMap implements Screen, InputProcessor {

    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private Texture background, trunks, leftt,rightt,middle,leftDark,middleDark,rightDark;
    private float progress;
    private boolean opening,closing, right, left;
    private ShapeRenderer shaperenderer;
    private int loc;
    private int[] worlds = {52, 148, 257, 335};
    private float x, counter;
    private Sprite current;
    private Texture walkingSprite;
    private final float walkfps = 14;
    public final int TextureWIDTH = 20;
    public final int TextureHEIGHT = 30;
    public int lastLook;

    public LevelMap(Zahrah game, int level) {
        loc = level;
        counter = 0;
        lastLook = 1;
        x = worlds[loc];
        right = false;
        left = false;
        if(game.player.cape && !game.player.gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingC.png");
        }else if(!game.player.cape && game.player.gloves) {
            walkingSprite = new Texture("Characters/Zahrah/WalkingG.png");
        }else if(game.player.cape && game.player.gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingCG.png");
        }else{
            walkingSprite = new Texture("Characters/Zahrah/Walking.png");
        }
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
        rightt = new Texture("Map/Right.png");
        leftt = new Texture("Map/Left.png");
        middle = new Texture("Map/Middle.png");
        leftDark = new Texture("Map/LeftDark.png");
        rightDark = new Texture("Map/RightDark.png");
        middleDark = new Texture("Map/MiddleDark.png");
        getCurrentSprite(0);
        game.transitions.opening = true;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        this.update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        if(!game.player.level2){
            game.batch.draw(rightDark, 0, 0);
        }else{
            game.batch.draw(rightt,0,0);
        }
        if(!game.player.level1){
            game.batch.draw(middleDark,0,0);
        }else{
            game.batch.draw(middle,0,0);
        }
        if(!game.player.darkMarket){
            game.batch.draw(leftDark,0,0);
        }else{
            game.batch.draw(leftt,0,0);
        }
        current.draw(game.batch);
        game.batch.end();

        game.batch.begin();
        game.batch.draw(trunks, 0, 0);
        game.batch.end();
        game.transitions.OpenScreen(dt);

        game.transitions.CloseScreen(dt, loc);

    }

    private void update(float dt) {
        getCurrentSprite(dt);
        current.setPosition(x-4,24);
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
        if(!game.transitions.opening || !game.transitions.exiting || !game.transitions.transitioning) {
            if(i == Input.Keys.D ){
                lastLook = 1;
            }else if(i == Input.Keys.A){
                lastLook = 0;
            }
            if (i == Input.Keys.D && loc != 3 && !right && !left) {
                if((loc == 0 && game.player.darkMarket)||(loc == 1 && game.player.level1) || (loc == 2 && game.player.level2)) {
                    right = true;
                    loc += 1;
                }
            } else if (i == Input.Keys.A && loc != 0 && !left && !right) {
                left = true;
                loc -= 1;

            }
            if (!right && !left && i == Input.Keys.SPACE) {
                game.transitions.transitioning = true;
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        if(!game.transitions.opening || !game.transitions.exiting || !game.transitions.transitioning) {
            if (i == Input.Keys.ESCAPE) {
                Gdx.app.exit();
            }
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
    public void getCurrentSprite(float dt) {

        if (left) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 8)) {
                counter = counter - ((1 / walkfps) * 8);
            }
            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(walkingSprite, 0, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
                System.out.println(counter);
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (6 * (1 / walkfps)) && counter < (7 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (7 * (1 / walkfps)) && counter < (8 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 7, 0, TextureWIDTH, TextureHEIGHT));
            }
            lastLook = 0;
        } else if (right) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 8)) {
                counter = counter - ((1 / walkfps) * 8);
            }

            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 7, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 6, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 5, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 4, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 3, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 2, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (6 * (1 / walkfps)) && counter < (7 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 1, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (7 * (1 / walkfps)) && counter < (8 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            }
            lastLook = 1;
        }else if(lastLook==1){
            counter = 0;
            current = new Sprite(new TextureRegion(walkingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
        }else if(lastLook == 0){
            counter = 0;
            current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 7, 0, TextureWIDTH, TextureHEIGHT));
        }
    }
}
