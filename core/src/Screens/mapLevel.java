package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Platform;
import com.r416alex.game.Player;
import com.r416alex.game.Spit;
import com.r416alex.game.Worm;
import com.r416alex.game.Zahrah;

import java.util.ArrayList;
import java.util.List;

import Utils.ContactChecker;
import Utils.Physics;

public abstract class mapLevel implements Screen, InputProcessor {
    private int loc;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRender;
    private Zahrah game;
    private OrthographicCamera gameCam;
    private Viewport gamePort;
    private World world;
    private Matrix4 b2dCam;
    private Box2DDebugRenderer debugRenderer;
    private final int ppm = 32;
    private boolean right, left, stop;
    private MapProperties prop;
    public int dir; // 1 = left 2 = right;
    public Sprite gloves, pepper, cape;
    public int buyzone;
    public List<Worm> worms;
    public List<Spit> spits;
    public List<Platform> plats;
    public ContactChecker contact;


    public mapLevel(Zahrah game, int loc) {
        this.game = game;
        this.loc = loc;
        buyzone = 0;
        worms = new ArrayList<Worm>();
        spits = new ArrayList<Spit>();
        game.player.dead = false;
        right = false;
        plats = new ArrayList<Platform>();
        left = false;
        stop = false;
        System.out.println(loc);
        if (loc == 1) {
            map = new TmxMapLoader().load("Map/Levels/Level1/Level1.tmx");
        } else if (loc == 0) {
            map = new TmxMapLoader().load("Map/Levels/DarkMarket/Darkmarket.tmx");
        } else if(loc == 2){
            map = new TmxMapLoader().load("Map/Levels/Level2/Level2.tmx");
        }

        Gdx.input.setInputProcessor(this);
        gameCam = new OrthographicCamera();
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        gamePort = new FitViewport((game.G_WIDTH), (game.G_HEIGHT), gameCam);
        gameCam.position.set(game.G_WIDTH / 2f, game.G_HEIGHT / 2f, 0);
        world = new World(new Vector2(0f, -20f), true);

        loadBodies();
        contact = new ContactChecker(game, this, worms, plats);
        world.setContactListener(contact);
        mapRender = new OrthogonalTiledMapRenderer(map);
        mapRender.setView(gameCam);
        debugRenderer = new Box2DDebugRenderer();
        prop = map.getProperties();

        if(game.player.level1){
            cape = new Sprite(new Texture("Misc/Cape.png"));
        }else{
            cape = new Sprite(new TextureRegion(new Texture("Misc/ItemsDark.png"),32,0,32,32));
        }
        if(game.player.level2){
            gloves = new Sprite(new Texture("Misc/Gloves.png"));
        }
        else{
            gloves = new Sprite(new TextureRegion(new Texture("Misc/ItemsDark.png"),0,0,32,32));
        }
        if(game.player.level3){
            pepper = new Sprite((new Texture("Misc/Pepper.png")));
        }
        else{
            pepper = new Sprite((new TextureRegion((new Texture("Misc/ItemsDark.png")),64,0,32,32)));
        }
        cape.setPosition(105,50);
        pepper.setPosition(345,45);
        gloves.setPosition(274,46);


        game.transitions.opening = true;

    }



