package com.example.learning;

public class database {
    int id;
    String key;
    String topic;
    String answer;
    String notes;
    String audio;

    public database(){

    }

    public database(int id, String key, String topic, String answer, String audio) {
        this.id=id;
        this.key=key;
        this.topic=topic;
        this.answer=answer;
        this.notes=notes;
        this.audio=audio;
    }
    public int getId(){
        return id;
    }
    public String getKey(){
        return key;
    }
    public String getTopic(){
        return topic;
    }
    public String getAnswer(){ return answer; }
    public String getNotes(){
        return notes;
    }
    public String getAudio(){
        return audio;
    }
}
