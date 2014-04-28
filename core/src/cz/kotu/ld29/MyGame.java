package cz.kotu.ld29;

import box2dLight.DirectionalLight;
import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.app;
import static cz.kotu.ld29.Tex.Tex;

public class MyGame extends ApplicationAdapter {

    private static final int RAYS_PER_BALL = 128;
    private static final int BALLSNUM = 5;
    /**
     * our boxes *
     */
    private ArrayList<Body> balls = new ArrayList<Body>(BALLSNUM);
    private static final float LIGHT_DISTANCE = 16f;
    private static final float radius = 1f;
    SpriteBatch batch;
    ShapeRenderer shapeRenderer;
    Grid mGrid;
    Stage mStage;
    ExtendViewport mViewport;
    RayHandler rayHandler;
    private World world;
    private DirectionalLight mDayLight;

    @Override
    public void create() {
        Tex.Tex = new Tex();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        // emulated display size
//        mViewport = new ExtendViewport(160, 120);
        mViewport = new ExtendViewport(320, 240);
        mStage = new Stage(mViewport);

        Gdx.input.setInputProcessor(mStage);

        mGrid = new Grid();
        mStage.addActor(mGrid);

        createPhysicsWorld();

        createBox2dLight();

        createAnt(4, 2);

        createAnt(16, 4);

        createAnt(8, 8);

        createQueen(10, 4);

    }

    private void createAnt(int x, int y) {
        placeAnt(x, y, new Ant());
    }

    private void createQueen(int x, int y) {
        placeAnt(x, y, new Queen());
    }

    private void placeAnt(int x, int y, Ant ant) {
        ant.setGridPos(x, y);
        mStage.addActor(ant);
        mGrid.getField(x, y).setEmpty();
        mGrid.getField(x - 1, y).setEmpty();
        mGrid.getField(x + 1, y).setEmpty();
    }

    private void createPhysicsWorld() {

        world = new World(new Vector2(0, -10), true);

//        ChainShape chainShape = new ChainShape();
//        chainShape.createLoop(new Vector2[]{new Vector2(-22, 1),
//                new Vector2(22, 1), new Vector2(22, 31), new Vector2(0, 20),
//                new Vector2(-22, 31)});
//        BodyDef chainBodyDef = new BodyDef();
//        chainBodyDef.type = BodyDef.BodyType.StaticBody;
//        groundBody = world.createBody(chainBodyDef);
//        groundBody.createFixture(chainShape, 0);
//        chainShape.dispose();
    }

    private void createBalls() {
        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(radius);

        FixtureDef def = new FixtureDef();
        def.restitution = 0.9f;
        def.friction = 0.01f;
        def.shape = ballShape;
        def.density = 1f;
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.type = BodyDef.BodyType.DynamicBody;

        for (int i = 0; i < BALLSNUM * 2; i++) {
            // Create the BodyDef, set a random position above the
            // ground and create a new body
//            boxBodyDef.position.x = 0 + (float) (Math.random() * mGrid.width * mGrid.WP);
//            boxBodyDef.position.y = 0 + (float) (Math.random() * mGrid.height * mGrid.HP);
            boxBodyDef.position.x = 0 + (float) (Math.random() * 10);
            boxBodyDef.position.y = 0 + (float) (Math.random() * 8);
            Body boxBody = world.createBody(boxBodyDef);
            boxBody.createFixture(def);
            balls.add(boxBody);
        }
        ballShape.dispose();
    }

    private void updateLightBoxes() {

//        createBox(1, 0);
//
//        createBox(4, 4);

        for (int y = 0; y < mGrid.height; y++) {
            for (int x = 0; x < mGrid.width; x++) {
                updateFieldLightBox(y, x);
            }
        }

    }

