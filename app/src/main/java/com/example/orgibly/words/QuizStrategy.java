package com.example.orgibly.words;

import java.util.ArrayList;

/**
 * Created by OrGibly on 2.6.2018.
 */

public interface QuizStrategy {
    /**
     * @param db Database manager.
     * @param maxQuizSize The size of the quiz that we want,
     * depending on number of words in database.
     * @return Words array for a quiz.
     */
    ArrayList<Word> getWordsForQuiz(DataBaseManager db, int maxQuizSize);
}
