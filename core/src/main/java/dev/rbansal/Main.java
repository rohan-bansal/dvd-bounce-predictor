package dev.rbansal;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.viewport.*;


public class Main extends ApplicationAdapter {

    OrthographicCamera camera;
    SpriteBatch batch;
    ShapeRenderer shape;
    Viewport viewport;

    DVD dvd;
    FrameTimer timer;

    DelayedRemovalArray<Point> points;
    BitmapFont font;

    String timeTilCorner = "";
    String bouncesTilCorner = "";

    int initialFrameSkip = 0;

    @Override
    public void create() {
        super.create();

        font = new BitmapFont(Gdx.files.internal("ari2.fnt"));
        font.getData().setScale(0.5f);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        viewport = new ScreenViewport(camera);

        batch = new SpriteBatch();
        shape = new ShapeRenderer();

        dvd = new DVD(camera.viewportWidth, camera.viewportHeight);
        points = new DelayedRemovalArray<>();

        timer = new FrameTimer(dvd);
    }

    @Override
    public void render() {

        initialFrameSkip++;
        timer.tick();

        Gdx.gl.glClearColor(26 / 255f, 24 / 255f, 27 / 255f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);


        batch.begin();

        dvd.draw(batch);

            font.draw(batch, "[x] recalibrate", 20, 100);
            font.draw(batch, "[z]<hold> next 30 bounces", 20, 80);
        font.draw(batch, timeTilCorner, 20, 40);
        font.draw(batch, bouncesTilCorner, 20, 20);

        batch.end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if(timer.isSafe()) {
                timer.reset();
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.Z)) {
            points = dvd.predict();

            shape.begin(ShapeRenderer.ShapeType.Line);
            for(int i = 0; i < points.size - 1; ++i) {

                if(i <= 2) {
                    shape.setColor(Color.ORANGE);
                } else {
                    shape.setColor(Color.GREEN);
                }

                shape.line(points.get(i).asVector(), points.get(i + 1).asVector());
            }
            shape.end();
        } else {
            if(points.size != 0) {
                points.clear();
            }
        }

        if(initialFrameSkip == Constants.FRAME_BUFFER_MAX) {
            timer.reset();
        } else {
            if(timer.isSafe()) {
                timeTilCorner = "~ " + timer.getFormattedTimeTilCorner() + " until a perfect corner";
                bouncesTilCorner = "~ " + timer.getBouncesTilCorner() + " bounces until a perfect corner";
            }
        }



    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        dvd.resize(width, height);
    }
}