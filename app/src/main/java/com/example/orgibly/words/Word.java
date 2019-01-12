package com.example.orgibly.words;

//4000 words ~ 1mb in memory (probably less).
public class Word{
    private int id=-1;
    private String word;
    private boolean isVisible = true;
    private String translation;
    private String w_translation1;
    private String w_translation2;
    private String w_translation3;
    private int w_answers;
    private boolean isInQuiz;

    public Word(String word, String translation, boolean isInQuiz,
                String w_translation1, String w_translation2, String w_translation3)
    {
        this.word = word;
        this.translation = translation;
        this.w_translation1 = w_translation1;
        this.w_translation2 = w_translation2;
        this.w_translation3 = w_translation3;
        this.isInQuiz = isInQuiz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if(this.id==-1){
            this.id = id;
        }
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public void switchVisibility(){
        if(isVisible)isVisible=false;
        else isVisible=true;
    }


    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getW_translation1() {
        return w_translation1;
    }

    public void setW_translation1(String w_translation1) {
        this.w_translation1 = w_translation1;
    }

    public String getW_translation2() {
        return w_translation2;
    }

    public void setW_translation2(String w_translation2) {
        this.w_translation2 = w_translation2;
    }

    public String getW_translation3() {
        return w_translation3;
    }

    public void setW_translation3(String w_translation3) {
        this.w_translation3 = w_translation3;
    }

    public int getW_answers() {
        return w_answers;
    }

    public void setW_answers(int w_answers) {
        this.w_answers = w_answers;
    }

    public void setInQuiz(boolean inQuiz){this.isInQuiz = inQuiz;}

    public boolean isInQuiz(){return isInQuiz;}
}
