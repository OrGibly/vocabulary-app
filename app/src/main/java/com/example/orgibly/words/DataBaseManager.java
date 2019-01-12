package com.example.orgibly.words;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataBaseManager extends SQLiteOpenHelper {

    //--------------------------------------- SQL --------------------------------------
    private static final String TABLE_NAME = "words";
    private static final String COL1_ID = "id";
    private static final String COL2_WORD = "word";
    private static final String COL3_IS_VISIBLE = "isVisible";
    private static final String COL4_TRANS = "translation";
    private static final String COL5_W_TRANS1 = "wrong_translation1";
    private static final String COL6_W_TRANS2 = "wrong_translation2";
    private static final String COL7_W_TRANS3 = "wrong_translation3";
    private static final String COL8_W_ANSWERS = "wrong_answers";
    private static final String COL9_IsInQuiz = "isInQuiz";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + COL1_ID + " integer PRIMARY KEY AUTOINCREMENT, "
            + COL2_WORD + " text, "
            + COL3_IS_VISIBLE + " boolean, "
            + COL4_TRANS + " text, "
            + COL5_W_TRANS1 + " text, "
            + COL6_W_TRANS2 + " text, "
            + COL7_W_TRANS3 + " text, "
            + COL8_W_ANSWERS + " int DEFAULT 0, "
            + COL9_IsInQuiz + " boolean);";

    //----------------------------------------- CONSTANTS --------------------------------------
    final static int MIN_W_ANSWERS_VALUE = 0;
    final static int MAX_W_ANSWERS_VALUE = 10;

    public static final String EMPTY_MSG = ""; //This msg is sent when everything went good.
    private final static String ID_NOT_FOUND_MSG = "No word with this ID in database.";

    //------------------------------------------------------------------------------------------

    public DataBaseManager(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public String insertWord(Word word) {
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL("INSERT INTO " + TABLE_NAME + "("
                    + COL2_WORD + ", "
                    + COL3_IS_VISIBLE + ", "
                    + COL4_TRANS + ", "
                    + COL5_W_TRANS1 + ", "
                    + COL6_W_TRANS2 + ", "
                    + COL7_W_TRANS3 + ", "
                    + COL9_IsInQuiz
                    + ") VALUES("
                    + "'" + word.getWord() + "', "
                    + "'" + (word.isVisible() ? 1 : 0) + "', "
                    + "'" + word.getTranslation() + "', "
                    + "'" + word.getW_translation1() + "', "
                    + "'" + word.getW_translation2() + "', "
                    + "'" + word.getW_translation3() + "', "
                    + "'" + (word.isInQuiz() ? 1 : 0) + "');"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    public String switchVisibility(int wordId) {
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL(
                    "UPDATE " + TABLE_NAME + " SET " + COL3_IS_VISIBLE + "="
                            + "CASE WHEN " + COL3_IS_VISIBLE + "=1 THEN 0 "
                            + "ELSE 1 END "
                            + "WHERE " + COL1_ID + "=" + wordId + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    public String setAllVisibility(boolean visibility) {
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " + COL3_IS_VISIBLE + " =" + (visibility ? 1 : 0));
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    public Word getWord(int id) {
        Word word = null;
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + " WHERE " + COL1_ID + "=" + id + ";", null);
        if (cursor.moveToNext()) {
            word = new Word(cursor.getString(1), cursor.getString(3),cursor.getInt(8) > 0,
                    cursor.getString(4), cursor.getString(5), cursor.getString(6));
            word.setId(cursor.getInt(0));
            word.setVisible(cursor.getInt(2) > 0);
            word.setW_answers(cursor.getInt(7));
        }
        cursor.close();
        return word;
    }

    public ArrayList<Word> getAllWords(boolean onlyInQuizWords) {
        String sqlCondition="";
        if(onlyInQuizWords)sqlCondition=" WHERE "+COL9_IsInQuiz+" = "+1;
        ArrayList<Word> words = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT * FROM " + TABLE_NAME + sqlCondition
                        +" ORDER BY " + COL1_ID + " DESC;", null);
        while (cursor.moveToNext()) {
            Word word = new Word(cursor.getString(1), cursor.getString(3), cursor.getInt(8) > 0,
                    cursor.getString(4), cursor.getString(5), cursor.getString(6));
            word.setId(cursor.getInt(0));
            word.setVisible(cursor.getInt(2) > 0);
            word.setW_answers(cursor.getInt(7));
            word.setInQuiz(cursor.getInt(8) > 0);
            words.add(word);
        }
        cursor.close();
        return words;
    }

    //Increase or decrease(if increaseBy argument is negative)
    //Word's wrongAnswers value in DB.
    //Limit the wrongAnswers value between MIN/MAX_W_ANSWERS_VALUE(constants).
    public String increaseWrongAnswers(int wordId, int increaseBy) {
        int wrongAnswers = getWord(wordId).getW_answers();

        //Limit the wrongAnswers value between MIN/MAX_W_ANSWERS_VALUE(constants)
        String wrongAnswersValueToSet;
        if (wrongAnswers + increaseBy < MIN_W_ANSWERS_VALUE) {
            wrongAnswersValueToSet = MIN_W_ANSWERS_VALUE + "";
        } else if (wrongAnswers + increaseBy > MAX_W_ANSWERS_VALUE) {
            wrongAnswersValueToSet = MAX_W_ANSWERS_VALUE + "";
        } else {
            wrongAnswersValueToSet = COL8_W_ANSWERS + " +(" + increaseBy + ")";
        }

        //SQLite.
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL("UPDATE " + TABLE_NAME + " SET " +
                    COL8_W_ANSWERS + " = " + wrongAnswersValueToSet
                    + " WHERE " + COL1_ID + "=" + wordId + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    public int getWrongAnswersSum() {
        int wrongAnswers = 100;
        Cursor cursor = getReadableDatabase().rawQuery(
                "SELECT SUM(" + COL8_W_ANSWERS + ") FROM " + TABLE_NAME + ";", null);
        if (cursor.moveToNext()) {
            wrongAnswers = cursor.getInt(0);
        }
        cursor.close();
        return wrongAnswers;
    }

    public String updateWord(Word word){
        if(getWord(word.getId())==null)return ID_NOT_FOUND_MSG;
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL("UPDATE " + TABLE_NAME +
                    " SET " +
                    COL2_WORD + " = '" + word.getWord() +"', "+
                    COL4_TRANS + " = '" + word.getTranslation() +"', "+
                    COL5_W_TRANS1 + " = '" + word.getW_translation1() +"', "+
                    COL6_W_TRANS2 + " = '" + word.getW_translation2() +"', "+
                    COL7_W_TRANS3 + " = '" + word.getW_translation3() +"', "+
                    COL9_IsInQuiz + " = '" + (word.isInQuiz() ? 1 : 0) +"'"+
                    " WHERE " + COL1_ID + " = " + word.getId() + ";");
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }

    public String deleteWord(Word word){
        if(getWord(word.getId())==null)return ID_NOT_FOUND_MSG;
        String msg = EMPTY_MSG;
        try {
            getWritableDatabase().execSQL("DELETE FROM "+TABLE_NAME+
                            " WHERE "+COL1_ID+"="+word.getId()+";"
            );
        } catch (SQLException e) {
            e.printStackTrace();
            msg = e.getMessage();
        }
        return msg;
    }
}

