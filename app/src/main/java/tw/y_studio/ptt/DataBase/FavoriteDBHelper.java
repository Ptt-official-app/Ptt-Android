package tw.y_studio.ptt.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FavoriteDBHelper extends SQLiteOpenHelper {

    public static final String _TableName="Favorite";
    public static final String DATABASE_CREATE_TABLE_1="CREATE TABLE IF NOT EXISTS " + _TableName +"("
            + "_ID INTEGER PRIMARY KEY,"
            + "_Board TEXT,"
            + "_Ttle TEXT,"
            + "_Category TEXT,"
            + "_Index INTEGER,"
            + "_other1 TEXT,"
            + "_other2 TEXT,"
            + "_other3 TEXT"
            + ");";
    public static final String[] colum = { "_ID","_Board","_Ttle","_Category","_Index"};
    public FavoriteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(DATABASE_CREATE_TABLE_1);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        //db.execSQL("DROP TABLE IF EXISTS config");	//刪除舊有的資料表
        //onCreate(db);
    }

    public List<Map<String,Object>> getAll() throws Exception{
        List<Map<String,Object>> output = new ArrayList<>();

        SQLiteDatabase db =getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.query(this._TableName,colum, null,null,null,null,"_Index ASC",null );
        if (cursor != null) {
            cursor.moveToFirst();	//將指標移到第一筆資料
            for(int i = 0 ;i<cursor.getCount();i++){
                Map<String,Object> mm=new HashMap<>();
                mm.put("title",cursor.getString(1));
                mm.put("number",cursor.getInt(4));
                mm.put("subtitle",cursor.getString(2));
                mm.put("class",cursor.getString(3));
                mm.put("online",0+"");
                mm.put("onlineColor", 7+"");
                output.add(mm);
                cursor.moveToNext();
            }

            cursor.close();
        }
        db.endTransaction();
        db.close();


        return output;
    }
    public Set<String> getAllSet() throws Exception{
        Set<String> output = new HashSet<>();

        SQLiteDatabase db =getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.query(this._TableName,colum, null,null,null,null,"_Index ASC",null );
        if (cursor != null) {
            cursor.moveToFirst();	//將指標移到第一筆資料
            for(int i = 0 ;i<cursor.getCount();i++){
                //Map<String,Object> mm=new HashMap<>();
                //mm.put("title",cursor.getString(1));
                //mm.put("number",cursor.getInt(4));
                //mm.put("subtitle",cursor.getString(2));
                //mm.put("class",cursor.getString(3));
                //mm.put("online",0+"");
                //mm.put("onlineColor", 7+"");
                output.add(cursor.getString(1));
                cursor.moveToNext();
            }

            cursor.close();
        }
        db.endTransaction();
        db.close();


        return output;
    }
    public void insertBoard(String board,String title,String Category,int index) throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_Board", board);
        values.put("_Ttle", title);
        values.put("_Category", Category);
        values.put("_Index",index);

        //values.put("_other1","");
        //values.put("_other2","");
        //values.put("_other3","");
        db.insert(_TableName,null, values);
        db.close();
    }
    public static ContentValues newContentValues(String board,String title,String Category,int index){
        ContentValues values = new ContentValues();
        values.put("_Board", board);
        values.put("_Ttle", title);
        values.put("_Category", Category);
        values.put("_Index",index);
        return values;
    }
    public void insertBoards(List<ContentValues> items) throws Exception{
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();


        for(ContentValues values:items){
            db.insert(_TableName,null, values);
        }

        //values.put("_other1","");
        //values.put("_other2","");
        //values.put("_other3","");
        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
    }
    public int getMaxIndex(){
        int index=0;
        SQLiteDatabase db =getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = db.query(this._TableName,colum, null,null,null,null,"_Index DESC","1" );
        if (cursor != null) {
            cursor.moveToFirst();	//將指標移到第一筆資料
            for(int i = 0 ;i<cursor.getCount();i++){

                index=cursor.getInt(4);

                cursor.moveToNext();
            }

            cursor.close();
        }
        db.endTransaction();
        db.close();
        return index;
    }
    public void delebyBoard(String board) throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        db.delete(_TableName,"_Board = '"+board+"'", null);
        db.close();
    }

    public void deleAll() throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        db.delete(_TableName,null, null);
        db.close();
    }

}
