package com.example.a1dpal;

import android.content.Context;
import android.content.SharedPreferences;

public class storyState {
    private String charChosen;
    private String story;
    private String emotion;
    private String summary = "the story has just begun";
    storyState(String Character,String emotion, String summary){
        this.emotion = emotion;
        this.summary = summary;
        this.charChosen = Character;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getStory() {
        return story;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setCharChosen(String Character){this.charChosen= Character;}

    public String  getCharChosen(){return this.charChosen;}

    public String inputWrapper(String input) {
        /*String output = "Write a short continuation of a story about going on a date with Ganyu. The story so far: " + this.summary +
                "The user's action is: " + input + " output in json string format ONLY, with keys 'story': story, and 'ganyu_emotion': ganyu's emotion in one word, and 'summary': a short summary of the story so far\n";
        */
        String output = String.format("Write %s's response to my action in short. ",this.charChosen) + "the story so far: " + this.summary + "my action is: " +
                input + " output in json string format ONLY, with keys" + String.format(" 'story':%s's reply",this.charChosen)+ String.format(" and 'character_emotion': %s's emotion in one word, and 'summary': summarise the story so far",this.charChosen);
        return output;
    };
    // make constructer for emotion and summary
    // make setter and getter
    // make method that takes in user input and wraps it with this.summary

}
