package Screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.r416alex.game.Zahrah;

import Utils.Physics;

public class Level1 implements Screen, InputProcessor {

    private TiledMap map;
    private TiledMapRenderer mapRenderer;
    private World world;
    private Zahrah game;
    private OrthographicCamera gameCam;
    private Matrix4 b2dCam;
    private Viewport gamePort;
    private Box2DDebugRenderer debugRenderer;
    private final int ppm = 32;
    private float x, y;
    private Body player;

    private boolean right, left, up, down;

    public Level1(Zahrah game) {
        right = false;
        left = false;
        up = false;
        down = false;
        x = 20;
        y = 130;

        this.game = game;
        Gdx.input.setInputProcessor(this);
        gameCam = new OrthographicCamera();
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
        gamePort = new FitViewport((game.G_WIDTH), (game.G_HEIGHT), gameCam);
        map = new TmxMapLoader().load("Map/Levels/Level1/Level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        gameCam.position.set(game.G_WIDTH/2,game.G_HEIGHT/2,0);
        world = new World(new Vector2(0f,-9.8f), true);

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
            fdef.friction = 0.2f;
            fdef.restitution = 0;

            Body body = world.createBody(bodyDef);
            body.createFixture(fdef);



        }
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(Physics.toUnits(75), Physics.toUnits(120));
        PolygonShape pshape = new PolygonShape();
        pshape.setAsBox(Physics.toUnits(32)/2, Physics.toUnits(32)/2);
        fdef.shape = pshape;
        fdef.density = 1.0f;
        fdef.friction = 0.2f;
        fdef.restitution = 0.5f;

 player = world.createBody(bodyDef);
        player.applyAngularImpulse(0.2f,true);
        player.createFixture(fdef);


        debugRenderer = new Box2DDebugRenderer();

    }

    @Override
    public void render(float dt) {
        update(dt);

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        mapRenderer.render();
        debugRenderer.render(world,b2dCam);
        game.batch.setProjectionMatrix(gameCam.combined);
        game.batch.begin();
        game.batch.end();
    }
    private void update(float dt){
        world.step(dt,3,3);
        gameCam.update();
        if(right){
            gameCam.translate(80*dt,0);
        }else if(left){
            gameCam.translate(-80*dt,0);
        }else if(up){
            gameCam.translate(0, 80*dt);
        }else if(down){
            gameCam.translate(0,-80*dt);
        }
        mapRenderer.setView(gameCam);
        b2dCam = gameCam.combined.cpy();
        b2dCam.scl(ppm);
    }
    @Override
    public boolean keyDown(int i) {
        if(i == Input.Keys.ESCAPE){
            this.dispose();
            Gdx.app.exit();
        }
        else if(i == Input.Keys.A) {
            left = true;
        }
        else if(i == Input.Keys.D){
            right = true;
        }
        else if(i == Input.Keys.W){
            up = true;
        }
        else if(i == Input.Keys.S){
            down = true;
        }
        else if(i == Input.Keys.SPACE){
            player.applyLinearImpulse(0f,5f,player.getWorldCenter().x,player.getWorldCenter().y, true);
        }
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        if(i == Input.Keys.A){
            left = false;
        }
        else if(i == Input.Keys.D){
            right = false;
        }
        else if(i == Input.Keys.W){
            up = false;
        }
        else if(i == Input.Keys.S){
            down = false;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }

    @Override
    public void show() {

    }



    @Override
    public void resize(int i, int i1) {
        gamePort.update(i, i1);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
       // world.dispose();

    }
}