    private void updateFieldLightBox(int y, int x) {
        Grid.Field field = mGrid.getField(x, y);

        if (field.blocksLight()) {
            if (field.lightBox == null) {
                field.lightBox = createBox(x, y);
            }
        } else {
            if (field.lightBox != null) {
                world.destroyBody(field.lightBox);
                field.lightBox = null;
            }
        }

    }

    private Body createBox(int x, int y) {
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(0.5f, 0.5f);

        FixtureDef def = new FixtureDef();
        def.shape = boxShape;
        BodyDef boxBodyDef = new BodyDef();
        boxBodyDef.position.set(x + 0.5f, y + 0.5f);
        Body boxBody = world.createBody(boxBodyDef);
        boxBody.createFixture(def);

        boxShape.dispose();

        return boxBody;
    }


    private void createBox2dLight() {
        /** BOX2D LIGHT STUFF BEGIN */
//        RayHandler.setGammaCorrection(true);
//        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(world);
//        rayHandler.setAmbientLight(0.2f, 0.2f, 0.2f, 0.1f);
//        rayHandler.setCulling(true);
//        rayHandler.setBlur(false);
        rayHandler.setBlurNum(1);
        rayHandler.setShadows(true);
        mViewport.getCamera().update(true);

        // rayHandler.setCombinedMatrix(camera.combined, camera.position.x,
        // camera.position.y, camera.viewportWidth * camera.zoom,
        // camera.viewportHeight * camera.zoom);
        for (int i = 0; i < 0; i++) {
            // final Color c = new Color(MathUtils.random()*0.4f,
            // MathUtils.random()*0.4f,
            // MathUtils.random()*0.4f, 1f);
            Light light = new PointLight(rayHandler, RAYS_PER_BALL);
            light.setDistance(LIGHT_DISTANCE);
//            light.setDistance(5);
            // Light light = new ConeLight(rayHandler, RAYS_PER_BALL, null,
            // LIGHT_DISTANCE, 0, 0, 0, 60);
            // light.setStaticLight(true);
            light.attachToBody(balls.get(i), 0, 0.5f);
//            light.setPosition(balls.get(i), 0, 0.5f);
            light.setColor(MathUtils.random(), MathUtils.random(),
                    MathUtils.random(), 1f);
            // light.setColor(0.1f,0.1f,0.1f,0.1f);

        }
//        Light redLight = new PointLight(rayHandler, RAYS_PER_BALL, Color.RED, LIGHT_DISTANCE, 0, 0);
        Color dayLight = new Color(64 / 255f, 156 / 255f, 255 / 255f, 1f);
        mDayLight = new DirectionalLight(rayHandler, 24, dayLight, -90);
        /** BOX2D LIGHT STUFF END */}

    @Override
    public void resize(int width, int height) {
        mStage.getViewport().update(width, height);
    }

    @Override
    public void dispose() {
        rayHandler.dispose();
        mStage.dispose();
    }

