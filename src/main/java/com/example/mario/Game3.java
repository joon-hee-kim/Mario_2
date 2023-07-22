package com.example.mario;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.provider.BaseColumns;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;


public class Game3 extends View {

    /* 테이블 내용 정의 */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String _ID = "_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Game.FeedEntry.TABLE_NAME + " (" +
                    Game.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    Game.FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    Game.FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Game.FeedEntry.TABLE_NAME;

    class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";

        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    Game3.FeedReaderDbHelper dbHelper = new Game3.FeedReaderDbHelper(getContext());

    //DB저장
    int save = 0; // 0이 저장된 것
    int savecount = 1;

    int n, m, d;
    int scrw, scrh;
    float xd, yd;
    int count = 0; //캐릭터 이동을 위한 카운트 수 저장할 정수형 변수
    boolean start = false; //캐릭터 방향키 버튼 클릭 유무
    private String DirButton; //캐릭터 정지상태시
    private String DirButton2; // 캐릭터 이동상태시

    float [] rxd= new float [3];
    float [] ryd= new float [3];
    int [] count2= new int [3]; //적군 이동을 위한 카운트 수 저장할 정수형 변수
    int [] life=new int[3];
    int [] chr_life = new int[1];
    private String[] RectDirButton = new String[3];
    //위에서 3은 적군을 화면에 최대 3개까지만 표시할 것을 의미함

    int missileCount; //발사 가능한 최대 미사일 수
    int [] missileNum = new int [10];
    float [] mx = new float [10];
    float [] my = new float [10];
    int [] md = new int [10];
    int MD=3; //미사일 초기 방향

    Random random = new Random();
    //위에서 10은 화면에 최대 10개까지의 미사일 표시할 것을 의미

    Paint p = new Paint();
    private GameThread T;

    MediaPlayer mp;

    public Game3(Context con, AttributeSet at) {
        super(con, at);
    }

    @Override  //뷰의 크기가 변경될 때 호출
    protected void onSizeChanged(int sw, int sh, int esw, int esh) {
        super.onSizeChanged(sw, sh, esw, esh);
        this.scrw = sw;
        this.scrh = sh;

        for(int i=0; i<3; i++)
        {
            life[i] = 2;
        }
        for(int i=0; i<1; i++)
        {
            chr_life[i] = 30;
        }

        if (T == null) {
            T = new GameThread();
            T.start();
        }
    }

    @Override
    // 뷰가 원도우에서 분리될 때마다 발생
    protected void onDetachedFromWindow() {
        T.run = false;
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint rect = new Paint();
        rect.setColor(Color.BLACK);
        canvas.drawRect(0, 0, scrw, scrh, rect);

        p.setColor(Color.YELLOW);
        p.setTextSize(scrh / 16);
//        canvas.drawText("sw" + scrw + "sh" + scrh, 0, scrh / 16, p);

        Bitmap[] bg = new Bitmap[1];

        bg[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bg02);
        bg[0] = Bitmap.createScaledBitmap(bg[0], scrw, scrh, true);
        canvas.drawBitmap(bg[0], scrw % 64 / 2, scrh % 32 / 2, null);

        if (count == 0 && DirButton == "Up") {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("DELETE FROM entry");
            savecount = 1;
        }

        String title = "" + savecount;
        String subtitle = "Data: " + savecount;

        //DB 저장
        if (count == 0 && DirButton == "Left" && save == 0) {
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Game.FeedEntry.COLUMN_NAME_TITLE, title);
            values.put(Game.FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(Game.FeedEntry.TABLE_NAME, null, values);
            save = 1;
            savecount += 1;
            db.close();
        }

        if (count == 0 && DirButton == "Down") {
            save = 0;
        }

        //DB 읽기
        if (count == 0 && DirButton == "Right") {
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String a = "2";
            Cursor cursor1 = db.rawQuery("SELECT * FROM entry WHERE _id =" + a, null);
            if (cursor1.moveToFirst()) {
                String Title = cursor1.getString(2);
                if (Title == null) return;
                p.setTextSize(scrh / 16);
                canvas.drawText("" + Title, 0, 400, p);
            }

            cursor1.close();
            db.close();
        }

        Bitmap[] missile = new Bitmap[10];

        missileCount = 0;

        for (int i = 0; i < 10; i++) {
            missile[i] = BitmapFactory.decodeResource(getResources(), R.drawable.fire);
            missile[i] = Bitmap.createScaledBitmap(missile[i], scrw / 16, scrw / 16, true);

            if(missileNum[i] == 0) {
                missileCount += 1;
            }

            if (missileNum[i] == 1) {
                canvas.drawBitmap(missile[i], mx[i], my[i], null);
                if (md[i] == 1) {//왼쪽
                    mx[i] -= scrw / 64;
                }
                if (md[i] == 2) {//오른쪽
                    mx[i] += scrw / 64;
                }
                if (md[i] == 3) {//위쪽
                    my[i] -= scrh / 32;
                }
                if (md[i] == 4) {//아래쪽
                    my[i] += scrh / 32;
                }

                for (int j = 0; j < 3; j++) {
                    if (life[j] > 0 && mx[i] <= scrw / 2 + (scrw - scrw % 64) / 8 + rxd[j] && mx[i] >= scrw / 2 + rxd[j]) {
                        life[j] -= 1;
                        missileNum[i] = 0;
                        if (life[0]==0 && life[1]==0 && life[2]==0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage("게임을 클리어하셨습니다. 클리어화면으로 이동하겠습니까?")
                                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // "예" 버튼을 클릭했을 때 실행할 코드
                                            Intent intent = new Intent(getContext(), EndActivity.class);
                                            getContext().startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // "아니오" 버튼을 클릭했을 때 실행할 코드
                                            Intent intent = new Intent(getContext(), Wld2Activity.class);
                                            getContext().startActivity(intent);
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);
                            dialog.show();
                        }

                    }
                }
            }
            if (mx[i] > scrw - scrw / 16 || mx[i] < 0 || my[i] > scrh - scrw / 16 || my[i] < 0) {
                missileNum[i] = 0;
            }
        }

        Bitmap AS[][] = new Bitmap[1][4];
        Bitmap A = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.dir);
        A = Bitmap.createScaledBitmap(A, scrw / 8, scrh, true);

