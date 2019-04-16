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
        private Player player;
        private boolean right, left, stop;
        private MapProperties prop;

        public mapLevel(Zahrah game, int loc){
        this.loc = loc;
        right = false;
        left = false;
        stop = false;
        if(loc == 1) {
            map = new TmxMapLoader().load("Map/Levels/Level1/Level1.tmx");
        }
        player = game.player;
        this.game = game;
        Gdx.input.setInputProcessor(this);
        gameCam = new OrthographicCamera();
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        gamePort = new FitViewport((game.G_WIDTH), (game.G_HEIGHT), gameCam);
        gameCam.position.set(game.G_WIDTH/2f,game.G_HEIGHT/2f,0);
        world = new World(new Vector2(0f,-11.8f), true);
        loadBodies();
        mapRender = new OrthogonalTiledMapRenderer(map);
        mapRender.setView(gameCam);
        debugRenderer = new Box2DDebugRenderer();
        prop = map.getProperties();

    }



    private void loadBodies(){
        MapLayer layer = map.getLayers().get("Collisions");

        BodyDef bodyDef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        for (MapObject object : layer.getObjects()) {

            bodyDef.type = BodyDef.BodyType.StaticBody;

            float x = (object.getProperties().get("x", Float.class));
            float y = (object.getProperties().get("y", Float.class));

            bodyDef.position.set(Physics.toUnits(x + (0.5f*object.getProperties().get("width", Float.class))) ,Physics.toUnits(y + (0.5f*(object.getProperties().get("height", Float.class)))));

            PolygonShape pshape = new PolygonShape();
            pshape.setAsBox(Physics.toUnits(object.getProperties().get("width", Float.class))/2, Physics.toUnits(object.getProperties().get("height", Float.class))/2);
            fdef.shape = pshape;
            fdef.density = 1.0f;
            fdef.friction = 0.9f;
            fdef.restitution = 0;

            Body body = world.createBody(bodyDef);
            Fixture b = body.createFixture(fdef);
            b.setUserData(2);


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

        player.init(world.createBody(bodyDef));
        player.body.applyAngularImpulse(0.2f,true);
        Fixture p = player.body.createFixture(fdef);
        p.setUserData(1);
        pshape.setAsBox(0.5f, 0.1f, new Vector2(0,-0.5f), 0);
        fdef.isSensor = true;
        Fixture footSensorFixture = player.body.createFixture(fdef);
        footSensorFixture.setUserData(3);
    }
    public void render(float dt) {
        update1(dt);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRender.render();
        debugRenderer.render(world,b2dCam);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.end();
    }
    public void update1(float dt) {
        world.step(dt, 3, 3);
        //System.out.println(Physics.toPixels(player.body.getWorldCenter().x));
        //System.out.println((prop.get("width", Integer.class)));
        if (Physics.toPixels(player.body.getWorldCenter().x) - ((game.G_WIDTH)/3) <= 0) {
            gameCam.position.set(game.G_WIDTH / 2, gameCam.position.y, 0);
        }
        else if(Physics.toPixels(player.body.getWorldCenter().x) + (2 *game.G_WIDTH/3) >= Physics.toPixels(prop.get("width", Integer.class))){
            gameCam.position.set(Physics.toPixels(prop.get("width", Integer.class)) - (game.G_WIDTH/2), gameCam.position.y,0);
        }
        else{
            gameCam.position.set(Physics.toPixels(player.body.getWorldCenter().x) + game.G_WIDTH / 6, gameCam.position.y, 0);
    }
        gameCam.update();
        mapRender.setView(gameCam);
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        player.update(dt, left, right, stop);
        world.setContactListener(player);

    }
    public boolean keyDown(int i) {
        //System.out.println("Level");
        if(i == Input.Keys.ESCAPE){
            this.dispose();
            Gdx.app.exit();
        }
        else if(i == Input.Keys.A) {
            left = true;
            stop = false;
        }
        else if(i == Input.Keys.D){
            right = true;
            stop = false;
        }
        else if(i == Input.Keys.SPACE){
            player.jump();
        }
        return false;
    }
    public boolean keyUp(int i) {
        if(i == Input.Keys.A){
            if(!right){
                stop = true;
            }
            left = false;
        }
        else if(i == Input.Keys.D){
            if(!left){
                stop = true;
            }
            right = false;
        }
        return false;
    }

    public void resize(int i, int i1) {
        gamePort.update(i, i1);
    }
}
