package dev.rbansal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;

import java.util.Random;

public class DVD {

    private Sprite dvd;

    private Vector2 startingPoint;
    private Vector2 position;
    private Vector2 speed;
    private int width, height;
    private Color tintColor;

    private int bounces;

    Random generator;
    DelayedRemovalArray<Point> predictPoints = new DelayedRemovalArray<>();

    public DVD(float width, float height) {
        this.width = Math.round(width);
        this.height = Math.round(height);

        dvd = new Sprite(new Texture(Gdx.files.internal("dvd_logo.png")));

        tintColor = new Color();
        generator = new Random();

        position = new Vector2(0, 0);
//                generator.nextInt(this.width),
//                generator.nextInt(this.height));
        startingPoint = new Vector2(position);

        speed = new Vector2(Constants.MOVE_SPEED_PX, Constants.MOVE_SPEED_PX);

    }

    private void randomTintImage() {
        tintColor.r = generator.nextInt(255);
        tintColor.g = generator.nextInt(255);
        tintColor.b = generator.nextInt(255);

        if(!dvd.getTexture().getTextureData().isPrepared()) {
            dvd.getTexture().getTextureData().prepare();
        }

        Pixmap map = dvd.getTexture().getTextureData().consumePixmap();

        map.setBlending(Pixmap.Blending.SourceOver);
        map.setColor(tintColor);

        dvd.setTexture(new Texture(map));

        map.dispose();
    }

    private void calculateAndSetPosition(Vector2 position, Vector2 speed, boolean predicting) {
        position.add(speed);

        if(position.x + dvd.getWidth() >= width) {
            speed.x = -speed.x;
            position.x = width - dvd.getWidth();
            if(!predicting) bounces++;
        } else if(position.x <= 0) {
            speed.x = -speed.x;
            position.x = 0;
            if(!predicting) bounces++;
        }

        if(position.y + dvd.getHeight() >= height) {
            speed.y = -speed.y;
            position.y = height - dvd.getHeight();
            if(!predicting) bounces++;
        } else if(position.y <= 0) {
            speed.y = -speed.y;
            position.y = 0;
            if(!predicting) bounces++;
        }

    }

    public DelayedRemovalArray<Point> predict() {
        predictPoints.clear();

        Vector2 curPos = new Vector2(position);
        Vector2 curSpeed = new Vector2(speed);
        Vector2 prevSpeed;

        do {
            prevSpeed = new Vector2(curSpeed);
            calculateAndSetPosition(curPos, curSpeed, true);
            if(prevSpeed.x != curSpeed.x || prevSpeed.y != curSpeed.y) {
                if(predictPoints.size == 0) {
                    predictPoints.add(new Point(
                            dvd.getX() + (dvd.getWidth() / 2),
                            dvd.getY() + (dvd.getHeight() / 2)));
                }
                predictPoints.add(new Point(
                        curPos.x + (dvd.getWidth() / 2),
                        curPos.y + (dvd.getHeight() / 2)));

            }
        } while (predictPoints.size < Constants.FUTURE_BOUNCE_MAX);
        return predictPoints;
    }

    public void draw(SpriteBatch batch) {

        calculateAndSetPosition(this.position, this.speed, false);

        dvd.setPosition(position.x, position.y);

        dvd.draw(batch);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Vector2 getSpeed() {
        return speed;
    }

    public Vector2 getSize() {
        return new Vector2(dvd.getWidth(), dvd.getHeight());
    }

    public int getBounces() {
        return bounces;
    }

}
