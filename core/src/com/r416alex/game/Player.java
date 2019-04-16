package com.r416alex.game;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class Player implements com.badlogic.gdx.physics.box2d.ContactListener{
    private boolean gloves, cape, pepper, level1, level2, level3, darkMarket;
    private int petals;
    private boolean left, right, stop;
    public Body body;
    public int numFootContacts;
    public ContactListener contactListener;


    Player(){
        numFootContacts = 0;
        left = false;
        right = false;
        stop = false;
        gloves = false;
        cape = false;
        pepper = false;
        level1 = false;
        level2 = false;
        level3 = false;
        darkMarket = false;
        petals = 0;


    }
    public void init(Body body){
        this.body = body;

    }
    public void update(float dt, boolean left,boolean right,boolean stop){
        if(numFootContacts < 1){

        }
        Vector2 vel = body.getLinearVelocity();
        float desiredVel = 0;

        if(left){
            if((vel.x + 0.25f) < -5.0f) {
                desiredVel = vel.x - 0.25f;
            }
            else{
                desiredVel = -5.0f;
            }
        } else if (right) {
            if((vel.x - 0.25f) > 5.0f) {
                desiredVel = vel.x + 0.25f;
            }
            else{
                desiredVel = 5.0f;
            }
        }
        else if(stop){
            desiredVel = vel.x * 0.75f;
        }
        //System.out.println(desiredVel);
        float velChange = desiredVel - vel.x;
        //System.out.println("Foot" + numFootContacts);
        float impulse = body.getMass() * velChange;
        body.applyLinearImpulse(new Vector2(impulse, 0), body.getWorldCenter(), true);
    }
    public void jump(){
        if(numFootContacts >= 1){
            body.applyLinearImpulse(new Vector2(0,6.5f),body.getWorldCenter(), true);
        }

    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if(fixtureA.getUserData().equals(3) || fixtureB.getUserData().equals(3) && (!fixtureA.getUserData().equals(1) || !fixtureB.getUserData().equals(1))) {
            //System.out.println("contact");
            numFootContacts++;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if((fixtureA.getUserData().equals(3)
                || fixtureB.getUserData().equals(3) && (!fixtureA.getUserData().equals(1) || !fixtureB.getUserData().equals(1)))) {
            numFootContacts--;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
