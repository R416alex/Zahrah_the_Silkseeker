package com.r416alex.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import Utils.Physics;


public class Player {
    public boolean gloves, cape, pepper, level1, level2, level3, darkMarket;
    private int petals;
    public boolean left, right, stop, jumping;
            public boolean flying;
    public Body body;
    public int numFootContacts;
    public int previous;
    public int frame;
    public float counter;
    public float progress;
    public Sprite current;
    public final float walkfps = 12;
    public final float jumpfps = 8;
    public final int TextureWIDTH = 20;
    public final int TextureHEIGHT = 30;
    public Texture walkingSprite;
    public Texture jumpingSprite;
    public Zahrah game;
    public int lastLook;
    public float x, y;
    public Random r;
    public int flipper;
    public boolean dead;
    public float timesincetornado;
    public boolean grounded;
    public boolean tornado;
    public List<Tornado> tornados;
    Player(Zahrah game) {
        tornados = new ArrayList<Tornado>();
        flipper = 1;
        r = new Random();
        x = 0;
        y = 0;
        dead = false;
        progress = 0;
        lastLook = 1;
        this.game = game;
        walkingSprite = new Texture("Characters/Zahrah/Walking.png");
        jumpingSprite = new Texture("Characters/Zahrah/Jumping.png");
        current = new Sprite(new TextureRegion(walkingSprite, 0, 0, TextureWIDTH, TextureHEIGHT));
        frame = 0;
        counter = 0;
        previous = 0;
        numFootContacts = 0;
        timesincetornado = 5f;
        left = false;
        jumping = false;
        right = true;
        flying = false;
        stop = false;
        gloves = true;
        cape = true;
        pepper = false;
        level1 = true;
        level2 = true;
        level3 = false;
        darkMarket = true;
        petals = 0;
        tornado = false;

    }

    public void updateSprite(){
        if(cape && !gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingC.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingC.png");
        }else if(!cape && gloves) {
            walkingSprite = new Texture("Characters/Zahrah/WalkingG.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingG.png");
        }else if(cape && gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingCG.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingCG.png");
        }else{
            walkingSprite = new Texture("Characters/Zahrah/Walking.png");
            jumpingSprite = new Texture("Characters/Zahrah/Jumping.png");
        }
    }
    public void init(Body body) {
        this.body = body;
        numFootContacts = 0;
        timesincetornado = 5;

        if(cape && !gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingC.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingC.png");
        }else if(!cape && gloves) {
            walkingSprite = new Texture("Characters/Zahrah/WalkingG.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingG.png");
        }else if(cape && gloves){
            walkingSprite = new Texture("Characters/Zahrah/WalkingCG.png");
            jumpingSprite = new Texture("Characters/Zahrah/JumpingCG.png");
        }else{
            walkingSprite = new Texture("Characters/Zahrah/Walking.png");
            jumpingSprite = new Texture("Characters/Zahrah/Jumping.png");
        }
    }
    public void throwtornado(boolean l){
        tornado = true;
        tornados.add(new Tornado(body.getWorld(), game, l));
    }

    public void update(float dt, int dir, boolean left, boolean right, boolean stop, boolean shift) {

        if(tornado == false && tornados.size() > 0){
            tornados.remove(0);
        }
        if(!tornado && gloves){
            timesincetornado += dt;
            if(timesincetornado > 5 && shift && tornados.size() == 0){
                timesincetornado = 0;
                if(dir == 1) {
                    throwtornado(true);
                }else{
                    throwtornado(false);
                }
            }
        }
        if(numFootContacts >= 1){
            grounded = true;
        }
        this.left = left;
        this.right = right;
        this.stop = stop;
        Vector2 vel = body.getLinearVelocity();
        float desiredVel = 0;

        if (dir == 1 && left && numFootContacts != 0) {
            if ((vel.x + 0.05f) < -5.0f) {
                desiredVel = vel.x - 0.025f;
            } else {
                desiredVel = -5.0f;
            }
        } else if (dir == 2 && right && numFootContacts != 0) {
            if ((vel.x - 0.05f) > 5.0f) {
                desiredVel = vel.x + 0.025f;
            } else {
                desiredVel = 5.0f;
            }
        } else if (stop && !left && !right && numFootContacts != 0) {
            desiredVel = vel.x * 0.75f;
        }else if(numFootContacts == 0){
            if(left){
                if ((vel.x - 0.15f) < -5.0f) {
                    desiredVel = -5;
                } else {
                    desiredVel = vel.x - 0.35f;
                }
            }else if(right){
                if ((vel.x + 0.15f) > 5.0f) {
                    desiredVel = 5;
                } else {
                    desiredVel = vel.x + 0.35f;
                }
            }else {
                desiredVel = vel.x * 0.97f;
            }
        }

        //System.out.println(desiredVel);
        float velChange = desiredVel - vel.x;
        //System.out.println("Foot" + numFootContacts);
        float impulse = body.getMass() * velChange;
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
        if(flying && cape){
            progress = progress + dt;
            if(progress < 2.75 && progress != 0){
                if(body.getLinearVelocity().y < 1) {
                    body.setTransform(new Vector2(body.getPosition().x + (float) (flipper * (1/9f)* progress * Math.sin(progress*10)), body.getPosition().y),0);
                    body.applyLinearImpulse(new Vector2(0f,0.5f), body.getWorldCenter(), true);
                   // body.applyLinearImpulse((float)(flipper *(Math.floor((progress*2))%2-0.5)),0.4f, body.getWorldCenter().x, body.getWorldCenter().y, true);
                }
                else{
                    body.setTransform(new Vector2(body.getPosition().x + (float) (flipper * progress * (1/9f)*Math.sin(progress * 10)), body.getPosition().y),0);
                  //  body.applyLinearImpulse((float)(flipper *(Math.floor((progress*2))%2-0.5)),0f, body.getWorldCenter().x, body.getWorldCenter().y, true);

                }
            }
            else{
                endFly();
            }

        }
        x = Physics.toPixels(body.getPosition().x - Physics.toUnits(9));
        y = Physics.toPixels(body.getPosition().y - Physics.toUnits(15));
        //System.out.println(body.getPosition().y);
        if(body.getPosition().y < 0){
            die();
        }
    }

