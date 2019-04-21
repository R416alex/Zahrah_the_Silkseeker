package Screens;

import com.r416alex.game.Zahrah;


public class Level1 extends mapLevel{
    public Zahrah game;

    public Level1(Zahrah game) {
        super(game, 1);
        this.game = game;
    }

    @Override
    public void render(float dt) {
        update(dt);
        super.render(dt);
    }
    public void update(float dt) {
        if(game.player.body.getPosition().x > 77f){
            game.player.level1 = true;
            game.transitions.transitioning = true;
        }
        if(game.player.body.getPosition().x < 0.5f){
            game.transitions.transitioning = true;
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