    private void loadBodies(){
        BodyDef bodyDef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        if(map.getLayers().get("Collisions") != null) {
            MapLayer layer = map.getLayers().get("Collisions");


            for (MapObject object : layer.getObjects()) {

                bodyDef.type = BodyDef.BodyType.StaticBody;

                float x = (object.getProperties().get("x", Float.class));
                float y = (object.getProperties().get("y", Float.class));

                bodyDef.position.set(Physics.toUnits(x + (0.5f * object.getProperties().get("width", Float.class))), Physics.toUnits(y + (0.5f * (object.getProperties().get("height", Float.class)))));

                PolygonShape pshape = new PolygonShape();
                pshape.setAsBox(Physics.toUnits(object.getProperties().get("width", Float.class)) / 2, Physics.toUnits(object.getProperties().get("height", Float.class)) / 2);
                fdef.shape = pshape;
                fdef.density = 1.0f;
                fdef.friction = 0.9f;
                fdef.restitution = 0;

                Body body = world.createBody(bodyDef);
                Fixture b = body.createFixture(fdef);
                b.setUserData(2);



            }
        }
        if(map.getLayers().get("Worms") != null) {
            MapLayer layer = map.getLayers().get("Worms");


            for (int i = 0; i < layer.getObjects().getCount(); i++) {

                MapObject object = layer.getObjects().get(i);
                bodyDef.type = BodyDef.BodyType.DynamicBody;

                float x = (object.getProperties().get("x", Float.class));
                float y = (object.getProperties().get("y", Float.class));

                bodyDef.position.set(Physics.toUnits(x + (0.5f * 32f)), Physics.toUnits(y + (0.5f *20f)));
                bodyDef.fixedRotation = true;
                PolygonShape pshape = new PolygonShape();
                pshape.setAsBox(Physics.toUnits(32f) / 2, Physics.toUnits(12f) / 2);
                fdef.shape = pshape;
                fdef.isSensor = false;
                fdef.density = 1.0f;
                fdef.friction = 0f;
                fdef.restitution = 0.1f;

                int index = i + 6;

                worms.add(new Worm(game, world.createBody(bodyDef), index, world, this));
                Fixture b = worms.get(i).body.createFixture(fdef);
                b.setUserData("worm"+index);

                pshape.setAsBox(0.05f, 0.05f, new Vector2(-0.75f,-0.3f), 0);
                fdef.isSensor = true;
                Fixture f = worms.get(i).body.createFixture(fdef);
                f.setUserData("l"+index);

                pshape.setAsBox(0.05f, 0.05f, new Vector2(0.75f,-0.3f), 0);
                fdef.isSensor = true;
                Fixture g = worms.get(i).body.createFixture(fdef);
                g.setUserData("r"+index);



            }
        }
        if(map.getLayers().get("Platforms") != null) {
            MapLayer layer = map.getLayers().get("Platforms");


            for (int i = 0; i < layer.getObjects().getCount(); i++) {

                MapObject object = layer.getObjects().get(i);
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                float x;
                if(object.getProperties().get("right", Boolean.class)){
                    x = (object.getProperties().get("width", Float.class) - 32);
                }else {
                    x = (object.getProperties().get("x", Float.class));
                }
                float y = (object.getProperties().get("y", Float.class));

                Vector2 range = new Vector2(Physics.toUnits(object.getProperties().get("x", Float.class)), Physics.toUnits(object.getProperties().get("y", Float.class)-32));

                float speed = Physics.toUnits(object.getProperties().get("speed", Float.class));

                bodyDef.position.set(Physics.toUnits(x), Physics.toUnits(y));
                bodyDef.fixedRotation = true;
                PolygonShape pshape = new PolygonShape();
                pshape.setAsBox(Physics.toUnits(32)/2, Physics.toUnits(10) / 2);
                fdef.shape = pshape;
                fdef.density = 1.0f;
                fdef.friction = 100000f;
                fdef.restitution = 0;
                fdef.isSensor = false;


                plats.add(new Platform(game,world.createBody(bodyDef), range, speed, Physics.toUnits(object.getProperties().get("y", Float.class))));
                Fixture f = plats.get(i).body.createFixture(fdef);
                f.setUserData(2);



            }
        }

        fdef.isSensor = false;
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Physics.toUnits(75), Physics.toUnits(220));
        bodyDef.fixedRotation = true;
        PolygonShape pshape = new PolygonShape();
        pshape.setAsBox(Physics.toUnits(12)/2, Physics.toUnits(28)/2);
        fdef.shape = pshape;
        fdef.density = 1.0f;
        fdef.friction = 0.001f;
        fdef.restitution = 0.1f;

