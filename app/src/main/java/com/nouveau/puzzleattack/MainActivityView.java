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

    //déclaration des images utilisées
    private Bitmap vide;
    private Bitmap brickYellow;
    private Bitmap brickOrange;
    private Bitmap brickBlue;
    private Bitmap background;
    private Bitmap timer;



    private long beginTimer, endTimer;


    int nbrTouch=0;
    int xDown = 0;
    int yDown = 0;




    // Declaration des objets Ressources et Context permettant d'accéder aux ressources de notre application et de les charger
    private Resources   pRes;
    private Context 	pContext;

    // déclaration de la matrice
    int [][] carte;


    // ancres pour pouvoir centrer la carte du jeu
    int        carteTopAnchor;                   // coordonnées en Y du point d'ancrage de notre carte
    int        carteLeftAnchor;                  //coordonnées en X du point d'ancrage de notre carte

    // initialisation de la taille

    static final int    carteTitleSize = 20;
    static final int    carteHeight = 6;
    static final int    carteWidth = 6;
    static final int    hitsNember=2;
    static final int    Timer=0;
    int    sizeCST = 0;


    // constante modelisant les differentes types de cases
    static final int    CST_vide      = 0;
    static final int    CST_brique1     = 1;//YELLOW
    static final int    CST_brique2     = 2;//ORANGE
    static final int    CST_brique3     = 3;//BLUE

    // tableau de reference du terrain
    int [][] puzzle1={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_brique1,CST_brique1,CST_vide,CST_brique1,CST_vide,CST_vide},
    } ;
    int [][] puzzle2={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide,CST_vide},
                      {CST_brique1,CST_brique1,CST_brique2,CST_brique1,CST_vide,CST_vide},
    } ;

    int [][] puzzle3={{CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_vide,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique2,CST_vide},
                      {CST_vide,CST_vide,CST_vide,CST_vide,CST_brique3,CST_vide},
                      {CST_vide,CST_vide,CST_brique2,CST_brique2,CST_brique3,CST_vide},
                      {CST_vide,CST_brique1,CST_brique1,CST_brique3,CST_brique1,CST_vide},
    } ;


    SurfaceHolder holder;
    private     Thread  cv_thread;
    private     boolean in      = true;
    Paint paint;

    public MainActivityView(Context context, AttributeSet attrs) {
        super(context,attrs);

        // permet d'ecouter les surfaceChanged, surfaceCreated, surfaceDestroyed
        holder= getHolder();
        holder.addCallback(this);

        // chargement des images
        pContext	    = context;
        pRes 		    = pContext.getResources();
        brickYellow 	= BitmapFactory.decodeResource(pRes, R.drawable.yellow);
        brickBlue 		= BitmapFactory.decodeResource(pRes, R.drawable.blue);
        brickOrange 		= BitmapFactory.decodeResource(pRes, R.drawable.orange);
        vide 		    = BitmapFactory.decodeResource(pRes, R.drawable.empty);
        background 		= BitmapFactory.decodeResource(pRes, R.drawable.background);
        timer 		= BitmapFactory.decodeResource(pRes, R.drawable.timer);



        //sizeCST = brickYellow.getWidth();


        initparameters(1);

        cv_thread   = new Thread(this);

        // prise de focus pour gestion des touches
        setFocusable(true);
    }

    public void startTimer(){
        beginTimer = System.currentTimeMillis();
    }
    public void stopTimer(){
        endTimer = System.currentTimeMillis();
    }
    public double getTimer() {
        return ((endTimer - beginTimer) / 1000);
    }

    // chargement du niveau a partir du tableau de reference du niveau
    private void loadlevel(int l) {
        if (l == 1) {
            for (int i = 0; i < carteWidth; i++) {
                for (int j = 0; j < carteHeight; j++) {
                    carte[j][i] = puzzle1[j][i];
                }
            }
        }
        else if (l==2) {
            for (int i = 0; i < carteWidth; i++) {
                for (int j = 0; j < carteHeight; j++) {
                    carte[j][i] = puzzle2[j][i];
                }
            }

        }
        else if (l==3) {
            for (int i = 0; i < carteWidth; i++) {
                for (int j = 0; j < carteHeight; j++) {
                    carte[j][i] = puzzle3[j][i];
                }
            }

        }

        startTimer();

    }

    // initialisation du jeu
    public void initparameters(int l) {

        Log.e("-FCT-", " initparameters()");
        carte           = new int[carteHeight][carteWidth];
        loadlevel(l);
        carteTopAnchor = getHeight()-525;
        carteLeftAnchor = (getWidth()) / carteWidth;
        sizeCST= (getWidth())/6;

        if ((cv_thread!=null) && (!cv_thread.isAlive())) {
            cv_thread.start();
            Log.e("-FCT-", "cv_thread.start()");
        }

    }

    // dessin du Timer
    private void paintTimer(Canvas canvas) {
        canvas.drawBitmap(timer, getWidth()-40, 10 , null);
    }

    // dessin de la carte du jeu
    private void paintcarte(Canvas canvas) {
        Log.e("-FCT-", "paintcarte()");
        for (int i=0; i< carteHeight; i++) {
            for (int j=0; j< carteWidth; j++) {
                switch (carte[i][j]) {
                    case CST_brique1:
                        canvas.drawBitmap(brickYellow, j*sizeCST, carteTopAnchor+ i*sizeCST, null);
                        break;
                    case CST_brique2:
                        canvas.drawBitmap(brickOrange, j*sizeCST, carteTopAnchor+ i*sizeCST, null);
                        break;
                    case CST_brique3:
                        canvas.drawBitmap(brickBlue,j*sizeCST, carteTopAnchor+ i*sizeCST, null);
                        break;
                    case CST_vide:
                        canvas.drawBitmap(vide,j*sizeCST, carteTopAnchor+ i*sizeCST, null);
                        break;
                }
            }
        }
    }

    //dessin du fond
    private void paintFond(Canvas canvas) {
        canvas.drawBitmap(background, 1, 1, null);
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
        paintFond(canvas);

        if (isWon()) {
            paintcarte(canvas);
            initparameters(2);
        } else {
            paintcarte(canvas);
        }

    }


    // callback sur le cycle de vie de la surfaceview
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
                cv_thread.sleep(40);
                stopTimer();
                try {
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

    // fonction permettant de recuperer les evenements tactiles
    public boolean onTouchEvent (MotionEvent event) {
        //Log.i("-> FCT <-", "onTouchEvent: " + event.getX());

        if(nbrTouch<2){

            int action = event.getAction();
            int x = (int) (event.getX() / sizeCST);
            int y = (int) ((event.getY() - carteTopAnchor) / sizeCST);

            boolean empty = true;
            int temp = 0;
            int sens = 0;

            switch(action){

                case MotionEvent.ACTION_DOWN:
                    xDown=x;
                    yDown=y;
                    return true;

                case MotionEvent.ACTION_MOVE:
                    return true;

                case MotionEvent.ACTION_UP:
                    if ((x - xDown) > 0) { sens = 1;  }  //Déplacement à droite
                    if ((x - xDown) < 0) { sens = -1; }  //Déplacement à gauche


                    if (yDown >= 0)
                    {
                        //si la case qu'on a touchée n'est pas vide
                        if (carte[yDown][xDown] != CST_vide)
                        {
                            if (sens != 0) { nbrTouch++; }

                            //on verifie que la case n'est pas à l'extrémité de la carte X
                            if ((sens == 1 && xDown < carteWidth) || ( sens == -1 && xDown > 0))
                            {
                                //on fait notre permutation
                                Log.i("-> FCT <-", "PERMUTATION");
                                temp = carte[yDown][xDown];
                                carte[yDown][xDown] = carte[yDown][xDown + (sens)];
                                carte[yDown][xDown + ( sens )] = temp;

                                //si notre case initiale a été remplacée par une case vide, on fait descendre
                                //ce qu'il y'a au dessus ( si ce n'est pas vide )
                                if (carte[yDown][xDown] == CST_vide)
                                {
                                    if (yDown > 0)
                                    {
                                        if (carte[yDown - 1][xDown] != CST_vide)
                                        {
                                            int a = yDown;
                                            while (a > 0)
                                            {
                                                carte[a][xDown] = carte[a - 1][xDown];
                                                carte[a - 1][xDown] = CST_vide;
                                                a--;
                                            }
                                        }
                                    }
                                }

                                //on verifie que la case n'est pas à l'extrémité de la carte Y
                                if (yDown < carteHeight - 1)
                                {
                                    //si après permutation notre case est suspendu sur un vide, on la fait descendre
                                    if (carte[yDown + 1][xDown + ( sens) ] == CST_vide)
                                    {
                                        int x1 = xDown + ( sens );
                                        int y1 = yDown + 1;
                                        do{
                                            empty = false;
                                            if (carte[y1][x1] == CST_vide)
                                            {
                                                empty=true;
                                                carte[y1][x1] = carte[y1 - 1][x1];
                                                carte[y1 - 1][x1] = CST_vide;
                                            }
                                            y1++;
                                        }while (empty & y1 < carteHeight);
                                    }
                                }
                            }
                        }
                    }

                    return true;

            }

        }
        return super.onTouchEvent(event);

    }
}
