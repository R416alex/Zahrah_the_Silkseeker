package com.r416alex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

import Screens.mapLevel;
import Utils.Physics;

public class Scorpion {

    public Zahrah game;
    public float counter, progress;
    public final float walkfps = 8;
    public final float dyefps = 10;
    public final int TextureWIDTH = 96;
    public final int TextureHEIGHT = 64;
    public Body body;
    public World world;
    public Body stinger;
    public Sprite current;
    public Texture strikeSpriteSheet;
    public Random r;
    public boolean striking;
    public boolean dead;
    public Shape[] shapes;
    public Vector2[] locs;
    public int cur;
    public Vector2 pos, ogpos;
    public int health;
    public boolean tobedestroyed;
    public int index;
    public Sound[] calls;
    public Sound death;
    public mapLevel level;

    public Scorpion(Zahrah game, Body body, Body stinger, int index, World world, mapLevel level){
        health = 3;
        this.level = level;
        calls = new Sound[6];
        calls[0] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call1.wav"));
        calls[1] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call2.wav"));
        calls[2] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call3.wav"));
        calls[3] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call4.wav"));
        calls[4] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call5.wav"));
        calls[5] = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Call6.wav"));
        death = Gdx.audio.newSound(Gdx.files.internal("Music/Scorpion/Death.wav"));
        tobedestroyed = false;
        this.index = index;
        this.world = world;
        this.stinger = stinger;
        pos = new Vector2(body.getPosition().x+ 0.35f, body.getPosition().y-1.5f);
        ogpos = body.getPosition();
        cur = 1;
        locs = new Vector2[9];
        shapes = new Shape[9];
        PolygonShape s = new PolygonShape();
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(-0.35f,1.6f),0);
        locs[0] = (new Vector2(-0.35f,1.6f));
        shapes[0] = s;
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0.35f,1.85f),0);
        shapes[1] = s;
        locs[1] = (new Vector2(0.35f,1.85f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0.15f,1.65f),0);
        shapes[2] = s;
        locs[2] = (new Vector2(0.15f,1.65f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0.55f,2.4f),0);
        shapes[3] = s;
        locs[3] = (new Vector2(0.55f,2.4f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0f,2.3f),0);
        shapes[4] = s;
        locs[4] = (new Vector2(0f,2.3f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(-1.1f,1.6f),0);
        shapes[5] = s;
        locs[5] = (new Vector2(-1.1f,1.6f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0f,2.3f),0);
        shapes[6] = s;
        locs[6] = (new Vector2(0f,2.3f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0.55f,2.4f),0);
        shapes[7] = s;
        locs[7] = (new Vector2(0.55f,2.4f));
        s.setAsBox(Physics.toUnits(15f),Physics.toUnits(10f),new Vector2(0.1f,1.6f),0);
        shapes[8] = s;
        locs[8] = (new Vector2(0.1f,1.6f));







        strikeSpriteSheet = new Texture("Characters/Scorpion/Strike.png");

        this.body = body;
        this.game = game;
        striking = false;
        dead = false;
        counter = 0;
        progress = 0;
        r = new Random();

        getCurrentSprite(0);

    }

    public void render(float dt){
        getCurrentSprite(dt);
        update(dt);
        game.batch.begin();
        if(!dead){
            current.draw(game.batch);
        }
        game.batch.end();
    }
    public void update(float dt){
        if(tobedestroyed && !dead){
            destroy();
        }
        progress = progress + dt;
        if(progress > 2 && r.nextBoolean() && !striking && !dead){
            striking = true;
            if(level.gameCam.frustum.pointInFrustum(new Vector3(current.getX(),current.getY(),0))) {
                float prob = 0.16666666f;
                float f = r.nextFloat();
                if (f < prob) {
                    calls[0].play();
                } else if (f > prob && f < prob * 2) {
                    calls[1].play();
                } else if (f > prob * 2 && f < prob * 3) {
                    calls[2].play();
                } else if (f > prob * 3 && f < prob * 4) {
                    calls[3].play();
                } else if (f > prob * 4 && f < prob * 5) {
                    calls[4].play();
                } else if (f > prob * 5 && f < prob * 6) {
                    calls[5].play();
                }
            }
            progress = 0;
        }
            if (cur == 1) {
                stinger.setTransform(pos.x + locs[0].x,pos.y +locs[0].y, 0);
            } else if (cur == 2) {
                stinger.setTransform(pos.x + locs[1].x,pos.y +locs[1].y, 0);
            } else if (cur == 3) {
                stinger.setTransform(pos.x + locs[2].x,pos.y +locs[2].y, 0);
            } else if (cur == 4) {
                stinger.setTransform(pos.x + locs[3].x,pos.y +locs[3].y, 0);
            } else if (cur == 5) {
                stinger.setTransform(pos.x + locs[4].x,pos.y +locs[4].y, 0);
            } else if (cur == 6) {
                stinger.setTransform(pos.x + locs[5].x,pos.y +locs[5].y, 0);
            } else if (cur == 7) {
                stinger.setTransform(pos.x + locs[6].x,pos.y +locs[6].y, 0);
            } else if (cur == 8) {
                stinger.setTransform(pos.x + locs[7].x,pos.y +locs[7].y, 0);
            } else if (cur == 9) {
                stinger.setTransform(pos.x + locs[8].x,pos.y +locs[8].y, 0);
            }
            else{
                stinger.setTransform(pos.x + locs[0].x,pos.y +locs[0].y, 0);
            }

    }
    public void destroy(){
        world.destroyBody(body);
        world.destroyBody(stinger);
        dead = true;
    }
    public void damage(){
        health--;

        if(health <= 0){
            death.play();
            tobedestroyed = true;
        }else if(!dead){
            float prob = 0.16666666f;
            float f = r.nextFloat();
            if(f < prob){
                calls[0].play();
            }else if(f > prob && f < prob*2){
                calls[1].play();
            }else if(f > prob*2 && f < prob*3){
                calls[2].play();
            }else if(f > prob*3 && f < prob*4){
                calls[3].play();
            }else if(f > prob*4 && f < prob*5){
                calls[4].play();
            }else if(f > prob*5 && f < prob*6){
                calls[5].play();
            }
        }
        game.player.body.setLinearVelocity(game.player.body.getLinearVelocity().x, 5.5f);
    }
    public void getCurrentSprite(float dt){

        if(striking) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 9)) {
                counter = counter - ((1 / walkfps) * 9);

            }
            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 11, 0, TextureWIDTH, TextureHEIGHT));
                cur = 1;
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 10, 0, TextureWIDTH, TextureHEIGHT));
                cur = 2;
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 9, 0, TextureWIDTH, TextureHEIGHT));
                cur = 3;
            } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 8, 0, TextureWIDTH, TextureHEIGHT));
                cur = 4;
            } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 7, 0, TextureWIDTH, TextureHEIGHT));
                cur = 5;
            } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
                cur = 6;
            } else if (counter > (6*(1 / walkfps)) && counter < (7 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
                cur = 7;
            } else if (counter > (7 * (1 / walkfps)) && counter < (8 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
                cur = 8;
            } else if (counter > (8 * (1 / walkfps)) && counter < (9 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(strikeSpriteSheet, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
                cur = 9;
                striking =false;
                counter = 0;
            }
        }
        else{
            current = new Sprite(new TextureRegion(strikeSpriteSheet,TextureWIDTH *11,0 ,TextureWIDTH,TextureHEIGHT));
            cur = 1;
            counter = 0;
        }
        current.setPosition(Physics.toPixels(body.getPosition().x) - 55, Physics.toPixels(body.getPosition().y));
        current.scale(2.25f);
    }

}
