package com.example.android.projectmanagement.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.projectmanagement.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME= "project_db";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(EmployeeSQL.CREATE_TABLE);
        sqLiteDatabase.execSQL(UserSQL.CREATE_TABLE);
        insertUser(UserSQL.id,sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+EmployeeSQL.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+UserSQL.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public long insertEmployee(String name,String phone,String email,String job,String address,String salary,byte[] img){
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EmployeeSQL.COLUMN_NAME,name);
        values.put(EmployeeSQL.COLUMN_PHONE,phone);
        values.put(EmployeeSQL.COLUMN_EMAIL,email);
        values.put(EmployeeSQL.COLUMN_JOB,job);
        values.put(EmployeeSQL.COLUMN_ADDRESS,address);
        values.put(EmployeeSQL.COLUMN_SALARY,salary);
        values.put(EmployeeSQL.COLUMN_IMG,img);

        long id = sqLiteDatabase.insert(EmployeeSQL.TABLE_NAME,null,values);
        sqLiteDatabase.close();
        return id;

    }
    public long insertUser(Long id, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(UserSQL.COLUMN_ID, id);
        values.put(UserSQL.COLUMN_NAME, "");
        values.put(UserSQL.COLUMN_PHONE, "");
        values.put(UserSQL.COLUMN_EMAIL, "");
        values.put(UserSQL.COLUMN_ADDRESS, "");
        values.put(UserSQL.COLUMN_IMG, "");
        long newRowId = db.insert(UserSQL.TABLE_NAME, null, values);
        return id;
    }

    public EmployeeSQL getEmpoloyee(long id){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(EmployeeSQL.TABLE_NAME,
                new String[]{EmployeeSQL.COLUMN_ID,EmployeeSQL.COLUMN_NAME,EmployeeSQL.COLUMN_PHONE, EmployeeSQL.COLUMN_EMAIL,
                EmployeeSQL.COLUMN_JOB,EmployeeSQL.COLUMN_ADDRESS,EmployeeSQL.COLUMN_SALARY,EmployeeSQL.COLUMN_IMG},EmployeeSQL.COLUMN_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if (cursor!=null){
            cursor.moveToFirst();
        }

        EmployeeSQL employeeSQL= new EmployeeSQL(cursor.getLong(cursor.getColumnIndex(EmployeeSQL.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_PHONE)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_EMAIL)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_JOB)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_SALARY)),
                cursor.getBlob(cursor.getColumnIndex(EmployeeSQL.COLUMN_IMG)));
        cursor.close();
        return employeeSQL;
    }

    public UserSQL getUser(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                UserSQL.COLUMN_ID,
                UserSQL.COLUMN_NAME,
                UserSQL.COLUMN_PHONE,
                UserSQL.COLUMN_EMAIL,
                UserSQL.COLUMN_ADDRESS,
                UserSQL.COLUMN_IMG
        };
        String selection = UserSQL.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(UserSQL.id)};
        Cursor cursor = db.query(
                UserSQL.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null
        );

        List<UserSQL> list_user = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_ID));
            String itemName = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_NAME));
            String itemPhone = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_PHONE));
            String itemEmail = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_EMAIL));
            String itemAddress = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_ADDRESS));
            byte[] itemImage = cursor.getBlob(
                    cursor.getColumnIndexOrThrow(UserSQL.COLUMN_IMG));
            list_user.add(new UserSQL(itemId, itemName, itemPhone, itemEmail, itemAddress, itemImage));
        }
        cursor.close();
        return list_user.get(0);
    }

    public List<EmployeeSQL> getAlLEmployee(){
        List<EmployeeSQL> employeeSQLList=new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ EmployeeSQL.TABLE_NAME+ " ORDER BY "+EmployeeSQL.COLUMN_ID +" ASC;";
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()){
            do{
                EmployeeSQL employeeSQL= new EmployeeSQL(cursor.getLong(cursor.getColumnIndex(EmployeeSQL.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_JOB)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(EmployeeSQL.COLUMN_SALARY)),
                        cursor.getBlob(cursor.getColumnIndex(EmployeeSQL.COLUMN_IMG)));
                employeeSQLList.add(employeeSQL);
            }while (cursor.moveToNext());
        }
        sqLiteDatabase.close();
        return employeeSQLList;
    }


    public int updateEmployee(EmployeeSQL employeeSQL){
        ContentValues values=new ContentValues();
        values.put(EmployeeSQL.COLUMN_NAME,employeeSQL.name);
        values.put(EmployeeSQL.COLUMN_PHONE,employeeSQL.phone);
        values.put(EmployeeSQL.COLUMN_EMAIL,employeeSQL.email);
        values.put(EmployeeSQL.COLUMN_ADDRESS,employeeSQL.address);
        values.put(EmployeeSQL.COLUMN_JOB,employeeSQL.job);
        values.put(EmployeeSQL.COLUMN_SALARY,employeeSQL.salary);
        values.put(EmployeeSQL.COLUMN_IMG,employeeSQL.img);

        String selection=EmployeeSQL.COLUMN_ID +" LIKE ?";
        String[] SelectionArgs= {String.valueOf(employeeSQL.id)};

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int count =sqLiteDatabase.update(EmployeeSQL.TABLE_NAME,values,selection,SelectionArgs);
        sqLiteDatabase.close();
        return count;


    }
    public int updateUser(UserSQL userSQL){
        ContentValues values=new ContentValues();
        values.put(UserSQL.COLUMN_NAME,userSQL.name);
        values.put(UserSQL.COLUMN_PHONE,userSQL.phone);
        values.put(UserSQL.COLUMN_EMAIL,userSQL.email);
        values.put(UserSQL.COLUMN_ADDRESS,userSQL.address);
        values.put(UserSQL.COLUMN_IMG,userSQL.img);

        String selection=UserSQL.COLUMN_ID +" LIKE ?";
        String[] SelectionArgs= {String.valueOf(userSQL.id)};

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        int count =sqLiteDatabase.update(UserSQL.TABLE_NAME,values,selection,SelectionArgs);
        sqLiteDatabase.close();
        return count;


    }

    public int deleteEmployee(long id){
        String selection = EmployeeSQL.COLUMN_ID +" LIKE ?";
        String[] SelectionArgs = {String.valueOf(id)};
        SQLiteDatabase sqLiteDatabase= this.getWritableDatabase();
        int count = sqLiteDatabase.delete(EmployeeSQL.TABLE_NAME,selection,SelectionArgs);
        sqLiteDatabase.close();
        return count;

    }
}
