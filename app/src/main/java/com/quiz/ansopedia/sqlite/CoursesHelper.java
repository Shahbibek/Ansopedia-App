package com.quiz.ansopedia.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.quiz.ansopedia.models.Chapters;
import com.quiz.ansopedia.models.Questions;
import com.quiz.ansopedia.models.Subjects;

import java.util.ArrayList;
import java.util.List;

public class CoursesHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "courses";
    public static final String TABLE_NAME = "courses";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DATA = "data";

    public CoursesHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT,"
                + DATA + " TEXT)";
        sqLiteDatabase.execSQL(query1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addNewData(String courseName, String data) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME, courseName);
        values.put(DATA, data);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    public ArrayList<Subjects> readData(Context context) {
        // on below line we are creating a
        // database for reading our database.
        CoursesHelper dbHelper = new CoursesHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Subjects> SubjectArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorCourses != null) {
            if (cursorCourses.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    Subjects subjects = new Subjects();
                    String data = cursorCourses.getString(2);
                    subjects = new Gson().fromJson(data, Subjects.class);
                    SubjectArrayList.add(subjects);
//                    ProductArrayList.add(new Chapters(cursorCourses.getInt(0),
//                            cursorCourses.getString(1),
//                            cursorCourses.getString(2),
//                            cursorCourses.getInt(4),
//                            cursorCourses.getInt(3)));
                } while (cursorCourses.moveToNext());
                // moving our cursor to next.
            }
            cursorCourses.close();
        }else {
            return null;
        }
        // at last closing our cursor
        // and returning our array list.

        return SubjectArrayList;
    }

    public void deleteData(String name) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "name=?", new String[]{name});
        db.close();
    }
}
