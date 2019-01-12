package com.example.orgibly.words;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

//Singleton.
public class MyQuizStrategy implements QuizStrategy{
    private static MyQuizStrategy singleton = null;
    private MyQuizStrategy(){}

    static MyQuizStrategy getInstance(){
        if(singleton==null){
            singleton = new MyQuizStrategy();
        }
        return singleton;
    }

    /**
     * @param db Database manager.
     * @param maxQuizSize The size of the quiz that we want,
     * depending on number of words in database.
     * @return Words array for quiz.
     */
    public ArrayList<Word> getWordsForQuiz(DataBaseManager db, int maxQuizSize){
        ArrayList<Word> wordsFromDb = db.getAllWords(true);
        int quizSize = wordsFromDb.size() < maxQuizSize? wordsFromDb.size():maxQuizSize;

        int wrongAnswersSum = db.getWrongAnswersSum();
        ArrayList<Word> wordsWithRepetitions = makeWordsWithRepetitions(wordsFromDb, wrongAnswersSum);
        ArrayList<Word> wordsForQuiz = makeWordsForQuiz(wordsWithRepetitions,quizSize);
        Collections.shuffle(wordsForQuiz);
        return wordsForQuiz;
        }

    //Make an array with number of references of each word,
    //correspondingly with its wrong answers value.
    //Each word should repeat once + its wrong answers value.
    //Example:
    //word1.getW_answers() -> 0
    //word2.getW_answers() -> 2
    //array: {word1, word2, word2, word2}
    private ArrayList<Word> makeWordsWithRepetitions(ArrayList<Word> wordsFromDb, int wrongAnswersSum){
        int arrLen = wordsFromDb.size()+wrongAnswersSum;
        ArrayList<Word> wordsWithRepetitions = new ArrayList<>(arrLen);
        int i=0;
        for (Word word:wordsFromDb){
            int wrongAnswers = word.getW_answers();
            for(int j=i; j<=i+wrongAnswers; j++){
                wordsWithRepetitions.add(word);
            }
            i++;
        }
        return wordsWithRepetitions;
    }

    //Make an array of words for quiz, taken randomly from wordsWithRepetitions.
    //Each word appears once in the returned array, with no repetitions.
    //Reason: a word that appears more times in the given array(wordsWithRepetitions)
    // will appear statistically more times in the returned array.
    private ArrayList<Word> makeWordsForQuiz(ArrayList<Word> wordsWithRepetitions, int quizSize) {
        ArrayList<Word> wordsForQuiz = new ArrayList<>(quizSize);
        Random random = new Random();
        for (int n=0; n<quizSize; n++) {
            int wordIndex;
            Word word;
            do {
                wordIndex = random.nextInt(wordsWithRepetitions.size());
                word = wordsWithRepetitions.get(wordIndex);
            } while (wordsForQuiz.contains(word));
            wordsForQuiz.add(word);
        }
        return wordsForQuiz;
    }
}
