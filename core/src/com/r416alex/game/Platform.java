package com.r416alex.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import Utils.Physics;

public class Platform {
    public Body body;
    public Zahrah game;
    public Vector2 range;
    public boolean left;
    public float speed;
    public float y;
    public boolean touching;
    public Sprite sprite;
    public Platform(Zahrah game, Body body, Vector2 range, float speed, float y){
        this.game = game;
        this.body = body;
        this.range = range;
        this.speed = speed;
        sprite = new Sprite(new TextureRegion(new Texture("Map/Levels/Level2/TileSets/Sprites/lvl2.png"),0,32,32,32));
        sprite.setPosition(body.getPosition().x, body.getPosition().y);
        this.y = y;
        touching = false;
    }
    public void render(float dt){
        update(dt);
        sprite.setPosition(Physics.toPixels(body.getPosition().x)-16, Physics.toPixels(body.getPosition().y)-5);
        game.batch.begin();
        sprite.draw(game.batch);
        game.batch.end();

    }
    public void update(float dt){
        if(left && body.getPosition().x < range.x){
            left = false;
        }
        else if(!left && body.getPosition().x > range.y){
            left = true;
        }
        if(left){
            body.setLinearVelocity(-speed, 0);
        }else{
            body.setLinearVelocity(speed,0);
        }
    }
}