        game.player.init(world.createBody(bodyDef));
        game.player.body.applyAngularImpulse(0.2f,true);
        Fixture p = game.player.body.createFixture(fdef);
        p.setUserData(1);
        pshape.setAsBox(0.14f, 0.05f, new Vector2(0,-0.5f), 0);
        fdef.isSensor = true;
        Fixture footSensorFixture = game.player.body.createFixture(fdef);
        footSensorFixture.setUserData(3);
    }
    public void render(float dt) {
        update1(dt);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRender.getBatch().begin();
        if(map.getLayers().get("Background") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Background"));
        }
        if(map.getLayers().get("Background2") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Background2"));
        }
        mapRender.getBatch().end();
        game.batch.begin();
        if(loc == 0){
            if(!game.player.cape) {
                cape.draw(game.batch);
            }
            if(!game.player.pepper) {
                pepper.draw(game.batch);
            }
            if(!game.player.gloves) {
                gloves.draw(game.batch);
            }
        }
        game.batch.end();
        debugRenderer.render(world,b2dCam);
        game.batch.setProjectionMatrix(gameCam.combined);

        if(plats.size() > 0){
            for(Platform plat : plats){
                plat.render(dt);
            }
        }
        if(worms.size() > 0){
            for(Worm currentworm : worms){
                currentworm.render(dt);
            }
        }

        game.player.render(dt);
        mapRender.getBatch().begin();
        if(map.getLayers().get("Foreground") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Foreground"));
        }
        mapRender.getBatch().end();
        if(game.player.dead){
            game.transitions.CloseScreen(dt, loc);
        }else {
            game.transitions.CloseScreen(dt, 4);
        }
        game.transitions.OpenScreen(dt);
    }
    public void update1(float dt) {
        world.step(dt, 3, 3);
        //System.out.println(Physics.toPixels(player.body.getWorldCenter().x));
        //System.out.println((prop.get("width", Integer.class)));
        Vector2 campos = new Vector2();
        if(loc != 0) {
            if (Physics.toPixels(game.player.body.getWorldCenter().x) - ((game.G_WIDTH) / 3) <= 0) {
                campos.x = game.G_WIDTH / 2;
            } else if (Physics.toPixels(game.player.body.getWorldCenter().x) + (2 * game.G_WIDTH / 3) >= Physics.toPixels(prop.get("width", Integer.class))) {
                campos.x = Physics.toPixels(prop.get("width", Integer.class)) - (game.G_WIDTH / 2);
            } else {
                campos.x = Physics.toPixels(game.player.body.getWorldCenter().x) + game.G_WIDTH / 6;
            }

            if (Physics.toPixels(game.player.body.getWorldCenter().y) - (game.G_HEIGHT / 2) <= 0) {
                campos.y = (game.G_HEIGHT / 2);
            } else if (Physics.toPixels(game.player.body.getWorldCenter().y) + (game.G_HEIGHT / 2) >= Physics.toPixels(prop.get("height", Integer.class))) {
                campos.y = Physics.toPixels(prop.get("height", Integer.class)) - (game.G_HEIGHT / 2);
            } else {
                campos.y = Physics.toPixels(game.player.body.getPosition().y);
            }
        }else{
            campos.x = game.G_WIDTH/2;
            campos.y = game.G_HEIGHT/2;
        }
        gameCam.position.set(campos, 0);
        gameCam.update();
        mapRender.setView(gameCam);
    b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        game.player.update(dt, dir, left, right, stop);

}
    public boolean keyDown(int i) {
        if(!game.transitions.opening || !game.transitions.exiting || !game.transitions.transitioning) {
            //System.out.println("Level");
            if (i == Input.Keys.ESCAPE) {
                game.transitions.transitioning = true;
            } else if (i == Input.Keys.A) {
                left = true;
                stop = false;
                dir = 1;
            } else if (i == Input.Keys.D) {
                right = true;
                stop = false;
                dir = 2;
            } else if (i == Input.Keys.SPACE) {
                if(buyzone == 1 && !game.player.cape && game.player.level1) {
                    game.player.cape = true;
                    game.player.updateSprite();
                }else if(buyzone == 2 && !game.player.gloves && game.player.level2){
                    game.player.gloves = true;
                    game.player.updateSprite();
                }else if(buyzone == 3 && !game.player.pepper && game.player.level3){
                    game.player.pepper = true;
                    game.player.updateSprite();
                }
                else{
                    game.player.jump();
                }
            }
        }
        return false;
    }
    public boolean keyUp(int i) {
        if(!game.transitions.opening || !game.transitions.exiting || !game.transitions.transitioning) {
            if (i == Input.Keys.A) {
                if (!right) {
                    stop = true;
                }
                left = false;
            } else if (i == Input.Keys.D) {
                if (!left) {
                    stop = true;
                }
                right = false;
            }else if(i == Input.Keys.SPACE){
                if(game.player.flying){
                    game.player.endFly();
                }
            }
        }
        return false;
    }

    public void resize(int i, int i1) {
        gamePort.update(i, i1);
    }

    public void dispose(){

    }
}
