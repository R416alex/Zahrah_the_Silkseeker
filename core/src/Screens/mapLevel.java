package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.r416alex.game.Player;
import com.r416alex.game.Zahrah;

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

    public mapLevel(Zahrah game, int loc) {
        this.loc = loc;
        right = false;
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
        this.game = game;
        Gdx.input.setInputProcessor(this);
        gameCam = new OrthographicCamera();
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        gamePort = new FitViewport((game.G_WIDTH), (game.G_HEIGHT), gameCam);
        gameCam.position.set(game.G_WIDTH / 2f, game.G_HEIGHT / 2f, 0);
        world = new World(new Vector2(0f, -20f), true);


        loadBodies();
        mapRender = new OrthogonalTiledMapRenderer(map);
        mapRender.setView(gameCam);
        debugRenderer = new Box2DDebugRenderer();
        prop = map.getProperties();
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
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Physics.toUnits(75), Physics.toUnits(220));
        bodyDef.fixedRotation = true;
        PolygonShape pshape = new PolygonShape();
        pshape.setAsBox(Physics.toUnits(30)/2, Physics.toUnits(30)/2);
        fdef.shape = pshape;
        fdef.density = 1.0f;
        fdef.friction = 0f;
        fdef.restitution = 0.1f;

        game.player.init(world.createBody(bodyDef));
        game.player.body.applyAngularImpulse(0.2f,true);
        Fixture p = game.player.body.createFixture(fdef);
        p.setUserData(1);
        pshape.setAsBox(0.5f, 0.1f, new Vector2(0,-0.5f), 0);
        fdef.isSensor = true;
        Fixture footSensorFixture = game.player.body.createFixture(fdef);
        footSensorFixture.setUserData(3);
    }
    public void render(float dt) {
        update1(dt);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mapRender.getBatch().begin();
        if(map.getLayers().get("Background") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Background"));
        }
        if(map.getLayers().get("Background2") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Background2"));
        }
        mapRender.getBatch().end();
        debugRenderer.render(world,b2dCam);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.end();
        mapRender.getBatch().begin();
        if(map.getLayers().get("Foreground") != null) {
            mapRender.renderTileLayer((TiledMapTileLayer)map.getLayers().get("Foreground"));
        }
        mapRender.getBatch().end();
        game.transitions.CloseScreen(dt, 4);
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
        world.setContactListener(game.player);

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
                game.player.jump();
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
