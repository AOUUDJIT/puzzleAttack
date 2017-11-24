package com.nouveau.puzzleattack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
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
    private Resources   pRes;
    private Context 	pContext;


    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  //coordonnées en X du point d'ancrage de notre carte

    // initialisation de la taille

    static final int    carteTitleSize = 20;
    static final int    carteHeight = 6;
    static final int    carteWidth = 5;
    static final int    hitsNember=2;
    static final int    Timer=0;


    private     boolean in      = true;


    // constante modelisant les differentes types de cases
    static final int    CST_vide      = 0;
    static final int    CST_brique1     = 1;//YELLOW
    static final int    CST_brique2     = 2;//GREEN
    static final int    CST_brique3     = 3;//BLUE

    // tableau de reference du terrain
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
    private     Thread  cv_thread;
    Paint paint;

    public MainActivityView(Context context, AttributeSet attrs) {
        super(context,attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder= getHolder();
        holder.addCallback(this);

        // chargement des images
        pContext	    = context;
        pRes 		    = pContext.getResources();
        brickYellow 	= BitmapFactory.decodeResource(pRes, R.drawable.brickyellow);
        brickBlue 		= BitmapFactory.decodeResource(pRes, R.drawable.brickblue);
        brickGreen 		= BitmapFactory.decodeResource(pRes, R.drawable.brickgreen);

        cv_thread   = new Thread(this);

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

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }

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

    // dessin du jeu (fond uni, en fonction du jeu gagne ou pas dessin du plateau et du joueur des diamants et des fleches)
    private void nDraw(Canvas canvas) {
        canvas.drawRGB(44,44,44);
        if (isWon()) {
            initparameters(2);
        } else {
            paintcarte(canvas);
        }

    }




    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        Log.i("-> FCT <-", "surfaceChanged "+ width +" - "+ height);
        initparameters(1);

    }
    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceCreated");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        Log.i("-> FCT <-", "surfaceDestroyed");

    }

    public void run() {
        Canvas c = null;
        while (in) {
            try{
                try {
                    cv_thread.sleep(40);
                    c = holder.lockCanvas(null);
                    nDraw(c);
                } finally {
                    if (c != null) {
                        holder.unlockCanvasAndPost(c);
                    }
                }

            } catch(Exception e) {
                Log.e("-> RUN <-", "PB DANS RUN");
            }

             in = false;
        }
    }
    // verification que nous sommes dans le tableau
    private boolean IsOut(int x, int y) {
        if ((x < 0) || (x > carteWidth- 1)) {
            return true;
        }
        if ((y < 0) || (y > carteHeight- 1)) {
            return true;
        }
        return false;
    }


    //controle de la valeur d'une cellule
    private boolean IsCell(int x, int y, int mask) {
        if (carte[y][x] == mask) {
            return true;
        }
        return false;
    }

    // fonction permettant de recuperer les retours clavier
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        Log.i("-> FCT <-", "onKeyUp: "+ keyCode);

        /*int xTmpPlayer	= xPlayer;
        int yTmpPlayer  = yPlayer;
        int xchange 	= 0;
        int ychange 	= 0;

        if (keyCode == KeyEvent.KEYCODE_0) {
            initparameters(1);
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            ychange = -1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            ychange = 1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            xchange = -1;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            xchange = 1;
        }
        //xPlayer = xPlayer+ xchange;
        //yPlayer = yPlayer+ ychange;

        if (IsOut(xPlayer, yPlayer) || IsCell(xPlayer, yPlayer, CST_block)) {
            xPlayer = xTmpPlayer;
            yPlayer = yTmpPlayer;
        } else if (IsDiamant(xPlayer, yPlayer)) {
            int xTmpDiamant = xPlayer;
            int yTmpDiamant = yPlayer;
            xTmpDiamant = xTmpDiamant+ xchange;
            yTmpDiamant = yTmpDiamant+ ychange;
            if (IsOut(xTmpDiamant, yTmpDiamant) || IsCell(xTmpDiamant, yTmpDiamant, CST_block) || IsDiamant(xTmpDiamant, yTmpDiamant)) {
                xPlayer = xTmpPlayer;
                yPlayer = yTmpPlayer;
            } else {
                UpdateDiamant(xTmpDiamant- xchange, yTmpDiamant- ychange, xTmpDiamant, yTmpDiamant);
            }
        }*/
        return true;
    }

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
        Log.i("-> FCT <-", "onTouchEvent: "+ event.getX());
        if (event.getY()<50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_UP, null);
        } else if (event.getY()>getHeight()-50) {
            if (event.getX()>getWidth()-50) {
                onKeyDown(KeyEvent.KEYCODE_0, null);
            } else {
                onKeyDown(KeyEvent.KEYCODE_DPAD_DOWN, null);
            }
        } else if (event.getX()<50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
        } else if (event.getX()>getWidth()-50) {
            onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
        }else
        if(isWon()){
            if( event.getX() > 0 && event.getX() < 0 + carteLeftAnchor+ 3*carteTitleSize && event.getX()> 0 && event.getX()< 0 + carteTopAnchor+ 4*carteTitleSize )
            {
                initparameters(2);
            }
        }
        return super.onTouchEvent(event);
    }

}
