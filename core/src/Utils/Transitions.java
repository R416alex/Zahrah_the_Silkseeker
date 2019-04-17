package Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.r416alex.game.Zahrah;

import Screens.DarkMarket;
import Screens.Level1;
import Screens.Level2;
import Screens.Level3;
import Screens.LevelMap;

public class Transitions {
    private float progress;
    private Zahrah game;
    private ShapeRenderer shaperenderer;
    public boolean exiting, transitioning, opening;
    public int loc, from;

    public Transitions(Zahrah game){
        from = 0;
        exiting = false;
        transitioning = false;
        opening = false;
        progress = 0;
        this.game = game;
        shaperenderer = new ShapeRenderer();
    }

    public void CloseScreen(float dt, int going){
        if(exiting || transitioning) {
            progress += dt;

            if (game.DEV_MODE) {
                System.out.println(progress);
            }
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shaperenderer.begin(ShapeRenderer.ShapeType.Filled);
            shaperenderer.setColor(new Color(0f, 0f, 0f, 1f * (progress / 1.4f)));
            shaperenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shaperenderer.end();
            if (exiting && progress >= 1.5) {
                game.getScreen().dispose();
                Gdx.app.exit();
            } else if (transitioning && progress >= 1.5) {

                game.getScreen().dispose();
                Reset();
                if (going == 4) {
                    game.setScreen(new LevelMap(game, game.player.previous));


                } else if (going == 1) {
                    game.setScreen(new Level1(game));
                } else if (going == 2) {
                    game.setScreen(new Level2(game));
                } else if (going == 3) {
                    game.setScreen(new Level3(game));
                } else if (going == 0) {
                    game.setScreen(new DarkMarket((game)));
                }
                if(going != 4) {
                    game.player.previous = going;
                }


            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
    public void OpenScreen(float dt) {
        if(opening) {
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
                System.out.println( " " + game.player.previous);
                Reset();
            }
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }
    private void Reset(){
        opening = false;
        exiting = false;
        transitioning = false;
        progress = 0;
    }

}
