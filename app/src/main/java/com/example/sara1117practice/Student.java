package com.example.sara1117practice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Student {
    public static class Score{
        private String scoreName;
        private int score;

        public Score(String scoreName, int score){
            this.scoreName = scoreName;

            this.score = score;
        }
        public String getName(){
            return this.scoreName;
        }
        public int getValue(){
            return this.score;
        }
        public JSONObject getJsonObject() throws JSONException { //把物件轉成jsonObject
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("name", scoreName);
            jsonObject.put("value", score);
            return jsonObject;
        }
        public static Score genBySJsonObject(JSONObject jsonObject) throws JSONException{
            String name = jsonObject.getString("name");
            int value = jsonObject.getInt("value");
            return new Score (name, value);
        }
    }

    private String name;
    private ArrayList<Score> scores;

    public Student(String name){
        this.name = name;
        scores = new ArrayList<>();
    }

    public String scoreToString() {
        String str = "";
        for (int i = 0; i < scores.size(); i++) {
            str += scores.get(i).scoreName + " " + scores.get(i).score + "\n";
        }
        return str;
    }


    public void addScore(String name , int value ){
        scores.add(new Score(name, value));
    }
        public void addScore(Score score){
            scores.add(score);
    }
    public Score getScore(int index){
        return scores.get(index);
    }
    public String getName(){
        return name;
    }
    public ArrayList <Score> getScores(){
        return scores;
    }

    public JSONObject getJsonObject() throws JSONException {
        JSONObject jsonObject= new JSONObject();
        jsonObject.put("name", name);
        JSONArray jsonScores = new JSONArray();
        for (Score score : scores){
            jsonScores.put(score.getJsonObject());
        }
        jsonObject.put("scores", jsonScores);
        return jsonObject;
    }
    public static Student genByJsonObject (JSONObject jsonObject) throws JSONException{
        String name = jsonObject.getString("name");
        Student student = new Student(name);
        JSONArray jsonArray = jsonObject.getJSONArray("scores");
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject scoreJsonOnj =  jsonArray.getJSONObject(i);
            Score score = Score.genBySJsonObject((scoreJsonOnj));
            student.addScore(score);
        }
        return student;
    }
}