    public void die(){
        game.transitions.transitioning = true;
        dead = true;
    }
    public void render(float dt) {
        if(tornado == false && tornados.size() > 0){
            tornados.remove(0);
        }
        getCurrentSprite(dt);
        game.batch.begin();
        current.draw(game.batch);
        if(tornados.size() > 0){
            for(Tornado temp : tornados){
                temp.render(dt);
            }
        }
        game.batch.end();

    }

    public void jump() {
        if (numFootContacts >= 1) {
            body.applyLinearImpulse(new Vector2(0, 3.25f), body.getWorldCenter(), true);
            jumping = true;
        }
        if (numFootContacts == 0 && cape  && !flying && grounded) {
            flying = true;
            if (r.nextBoolean()) {
                flipper = 1;
            } else
                flipper = -1;

        }

    }
    public void endFly(){
       // float x = (float)(flipper *(Math.floor((progress*2))%2-0.5));
        float x = (float)(flipper * progress *Math.sin(progress * 10));
        body.applyLinearImpulse(x,0, body.getWorldCenter().x, body.getWorldCenter().y, true);
        progress = 0;
        flying = false;
        grounded = false;
    }


    public void getCurrentSprite(float dt) {

        if (left && numFootContacts > 0) {
            counter = counter + dt;
            if (counter > ((1 / walkfps) * 8)) {
                counter = counter - ((1 / walkfps) * 8);
            }

            if (counter < (1 / walkfps) && counter > 0) {
                current = new Sprite(new TextureRegion(walkingSprite, 0, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > ((1 / walkfps)) && counter < (2 * (1 / walkfps))) {
                current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
            } else if (counter > (2 * (1 / walkfps)) && counter < (3 * (1 / walkfps))) {
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
        } else if (right && numFootContacts > 0) {
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
        } else if (numFootContacts == 0 && jumping) {
            counter = counter + dt;
            if (left) {
                if (counter < (1 / jumpfps) && counter > 0) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > ((1 / jumpfps)) && counter < (2 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 5, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (2 * (1 / jumpfps)) && counter < (3 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 4, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (3 * (1 / jumpfps)) && counter < (4 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 3, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (4 * (1 / jumpfps)) && counter < (5 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 2, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (5 * (1 / jumpfps)) && counter < (6 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (6 * (1 / jumpfps)) && counter < (7 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                    jumping = false;
                }
                lastLook = 0;
            } else if (right) {
                if (counter < (1 / jumpfps) && counter > 0) {
                    current = new Sprite(new TextureRegion(jumpingSprite, 0, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > ((1 / jumpfps)) && counter < (2 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (2 * (1 / jumpfps)) && counter < (3 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (3 * (1 / jumpfps)) && counter < (4 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (4 * (1 / jumpfps)) && counter < (5 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (5 * (1 / jumpfps)) && counter < (6 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (6 * (1 / jumpfps)) && counter < (7 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
                    jumping = false;
                }
                lastLook = 1;
            } else if (lastLook == 1) {
                if (counter < (1 / jumpfps) && counter > 0) {
                    current = new Sprite(new TextureRegion(jumpingSprite, 0, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > ((1 / jumpfps)) && counter < (2 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (2 * (1 / jumpfps)) && counter < (3 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 2, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (3 * (1 / jumpfps)) && counter < (4 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 3, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (4 * (1 / jumpfps)) && counter < (5 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 4, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (5 * (1 / jumpfps)) && counter < (6 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 5, 0, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (6 * (1 / jumpfps)) && counter < (7 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
                    jumping = false;
                }
            } else if (lastLook == 0) {
                if (counter < (1 / jumpfps) && counter > 0) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > ((1 / jumpfps)) && counter < (2 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 5, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (2 * (1 / jumpfps)) && counter < (3 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 4, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (3 * (1 / jumpfps)) && counter < (4 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 3, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (4 * (1 / jumpfps)) && counter < (5 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 2, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (5 * (1 / jumpfps)) && counter < (6 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                } else if (counter > (6 * (1 / jumpfps)) && counter < (7 * (1 / jumpfps))) {
                    current = new Sprite(new TextureRegion(jumpingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                    jumping = false;
                }
            }

        } else if (numFootContacts == 0 && !jumping) {
            if (right) {
                current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
                lastLook = 1;
            } else if (left) {
                current = new Sprite(new TextureRegion(jumpingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
                lastLook = 0;
            } else if (lastLook == 1) {
                current = new Sprite(new TextureRegion(jumpingSprite, TextureWIDTH * 6, 0, TextureWIDTH, TextureHEIGHT));
            } else if (lastLook == 0) {
                current = new Sprite(new TextureRegion(jumpingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            }

        } else if (lastLook == 0) {
            current = new Sprite(new TextureRegion(walkingSprite, TextureWIDTH * 7, 0, TextureWIDTH, TextureHEIGHT));
            counter = 0;
        } else if (lastLook == 1) {
            current = new Sprite(new TextureRegion(walkingSprite, 0, TextureHEIGHT, TextureWIDTH, TextureHEIGHT));
            counter = 0;
        }
        current.setPosition(x, y);
    }
}
