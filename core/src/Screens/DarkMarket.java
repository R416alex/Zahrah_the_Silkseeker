package Screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.r416alex.game.Zahrah;

import Utils.Physics;

public class DarkMarket extends mapLevel {
    public Zahrah game;



    public DarkMarket(Zahrah game) {

        super(game, 0);
        this.game = game;
    }

    @Override
    public void render(float dt) {
        update(dt);
        super.render(dt);


    }

    public void update(float dt) {

        if(game.player.body.getPosition().x < 0.5f){
            game.player.darkMarket = true;
            game.transitions.transitioning = true;
        }
        cape.setPosition(105,50);
        pepper.setPosition(345,45);
        gloves.setPosition(274,46);
        if(Physics.toPixels(game.player.body.getPosition().x) > 80 && Physics.toPixels(game.player.body.getPosition().x) < 130 && Physics.toPixels(game.player.body.getPosition().y) < 60){
            buyzone = 1;
        }
        else if(Physics.toPixels(game.player.body.getPosition().x) > 249 && Physics.toPixels(game.player.body.getPosition().x) < 299 && Physics.toPixels(game.player.body.getPosition().y) < 60){
            buyzone = 2;
        }
        else if(Physics.toPixels(game.player.body.getPosition().x) > 320 && Physics.toPixels(game.player.body.getPosition().x) < 370 && Physics.toPixels(game.player.body.getPosition().y) < 60){
            buyzone = 3;
        }
        else{
            buyzone = 0;
        }
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

    @Override
    public void show() {

    }



    @Override
    public void resize(int i, int i1) {
        super.resize(i,i1);
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
        // world.dispose();

    }
}