    @Override
    public void render() {

        float grey = 0.1f;

        Gdx.gl.glClearColor(grey, grey, grey, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        drawGrid();

        batch.setProjectionMatrix(mViewport.getCamera().combined);

        batch.begin();

//        batch.draw(badlogic, 0, 0);

//        for (Grid.Ant ant : mGrid.mFields) {
//            batch.draw(badlogic, ant.pos.x, ant.pos.y);
//        }

        batch.end();

        mStage.act(Gdx.graphics.getDeltaTime());
        mStage.draw();

        /** BOX2D LIGHT STUFF BEGIN */

        updateLightBoxes();

        Camera camera = mViewport.getCamera();

//        rayHandler.setCombinedMatrix(camera.combined, camera.position.x,
//                camera.position.y, camera.viewportWidth * camera.zoom,
//                camera.viewportHeight * camera.zoom);


        Matrix4 tempCombined = camera.combined.cpy();
        tempCombined.scl(16);

//        rayHandler.setCombinedMatrix(camera.combined);
        rayHandler.setCombinedMatrix(tempCombined);

//        if (stepped)
        rayHandler.update();
        rayHandler.render();

        /** BOX2D LIGHT STUFF END */

    }

    public class Ant extends Actor {

        public static final float DIG_DURATION = 0.25f;
        public static final float FLIP_DURATION = 0.25f;
        public static final float WALK_DURATION = 0.5f;
        // grid position
        final Vec pos = new Vec();
        final Store mCarry = new Store();
        final Light mPointLight;
        // horizontal direction 1 = facing right; -1 = facing left
        int hdir = 1;

        public Ant() {
            setSize(mGrid.WP, mGrid.HP);
            float lightDistance = 4;
            // candle light color
            Color lightColor = new Color(255 / 255f, 147 / 255f, 41 / 255f, 1);
            mPointLight = new PointLight(rayHandler, RAYS_PER_BALL, lightColor, lightDistance, pos.x, pos.y);
        }

        private void setGridPos(int x, int y) {
            setPosition(x * mGrid.WP, y * mGrid.HP);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            pos.set(MathUtils.round(getX() / mGrid.WP), MathUtils.round(getY() / mGrid.HP));
            mPointLight.setPosition((getX() / mGrid.WP) + 0.5f, getY() / mGrid.HP + 0.5f);
            // turn off on daylight:
//            mPointLight.setActive(!rayHandler.pointAtLight(mPointLight.getPosition().x, mPointLight.getPosition().y));
            mPointLight.setActive(!mDayLight.contains(mPointLight.getPosition().x, mPointLight.getPosition().y));

            if (getActions().size == 0) {
                Action nextAction = getNextAction();
                if (nextAction != null) {
                    addAction(nextAction);
                }
            } else {
                // just animate
            }
        }

        Action getNextAction() {
            final boolean wantRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
            final boolean wantLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
            final boolean wantUp = Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W);
            final boolean wantDown = Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S);
            final boolean wantDig = Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.F);
            final boolean wantDrop = Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.F);

            boolean freeRight = relativeField(1, 0).isEmpty();
            boolean freeLeft = relativeField(-1, 0).isEmpty();
            boolean freeUp = relativeField(0, 1).isEmpty();
            boolean freeDown = relativeField(0, -1).isEmpty();

            Ant occupantInFront = null;
            Store storeInFront = relativeField(hdir, 0).mStore;
            for (Ant occupant : relativeField(hdir, 0).occupants) {
                occupantInFront = occupant;
                storeInFront = occupantInFront.mCarry;
            }

            boolean canDig = mCarry.isEmpty() && storeInFront.isPickable();
            boolean canDrop = mCarry.isPickable() && storeInFront.isEmpty();

            boolean doDig = wantDig && canDig;
            boolean doDrop = wantDrop && canDrop;

            if (wantRight != wantLeft) {
                int dir = ((wantRight ? 1 : 0) + (wantLeft ? -1 : 0));
                if (dir != hdir) {
                    hdir = dir;
//                  flipSide:
                    return Actions.delay(FLIP_DURATION);
                }
            }

            boolean onGround = !relativeField(0, -1).isEmpty();
            boolean climbing = !relativeField(hdir, 0).isEmpty();
            boolean onLedge = !relativeField(hdir, -1).isEmpty();

            boolean doRight = wantRight & freeRight;
            boolean doLeft = wantLeft & freeLeft;
            boolean doUp = wantUp & freeUp;
            boolean doDown = wantDown & freeDown;

            int dx = (doRight != doLeft) ? ((doRight ? 1 : 0) + (doLeft ? -1 : 0)) : 0;
            int dy = (doUp != doDown) ? ((doUp ? 1 : 0) + (doDown ? -1 : 0)) : 0;
            if (!(onGround || climbing || onLedge)) {
                // fall
                return getMoveAction(0, -1);
            } else if (dx != 0) {
                if (onGround) {
                    // ok
                    return getMoveAction(dx, 0);
                } else {
                    if (onLedge) {
                        // ok - climbed over ledge
                        return getMoveAction(dx, 0);
                    }
                }
            } else if (dy < 0) {
                // descent
                return getMoveAction(0, -1);
            } else if (dy > 0) {
                if (climbing) {
                    // climb
                    return getMoveAction(0, 1);
                } else {
                    // can not climb
                    dy = 0;
                }
            }

            if (doDig) {
                // field in front of ant
                mCarry.content = storeInFront.content;
                storeInFront.content = FieldType.VOID;
                app.log("Ant", "" + this + " picked: " + mCarry.content);
                return Actions.delay(DIG_DURATION);
            }

            if (doDrop) {
                // field in front of ant
                storeInFront.content = mCarry.content;
                mCarry.content = null;
                app.log("Ant", "" + this + " dropped: " + storeInFront.content);
                return Actions.delay(DIG_DURATION);
            }

            // grid coordinates
