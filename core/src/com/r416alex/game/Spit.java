package com.r416alex.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

public class Spit{

    public Sprite current;
    public Body body;
    public Texture spritesheet;
    public boolean toBeDestroyed;
    public World world;
    public Zahrah game;

    public Spit(Body body, World world, Zahrah game){
        toBeDestroyed = false;
        this.body = body;
        this.world = world;
        this.game = game;
    }
    public void update(float dt){
        if(toBeDestroyed){
            destroy();
        }
    }
    public void destroy(){
        world.destroyBody(body);

    }
    public String toString(){
        return "yes";
    }
}
