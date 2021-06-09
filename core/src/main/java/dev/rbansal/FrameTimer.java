package dev.rbansal;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class FrameTimer {

    float[] frameTimer = new float[Constants.FRAME_BUFFER_MAX];
    int counter = 0;
    float frameAverage = 0f;
    boolean safe = false;

    DVD dvd;
    Vector2 dvdSpeed;
    Vector2 dvdSize;
    long startTime;

    public FrameTimer(DVD dvd) {
        this.dvd = dvd;
        dvdSpeed = dvd.getSpeed();
        dvdSize = dvd.getSize();

        startTime = System.currentTimeMillis();
    }

    public String getFormattedTimeTilCorner() {
        float timeToCross = (Gdx.graphics.getWidth() / (Math.abs(dvdSpeed.x) / frameAverage));
        float LCM = MathTools.lcm(
                Math.round((Gdx.graphics.getHeight() - dvdSize.y)),
                Math.round((Gdx.graphics.getWidth() - dvdSize.x)));
        float answer = (timeToCross * LCM) / (Gdx.graphics.getWidth() - dvdSize.x);
        return MathTools.formatSeconds(Math.round(answer - ((float)(System.currentTimeMillis() - startTime) / 1000)));
    }

    public String getBouncesTilCorner() {
        int totalBounces = MathTools.lcm(Math.round((Gdx.graphics.getHeight() - dvdSize.y)), Math.round((Gdx.graphics.getWidth() - dvdSize.x)));
        int currentBounces = dvd.getBounces();
        return MathTools.commaNumber(totalBounces - currentBounces);
    }

    public boolean isSafe() {
        return safe;
    }

    public void reset() {
        counter = 0;
        frameAverage = 0f;
        frameTimer = new float[frameTimer.length];
    }

    public void tick() {
        if(counter <= 29) {
            frameTimer[counter] = Gdx.graphics.getDeltaTime();
            counter++;
            safe = false;
        } else {
            if(frameAverage == 0f) {
                frameAverage = MathTools.average(frameTimer);
                safe = true;
            }
        }
    }

}
