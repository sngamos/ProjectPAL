package com.example.a1dpal;

public class storyState {
    private String story;
    private String emotion;
    private String summary = "the story has just begun";
    storyState(String emotion, String summary){
        this.emotion = emotion;
        this.summary = summary;
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
    public String inputWrapper(String input) {
        /*String output = "Write a short continuation of a story about going on a date with Ganyu. The story so far: " + this.summary +
                "The user's action is: " + input + " output in json string format ONLY, with keys 'story': story, and 'ganyu_emotion': ganyu's emotion in one word, and 'summary': a short summary of the story so far\n";
        */
        String output = "Write Ganyu's response to my action in short. " + "the story so far: " + this.summary + "my action is: " +
                input + " output in json string format ONLY, with keys 'story':Ganyu's reply and 'ganyu_emotion': Ganyu's emotion in one word, and 'summary': summarise the story so far";
        return output;
    };
    // make constructer for emotion and summary
    // make setter and getter
    // make method that takes in user input and wraps it with this.summary

}