        for (int i = 0; i < 1; i++) {
            for (int j = 0; j < 4; j++) {
                AS[i][j] = Bitmap.createBitmap(A, i * scrw / 8, j * scrh / 4, scrw / 8, scrh / 4);
            }
        }
        canvas.drawBitmap(AS[0][0], scrw / 8, scrh - scrh / 2, null);
        canvas.drawBitmap(AS[0][1], 0, scrh - scrh / 4, null);
        canvas.drawBitmap(AS[0][2], scrw / 4, scrh - scrh / 4, null);
        canvas.drawBitmap(AS[0][3], scrw / 8, scrh - scrh / 4, null);

        Bitmap[] main = new Bitmap[12];

        for(int i=0; i<12; i++) {
            for(int j=0; j<1; j++) {
                for(int k=0; k<3; k++) {
                    main[i] = BitmapFactory.decodeResource(getResources(), R.drawable.luigi01 + i);
                    if (main[i] == null) return;
                    main[i] = Bitmap.createScaledBitmap(main[i], scrw / 8, scrh / 4, true);
                    if (i == n && chr_life[j] > 0) {
                        canvas.drawBitmap(main[i], scrw * 7 / 16 + xd, scrh - scrh / 4 + yd, null);
                        if (scrw * 7 / 16 + xd <= scrw/2+rxd[k]+scrw/8 && scrw * 7 / 16 + xd >= scrw/2+rxd[k] && scrh - scrh / 4 + yd <= scrh/2+ryd[k]+scrh/4 && scrh - scrh / 4 + yd >= scrh/2+ryd[k]) {
                            chr_life[j] -= 1;
                            if (chr_life[0] == 0) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("게임오버 하셨습니다. 다시 도전하겠습니까?")
                                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // "예" 버튼을 클릭했을 때 실행할 코드
                                                Intent intent = new Intent(getContext(), Play3Activity.class);
                                                getContext().startActivity(intent);
                                            }
                                        })
                                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // "아니오" 버튼을 클릭했을 때 실행할 코드
                                                Intent intent = new Intent(getContext(), Dead2Activity.class);
                                                getContext().startActivity(intent);
                                            }
                                        });
                                AlertDialog dialog = builder.create();
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.setCancelable(false);
                                dialog.show();
                            }

                        }
                    }
                }
            }
        }

        Bitmap[] man = new Bitmap[3];

        for(int i=0; i<3; i++) {
            for(int j=0; j<3; j++) {
                man[j] = BitmapFactory.decodeResource(getResources(), R.drawable.junior01 + j);
                man[j] = Bitmap.createScaledBitmap(man[j], scrw/8, scrh/4, true);
                if(life[i] > 0) {
                    canvas.drawBitmap(man[j],scrw/2+rxd[i], scrh/2+ryd[i], null);
                }
            }
        }
        canvas.drawText("HP:"+chr_life[0]+"적 생명력:"+life[0]+"적 생명력:"+life[1]+"적 생명력:"+life[2]+"발사 가능한 수"+missileCount, 0, scrh/16, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_POINTER_DOWN) {
            if ((int) event.getX() > scrw / 4 && (int) event.getX() < scrw * 3 / 8 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                if (start == false && count == 0) {
                    start = true;
                    DirButton = "Right";
                }
                DirButton2 = "Right";
            } else if ((int) event.getX() > 0 && (int) event.getX() < scrw / 8 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                if (start == false && count == 0) {
                    start = true;
                    DirButton = "Left";
                }
                DirButton2 = "Left";
            } else if ((int) event.getX() > scrw / 8 && event.getX() < scrw / 4 && (int) event.getY() < scrh - scrh / 4 && (int) event.getY() > scrh - scrh / 2) {
                if (start == false && count == 0) {
                    start = true;
                    DirButton = "Up";
                }
                DirButton2 = "Up";
            } else if ((int) event.getX() > scrw / 8 && (int) event.getX() < scrw / 4 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                if (start == false && count == 0) {
                    start = true;
                    DirButton = "Down";
                }
                DirButton2 = "Down";
            }
            else if ((int) event.getX()>scrw/2) {
                for(int i=0; i<10; i++) {
                    if(missileNum[i] == 0) {
                        mx[i] = scrw/2 + (scrw/8 - scrw/16)/4 + xd;
                        my[i] = scrh/2 + (scrh/2 - scrh/4) + yd;
                        md[i] = MD;
                        missileNum[i] = 1;
                        if(missileCount!=0) missileCount -= 1;
                        break;
                    }
                }
            }

            else {
                start = false;
            }
        }


        if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_POINTER_UP) {
            if ((int) event.getX() > scrw / 4 && (int) event.getX() < scrw * 3 / 8 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                start = false;
            } else if ((int) event.getX() > 0 && (int) event.getX() < scrw / 8 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                start = false;
            } else if ((int) event.getX() > scrw / 8 && event.getX() < scrw / 4 && (int) event.getY() < scrh - scrh / 4 && (int) event.getY() > scrh - scrh / 2) {
                start = false;
            } else if ((int) event.getX() > scrw / 8 && (int) event.getX() < scrw / 4 && (int) event.getY() < scrh && (int) event.getY() > scrh - scrh / 4) {
                start = false;
            }
        }
        return true;
    }


    class GameThread extends Thread {
        public boolean run = true;

        @Override
        public void run() {
            while (run) {
                try {
                    postInvalidate(); //뷰에서 이미지를 분리시킨다.
                    if (count==8) {
                        count=0;
                        DirButton=DirButton2;
                    }
                    for (int i=0; i<3; i++) {
                        if(count2[i]==8) {
                            count2[i]=0;
                        }
                        if(count2[i]==0) {
                            int r = random.nextInt(4-1+1)+1;
                            if (r == 1) {
                                RectDirButton[i] = "Left";
                            }
                            if (r == 2) {
                                RectDirButton[i] = "Right";
                            }
                            if (r == 3) {
                                RectDirButton[i] = "Up";
                            }
                            if (r == 4) {
                                RectDirButton[i] = "Down";
                            }
                        }
                        if (life[i] > 0 && RectDirButton[i] == "Down") {
                            if(scrh/2 + ryd[i] < scrh - scrh/4 - (scrh%32)/2) {
                                ryd[i] += scrh/32;
                            }
                        }
                        if (life[i] > 0 && RectDirButton[i] == "Up") {
                            if(scrh/2 + ryd[i] > (scrh%32)/2) {
                                ryd[i] -= scrh/32;
                            }
                        }
                        if (life[i] > 0 && RectDirButton[i] == "Left") {
                            if(scrw/2 + rxd[i] > (scrw%64)/2) {
                                rxd[i] -= scrw/64;
                            }
                        }
                        if (life[i] > 0 && RectDirButton[i] == "Right") {
                            if(scrw/2 + ryd[i] < scrw-scrw/8-(scrw%64)/2) {
                                rxd[i] += scrw/64;
                            }
                        }
                    }

                    for(int i=0; i<1; i++) {
                        if(chr_life[i]>0 && start==true&&DirButton=="Down"&&count!=8 || start==false&&count>0&&count<8&&DirButton=="Down") {
                            if(count%4==0) {
                                yd+=scrh/80;
                                n=9;
                                MD=4;
                            }
                            else if(count%4==1 | count%4==3) {
                                yd+=scrh/80;
                                n=10;
                            }
                            else if(count%4==2) {
                                yd+=scrh/80;
                                n=11;
                            }
                        }

                        if(chr_life[i]>0 && start==true&&DirButton=="Up"&&count!=8 || start==false&&count>0&&count<8&&DirButton=="Up") {
                            if(count%4==0) {
                                yd-=scrh/80;
                                n=0;
                                MD=3;
                            }
                            else if(count%4==1 | count%4==3) {
                                yd-=scrh/80;
                                n=1;
                            }
                            else if(count%4==2) {
                                yd-=scrh/80;
                                n=2;
                            }
                        }

                        if(chr_life[i]>0 && start==true&&DirButton=="Right"&&count!=8 || start==false&&count>0&&count<8&&DirButton=="Right") {
                            if(count%4==0) {
                                xd+=scrw/160;
                                n=6;
                                MD=2;
                            }
                            else if(count%4==1 | count%4==3) {
                                xd+=scrw/160;
                                n=7;
                            }
                            else if(count%4==2) {
                                xd+=scrw/160;
                                n=8;
                            }
                        }

                        if(chr_life[i]>0 && start==true&&DirButton=="Left"&&count!=8 || start==false&&count>0&&count<8&&DirButton=="Left") {
                            if(count%4==0) {
                                xd-=scrw/160;
                                n=3;
                                MD=1;
                            }
                            else if(count%4==1 | count%4==3) {
                                xd-=scrw/160;
                                n=4;
                            }
                            else if(count%4==2) {
                                xd-=scrw/80;
                                n=5;
                            }
                        }
                    }

                    for(int i=0; i<1; i++)
                    {
                        if(chr_life[i]>0) {
                            if (start==true && count==0) {
                                count+=1;
                            } else {
                                if (count>0 && count<8) count+=1;
                            }
                        }
                    }
                    for(int i=0; i<3; i++)
                    {
                        if(life[i] > 0) {
                            count2[i] += 1;
                        }
                    }
                    sleep(40);
                } catch (Exception e) {

                }
            }

        }
    }
}







