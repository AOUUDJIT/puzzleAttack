package com.nouveau.puzzleattack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by User on 13/11/2017.
 */

public class MainActivityView extends SurfaceView implements SurfaceHolder.Callback, Runnable{
    // déclaration de la matrice
    int [][] carte;
    //déclaration des images utilisées
    private Bitmap vide;
    private Bitmap brickYellow;
    private Bitmap brickGreen;
    private Bitmap brickBlue;

    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources pRes;
    private Context 	pContext;

    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  //coordonnées en X du point d'ancrage de notre carte

    // initialisation de la taille

    static final int    carteTitleSize = 20;
    static final int    carteHeight = 6;
    static final int    carteWidth = 5;
    static final int    hitsnbr=2;
    static final int    Timer=0;


    // constante modelisant les differentes types de cases
    static final int    CST_vide      = 0;
    static final int    CST_brique1     = 1;//YELLOW
    static final int    CST_brique2     = 2;//GREEN
    static final int    CST_brique3     = 3;


    int [][] puzzle1={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_brique1,CST_brique1,CST_vide,CST_brique1,CST_vide,CST_vide},
    } ;
    int [][] puzzle2={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                     {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                     {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                     {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                     {CST_brique1,CST_brique1,CST_brique2,CST_brique1,CST_vide,CST_vide},
    } ;

    int [][] puzzle3={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide},
            {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique3,CST_vide},
            {CST_vide,CST_vide,CST_brique2,CST_brique2,CST_brique3,CST_vide},
            {CST_vide,CST_brique1,CST_brique1,CST_brique3,CST_brique1,CST_vide},
            } ;

    SurfaceHolder holder;
    private     Thread  thread;
    Paint paint;

    public MainActivityView(Context context) {
        super(context);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder= getHolder();
        holder.addCallback(this);

        // chargement des images
        pContext	= context;
        pRes 		= pContext.getResources();
        brickYellow 		= BitmapFactory.decodeResource(pRes, R.drawable.brickYellow);
        brickBlue 		= BitmapFactory.decodeResource(pRes, R.drawable.brickBlue);
        brickGreen 		= BitmapFactory.decodeResource(pRes, R.drawable.brickGreen);

        initparameters(1);



        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    private void loadlevel(int l) {
        if (l == 1) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    carte[j][i] = puzzle1[j][i];
                }
            }
        }
        else if (l==2) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    carte[j][i] = puzzle2[j][i];
                }
            }

        }
        else if (l==3) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 6; j++) {
                    carte[j][i] = puzzle3[j][i];
                }
            }

        }


    }

    // initialisation du jeu
    public void initparameters(int l) {
        paint = new Paint();
        paint.setColor(0xff0000);

        paint.setDither(true);
        paint.setColor(0xFFFFFF00);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(3);
        paint.setTextAlign(Paint.Align.LEFT);
        carte           = new int[carteHeight][carteWidth];
        carteTopAnchor  = (getHeight()- carteHeight*carteTitleSize)/2;
        carteLeftAnchor = (getWidth()- carteWidth*carteTitleSize)/2;

    }

    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                switch (carte[i][j]) {
                    case CST_brique1:
                        canvas.drawBitmap(brickYellow, carteLeftAnchor+ j*carteTitleSize, carteTopAnchor+ i*carteTitleSize, null);
                        break;
                    case CST_brique2:
                        canvas.drawBitmap(brickGreen,carteLeftAnchor+ j*carteTitleSize, carteTopAnchor+ i*carteTitleSize, null);
                        break;
                    case CST_brique3:
                        canvas.drawBitmap(brickBlue,carteLeftAnchor+ j*carteTitleSize, carteTopAnchor+ i*carteTitleSize, null);
                        break;
                    case CST_vide:
                        canvas.drawBitmap(vide,carteLeftAnchor+ j*carteTitleSize, carteTopAnchor+ i*carteTitleSize, null);
                        break;
                }
            }
        }
    }

    // permet d'identifier si la partie est gagnee (tous les diamants à leur place)
    private boolean isWon() {
        for (int i=0; i< 4; i++) {

        }
        return true;
    }

    private void nDraw(Canvas canvas) {
        canvas.drawRGB(44,44,44);
        if (isWon()) {
            initparameters(2);
        } else {
            paintcarte(canvas);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void run() {

    }
}
