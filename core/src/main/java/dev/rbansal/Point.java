package dev.rbansal;

import com.badlogic.gdx.math.Vector2;

public class Point {

    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(float x, float y) {
        this.x = Math.round(x);
        this.y = Math.round(y);
    }

    public Point(Vector2 vec) {
        this.x = Math.round(vec.x);
        this.y = Math.round(vec.y);
    }

    public Vector2 asVector() {
        return new Vector2(x, y);
    }
}
