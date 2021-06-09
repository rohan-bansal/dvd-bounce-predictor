package dev.rbansal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class DVD {

    private Sprite dvd;

    private Vector2 position;
    private Vector2 speed;
    private int width, height;
    private Color tintColor;

    Random generator;

    public DVD(float width, float height) {
        this.width = Math.round(width);
        this.height = Math.round(height);

        dvd = new Sprite(new Texture(Gdx.files.internal("dvd_logo.png")));

        tintColor = new Color();
        generator = new Random();

        position = new Vector2(
                generator.nextInt(this.width),
                generator.nextInt(this.height));

        speed = new Vector2(1, 1);

//        randomTintImage();
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

    private void calculate() {
        position.add(speed);

        if(position.x + dvd.getWidth() >= width) {
            speed.x = -speed.x;
            position.x = width - dvd.getWidth();
        } else if(position.x <= 0) {
            speed.x = -speed.x;
            position.x = 0;
        }

        if(position.y + dvd.getHeight() >= height) {
            speed.y = -speed.y;
            position.y = height - dvd.getHeight();
        } else if(position.y <= 0) {
            speed.y = -speed.y;
            position.y = 0;
        }

        dvd.setPosition(position.x, position.y);
    }

    public void draw(SpriteBatch batch) {

        calculate();

        dvd.draw(batch);
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;
    }

}
