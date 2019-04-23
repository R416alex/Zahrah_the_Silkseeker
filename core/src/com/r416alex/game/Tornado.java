package com.r416alex.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import Utils.Physics;

public class Tornado {

    public Sprite current;
    public Body body;
    public Texture spritesheet;
    public boolean toBeDestroyed;
    public World world;
    public Zahrah game;
    public boolean left;
    public float time;
    public final float walkfps = 12f;
    public final int TextureWIDTH= 27;
    public final int TextureHEIGHT= 26;
    public float counter;
    public boolean dead;
    public Tornado(World world, Zahrah game, boolean left){
        toBeDestroyed = false;
        time = 0;
        dead = false;
        counter = 0;
        this.left = left;
        this.world = world;
        this.game = game;
        spritesheet = new Texture("Characters/Zahrah/Tornado.png");
        makeBody();
    }
    public void render(float dt){
        update(dt);
        getcurrentsprite(dt);
        if(!dead) {
            current.draw(game.batch);
        }

    }
    public void makeBody(){
        BodyDef bodyDef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        bodyDef.fixedRotation = true;
        bodyDef.allowSleep = false;


        bodyDef.type = BodyDef.BodyType.DynamicBody;

        float x;
        if(left){
            x = game.player.body.getPosition().x - 1f;
        }else{
            x = game.player.body.getPosition().x + (Physics.toUnits(12)/2) + 0.5f;
        }

        bodyDef.position.set(x,game.player.body.getPosition().y-(Physics.toUnits(8)));
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);
        PolygonShape pshape = new PolygonShape();
        pshape.setAsBox(Physics.toUnits(5f) / 2, Physics.toUnits(5f) / 2);
        fdef.shape = pshape;
        fdef.isSensor = false;
        fdef.density = 1.0f;
        fdef.friction = 0f;
        fdef.restitution = 0.1f;
        Fixture f = body.createFixture(fdef);
        f.setUserData("t");


    }
    public void update(float dt){
        time += dt;

        if(toBeDestroyed || time > 2f){
            destroy();
        }
        if(!dead) {
            if (left) {
                body.setLinearVelocity(-2, 0);
            } else {
                body.setLinearVelocity(2, 0);
            }
        }
    }
    public void getcurrentsprite(float dt){
        counter = counter +dt;
        if (counter > ((1 / walkfps) * 6)) {
            counter = counter - ((1 / walkfps) *6);

        }
        if (counter < (1 / walkfps) && counter > 0) {
            current = new Sprite(new TextureRegion(spritesheet, 0, 0, TextureWIDTH, TextureHEIGHT));
        } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
            current = new Sprite(new TextureRegion(spritesheet, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
        } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
            current = new Sprite(new TextureRegion(spritesheet, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
        } else if (counter > (3 * (1 / walkfps)) && counter < (4 * (1 / walkfps))) {
            current = new Sprite(new TextureRegion(spritesheet, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
        } else if (counter > (4 * (1 / walkfps)) && counter < (5 * (1 / walkfps))) {
            current = new Sprite(new TextureRegion(spritesheet, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
        } else if (counter > (5 * (1 / walkfps)) && counter < (6 * (1 / walkfps))) {
            current = new Sprite(new TextureRegion(spritesheet, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
        }
        if(!dead) {
            current.setPosition(Physics.toPixels(body.getPosition().x - 0.25f) - 4, Physics.toPixels(body.getPosition().y) - 2);
        }
        }
    public void destroy(){
        dead = true;
        game.player.tornado = false;
        world.destroyBody(body);
        body = null;
    }

}
