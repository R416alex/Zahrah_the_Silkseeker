package Utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.r416alex.game.Worm;
import com.r416alex.game.Zahrah;

import java.util.ArrayList;
import java.util.List;

import Screens.mapLevel;

public class ContactChecker implements ContactListener {
    public Zahrah game;
    public mapLevel level;
    public List<Worm> worms;
    public ContactChecker(Zahrah game, mapLevel level, List<Worm> worms){
        this.game = game;
        this.level = level;
        this.worms = worms;
    }
    @Override
    public void beginContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if ((fixtureA.getUserData().equals(3)
                && fixtureB.getUserData().equals(2)) || (fixtureA.getUserData().equals(2) && fixtureB.getUserData().equals(3))) {
            //System.out.println("contact");
            game.player.numFootContacts++;
            if(game.player.numFootContacts > 0){
                game.player.jumping = false;
                game.player.flying = false;
                game.player.progress = 0;
            }else{
                game.player.jumping = true;
            }
        }else if((fixtureA.getUserData().toString().substring(0,1).equals("r") && fixtureB.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(1))-6).groundRight = true;
        }else if((fixtureB.getUserData().toString().substring(0,1).equals("r") && fixtureA.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(1))-6).groundRight = true;
        }
        else if((fixtureA.getUserData().toString().substring(0,1).equals("l") && fixtureB.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(1))-6).groundLeft = true;
        }else if((fixtureB.getUserData().toString().substring(0,1).equals("l") && fixtureA.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(1))-6).groundLeft = true;
        }else if(fixtureA.getUserData().equals(3) && fixtureB.getUserData().toString().length() > 4){
            if(fixtureB.getUserData().toString().substring(0,4).equals("worm")) {
                worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4))-6).die();
            }
        }
        else if(fixtureB.getUserData().equals(3) && fixtureA.getUserData().toString().length() > 4){
            if(fixtureA.getUserData().toString().substring(0,4).equals("worm")) {
                worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4))-6).die();
            }
        }
        else if(fixtureB.getUserData().equals(1) && fixtureA.getUserData().toString().length() > 4){
            if(fixtureA.getUserData().toString().substring(0,4).equals("spit")) {
                if (worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp != null && !worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed) {
                    worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed = true;
                    game.player.die();
                }
            }
        }
        else if(fixtureA.getUserData().equals(1) && fixtureB.getUserData().toString().length() > 4){
            if(fixtureB.getUserData().toString().substring(0,4).equals("spit")) {
                if (worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4)) - 6).sp != null && !worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed) {
                    worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed = true;
                    game.player.die();
                }
            }
        }
        else if(fixtureB.getUserData().equals(2) && fixtureA.getUserData().toString().length() > 4){
            if(fixtureA.getUserData().toString().substring(0,4).equals("spit")) {
                if (worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp != null && !worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed) {
                    worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed = true;
                }
            }
        }
        else if(fixtureA.getUserData().equals(2) && fixtureB.getUserData().toString().length() > 4){
            if(fixtureB.getUserData().toString().substring(0,4).equals("spit")) {
                if(worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4))-6).sp != null && !worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4))-6).sp.toBeDestroyed) {
                    worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(4)) - 6).sp.toBeDestroyed = true;
                }
            }
        }

    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if ((fixtureA.getUserData().equals(3)
                && fixtureB.getUserData().equals(2)) || (fixtureA.getUserData().equals(2) && fixtureB.getUserData().equals(3))) {
            game.player.numFootContacts--;
            if(game.player.numFootContacts > 0){
                game.player.jumping = false;
                game.player.flying = false;
                game.player.progress = 0;
            }else{
                game.player.jumping = true;
            }
        }else if((fixtureA.getUserData().toString().substring(0,1).equals("r") && fixtureB.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(1))-6).groundRight = false;
        }else if((fixtureB.getUserData().toString().substring(0,1).equals("r") && fixtureA.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(1))-6).groundRight = false;
        }
        else if((fixtureA.getUserData().toString().substring(0,1).equals("l") && fixtureB.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureA.getUserData().toString().substring(1))-6).groundLeft = false;
        }else if((fixtureB.getUserData().toString().substring(0,1).equals("l") && fixtureA.getUserData().equals(2))){
            worms.get(Integer.parseInt(fixtureB.getUserData().toString().substring(1))-6).groundLeft = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }
}
