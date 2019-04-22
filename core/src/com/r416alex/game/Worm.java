package com.r416alex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Random;

import Screens.mapLevel;
import Utils.Physics;

public class Worm{
    public Body body;
    public Sprite current;
    public Texture walking, spitting, dying;
    public Zahrah game;
    public boolean left;
    public boolean spit;
    public float counter;
    public final float walkfps = 10;
    public final float dyefps = 10;
    public final int TextureWIDTH = 64;
    public final int TextureHEIGHT = 32;
    public boolean groundLeft, groundRight, dead;
    public int index;
    public float time;
    public World world;
    public float lastx;
    public float lasty;
    public Random r;
    public Spit sp;
    public boolean toBeDestroyed, dyingc;
    public mapLevel level;

    public Sound sound, death;
    public Worm(Zahrah game, Body body, int index, World world, mapLevel level){
        this.index = index;
        r = new Random();
        death = Gdx.audio.newSound(Gdx.files.internal("Music/Worm/Death.wav"));
        sound = Gdx.audio.newSound(Gdx.files.internal("Music/Worm/Spit.wav"));
        time = 0;
        this.level = level;
        dyingc = false;
        toBeDestroyed = false;
        counter = 0;
        lastx = body.getPosition().x;
        lasty = body.getPosition().y;
        this.world = world;
        groundLeft = true;
        dead = false;
        groundRight = true;
        this.game = game;
        left = true;
        spit = false;
        this.body = body;
        walking = new Texture("Characters/Worm/Walking.png");
        //spitting = new Texture("Characters/Worm/Spitting.png");
        dying = new Texture("Characters/Worm/Dying.png");
        current = new Sprite(new TextureRegion(walking, 0,0,TextureWIDTH,TextureHEIGHT));
    }

    public void render(float dt){
        update(dt);
        getCurrentSprite(dt);
        game.batch.begin();
        if(!dead) {
            current.draw(game.batch);
        }
        game.batch.end();

    }
    public void update(float dt){
        time = time + dt;
            if (counter < (1 / walkfps) && counter > 0) {
                body.setLinearVelocity(0, 0);
            } else {
                if (left && groundLeft && !dyingc) {
                    body.setLinearVelocity(-0.25f, 0);
                } else if (!left && groundRight && !dyingc) {
                    body.setLinearVelocity(0.25f, 0);
                } else if (left && !groundLeft && !dyingc) {
                    left = false;
                } else if (!left && !groundRight && !dyingc) {
                    left = true;
                }
            }
        if(time > 0.5 && sp == null && !dead){
            if(r.nextFloat() <= 0.25){
                spit();
            }
            time = 0;
        }
        if(toBeDestroyed){
            world.destroyBody(body);
            toBeDestroyed = false;
        }

        if(!dyingc && !dead){
            lastx = body.getPosition().x;
            lasty = body.getPosition().y;
        }
        if(sp != null){
            sp.update(dt);

            if(sp.toBeDestroyed == true){
                sp = null;
            }
        }


    }
    public void spit(){
        if(level.gameCam.frustum.pointInFrustum(new Vector3(current.getX(),current.getY(),0)) ) {
            sound.play();
        }
        BodyDef b = new BodyDef();
        b.fixedRotation = true;
        b.type = BodyDef.BodyType.DynamicBody;
        FixtureDef f = new FixtureDef();
        sp = new Spit(world.createBody(b), world, game);
        Shape s = new CircleShape();
        if(left) {
            ((CircleShape) s).setPosition(new Vector2(body.getPosition().x - 0.5f, body.getPosition().y));
       //     spit.applyLinearImpulse(-0.25f,0.3f, spit.getWorldCenter().x, spit.getWorldCenter().y, true);
        }
        else{
            ((CircleShape) s).setPosition(new Vector2(body.getPosition().x + 0.5f, body.getPosition().y));
       //     spit.applyLinearImpulse(0.25f,0.3f, spit.getWorldCenter().x, spit.getWorldCenter().y, true);
        }
        s.setRadius(0.1f);
        f.isSensor = true;
        f.density = 1f;
        f.shape = s;
        f.restitution = 1f;
        Fixture fix = sp.body.createFixture(f);
        fix.setUserData("spit"+index);

        if(left){
            sp.body.applyLinearImpulse(-0.15f,0.2f, sp.body.getWorldCenter().x, sp.body.getWorldCenter().y, true);
        }else{
            sp.body.applyLinearImpulse(0.15f,0.2f, sp.body.getWorldCenter().x, sp.body.getWorldCenter().y, true);
        }

    }
    public void die(){
        dyingc = true;
        death.play();
        toBeDestroyed = true;
        counter = 0;
        game.player.body.setLinearVelocity(game.player.body.getLinearVelocity().x, 5.5f);
    }

    public void getCurrentSprite(float dt) {

        if(!dead && !dyingc && left) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 6)) {
                counter = counter - ((1 / walkfps) * 8);

            }
            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(walking, 0, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
            }
            current.setPosition(Physics.toPixels(lastx) - 33, Physics.toPixels(lasty) - 17);
        }else if(!dead && !dyingc && !left) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 6)) {
                counter = counter - ((1 / walkfps) * 8);

            }
            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 5, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 4, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 3, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 2, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, TextureWIDTH * 1, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walking, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            }
            current.setPosition(Physics.toPixels(lastx) - 33, Physics.toPixels(lasty) - 17);
        }
        else if(left && dyingc){
            counter = counter + dt;
            if (counter > ((1 / dyefps) * 5)) {
                counter = counter - ((1 / dyefps) * 5);
                dead = true;
            }
            if (counter < (1 / dyefps) && counter > 0) {
                current = new Sprite(new TextureRegion(dying, 0, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / dyefps)) && counter < (2 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / dyefps)) && counter < (3 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / dyefps)) && counter < (4 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / dyefps)) && counter < (5 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
            }
            current.setPosition(Physics.toPixels(lastx) - 33, Physics.toPixels(lasty) - 20);
        }else if(!left && dyingc){
            counter = counter + dt;
            if (counter > ((1 / dyefps) * 5)) {
                counter = counter - ((1 / dyefps) * 5);
                dead = true;
            }
            if (counter < (1 / dyefps) && counter > 0) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH*4, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / dyefps)) && counter < (2 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 3, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / dyefps)) && counter < (3 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 2, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (3 * (1 / dyefps)) && counter < (4 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 1, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (4 * (1 / dyefps)) && counter < (5 * (1 / dyefps))) {
                current = new Sprite(new TextureRegion(dying, TextureWIDTH * 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            }
            current.setPosition(Physics.toPixels(lastx) - 33, Physics.toPixels(lasty) - 20);
        }
    }

}
