package com.example.learning;

public class database {
    int id;
    String topic;
    String answer;
    String notes;
    int audio;

    public database(){

    }

    public database(int id, String topic, String answer) {
        this.id=id;
        this.topic=topic;
        this.answer=answer;
        this.notes=notes;
        this.audio=audio;
    }
    public int getId(){
        return id;
    }
    public String getTopic(){
        return topic;
    }
    public String getAnswer(){ return answer; }
    public String getNotes(){
        return notes;
    }
    public int getAudio(){
        return audio;
    }
}