//            Grid.Field field = mGrid.getField(pos.x + dx, pos.y + dy);
//            Gdx.app.log("Ant", "g " + pos.x + ", " + pos.y + " d " + dx + ", " + dy + " " + field.isEmpty());

            return null;
        }

        private Action getMoveAction(int dx, int dy) {
            if (dx != 0 && dx != hdir) {
                // need to flip first
                hdir = dx;
//                  flipSide:
                return Actions.delay(FLIP_DURATION);
            } else {
                // add immediately so that we do not have two ants on the same field
                // TODO not so simple - we refresh every frame
                // mGrid.getField(pos.x + dx, pos.y + dy).occupants.add(this);

                // animate move:
                return Actions.moveBy(mGrid.WP * dx, mGrid.HP * dy, WALK_DURATION);
            }
        }

        private Grid.Field relativeField(int dx, int dy) {
            return mGrid.getField(pos.x + dx, pos.y + dy);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            TextureRegion texture = (headsRight()) ? Tex.redAntRight : Tex.redAntLeft;
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            // show what is carried
            TextureRegion carryTexture = mGrid.getTextureForType(mCarry);
            if (carryTexture != null) {
                batch.draw(carryTexture, getX() + 4 + 6 * hdir, getY() + 1, 8f, 8f);
            }
        }

        boolean headsRight() {
            return hdir > 0;
        }
    }

    class Queen extends Ant {

        Queen() {

            mPointLight.setColor(Color.RED);
        }

//        @Override
//        public void act(float delta) {
//            super.act(delta);
//            pos.set(MathUtils.round(getX() / mGrid.WP), MathUtils.round(getY() / mGrid.HP));
//            mPointLight.setPosition((getX() / mGrid.WP) + 0.5f, getY() / mGrid.HP + 0.5f);
//            // turn off on daylight:
////            mPointLight.setActive(!rayHandler.pointAtLight(mPointLight.getPosition().x, mPointLight.getPosition().y));
//            mPointLight.setActive(!mDayLight.contains(mPointLight.getPosition().x, mPointLight.getPosition().y));
//
//            if (getActions().size == 0) {
//                Action nextAction = getNextAction();
//                if (nextAction != null) {
//                    addAction(nextAction);
//                }
//            } else {
//                // just animate
//            }
//        }

        @Override
        Action getNextAction() {
            if (mCarry.content == FieldType.LEAF) {
                // consume
                mCarry.content = null;
                // do not spawn on same place
                if (mGrid.getField(pos.x, pos.y + 1).occupants.isEmpty()) {
                    // spawn
                    createAnt(pos.x, pos.y + 1);
                }
            } else {
                // consume
                mCarry.content = null;
            }
            return Actions.delay(5); // 5s
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            TextureRegion texture = Tex.redAntQueeen;
            batch.draw(texture, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

            TextureRegion carryTexture = mGrid.getTextureForType(mCarry);
            if (carryTexture != null) {
                batch.draw(carryTexture, getX() + 4, getY() + 1, 8f, 8f);
            }

        }


    }


}

