package com.example.asteroiddetector;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.graphics.Canvas;

import androidx.annotation.NonNull;


public class SystemView extends View {

    private static final long DUREE_ANNEE = 10000;

    public enum Centre {SOLEIL, TERRE, ASTEROIDE}

    private Centre centre = Centre.SOLEIL;
    private float angleTerre;
    private float angleAsteroide;

    public SystemView(Context context) {
        super(context);
        lireCouleurs();
    }

    public SystemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        lireCouleurs();
    }

    private final Paint pinceau = new Paint(Paint.ANTI_ALIAS_FLAG);

    private static final double ORBITE_TERRE = 149597870;

    private int couleurSoleil, couleurTerre, couleurAsteroide, couleurOrbite;
    private float cx, cy, rayonTerre;
    private double distance;

    public void setDistance(double distance) {
        this.distance = distance;
        invalidate();
    }

    public void setPeriode(double periode) {
        this.periode = periode;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
        invalidate();
    }

    private void lireCouleurs(){
        couleurSoleil = ContextCompat.getColor(getContext(), R.color.soleil);
        couleurTerre = ContextCompat.getColor(getContext(), R.color.terre);
        couleurAsteroide = ContextCompat.getColor(getContext(), R.color.asteroide);
        couleurOrbite = ContextCompat.getColor(getContext(), R.color.orbite);
    }

    @Override
    protected void onSizeChanged(int w, int h, int ancienW, int ancienH) {
        super.onSizeChanged(w, h, ancienW, ancienH);
        cx = w / 2f;
        cy = h / 2f;
        rayonTerre = Math.min(cx, cy) * 0.7f;

    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        float rayon = rayonTerre;
        if (centre == Centre.ASTEROIDE) {
            rayon = (float) (rayonTerre / (1 + distance / ORBITE_TERRE));
        }
        float rayonAsteroide = (float) (distance / ORBITE_TERRE * rayon);
        float a = -45 + angleAsteroide;

        canvas.save();
        canvas.translate(cx, cy);
        if (centre == Centre.ASTEROIDE) {
            canvas.rotate(angleTerre + a);
            canvas.translate(-rayonAsteroide, 0);
            canvas.rotate(-a);
            canvas.translate(-rayon, 0);
        } else {
            canvas.rotate(angleTerre);
            if (centre == Centre.TERRE) {
                canvas.translate(-rayon, 0);
            }
        }

        pinceau.setStyle(Paint.Style.STROKE);
        pinceau.setStrokeWidth(3);
        pinceau.setColor(couleurOrbite);
        if (centre == Centre.SOLEIL) {
            canvas.drawCircle(0, 0, rayon, pinceau);
        } else {
            canvas.drawCircle(rayon, 0, rayon, pinceau);
        }
        if (centre != Centre.ASTEROIDE) {
            canvas.drawCircle(rayon, 0, rayonAsteroide, pinceau);
        }

        pinceau.setStyle(Paint.Style.FILL);
        pinceau.setColor(couleurSoleil);
        canvas.drawCircle(0, 0, rayonTerre * 0.12f, pinceau);
        pinceau.setColor(couleurTerre);
        canvas.drawCircle(rayon, 0, rayonTerre * 0.07f, pinceau);

        canvas.save();
        canvas.rotate(a, rayon, 0);
        if (centre == Centre.ASTEROIDE) {
            pinceau.setStyle(Paint.Style.STROKE);
            pinceau.setColor(couleurOrbite);
            canvas.drawCircle(rayon + rayonAsteroide, 0, rayonAsteroide, pinceau);
            pinceau.setStyle(Paint.Style.FILL);
        }
        pinceau.setColor(couleurAsteroide);
        canvas.drawCircle(rayon + rayonAsteroide, 0, rayonTerre * 0.04f, pinceau);
        canvas.restore();

        canvas.restore();
    }

    private static final long PAS = 20;
    private double periode = 365;

    private final Runnable tic = new Runnable() {
        @Override
        public void run() {
            angleTerre = (angleTerre + 360f * PAS / DUREE_ANNEE) % 360;
            angleAsteroide = (float) ((angleAsteroide + 360.0 * PAS / (DUREE_ANNEE * periode / 365)) % 360);
            invalidate();
            postDelayed(this, PAS);
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(tic);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(tic);
    }


}
