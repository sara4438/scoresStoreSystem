package com.example.sara1117practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sara1117practice.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String SP_STUDENT = "STUDENT";
//    private static final String = "PASSWORD";
    private ArrayList<Student> students;
    private EditText etName;
    private Button btnAddName;
    private TextView tvName;
    private EditText etScoreName;
    private EditText etScore;
    private Button btnAddScore;
    private boolean loadScore;
    private TextView tvScore;
    private TextView tvScore2;
    private TextView tvScore3;
    private Button btnNext;
    private Button btnPrevious;
    private Button btnLoad;
    private Button btnSave;
    private int current =-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setBtnAddStudent();
        setBtnAddScore();
        setBtnPrevious();
        setBtnNext();
        setBtnLoad();
        setBtnSave();
    }
    public void init(){
        students = new ArrayList<>();
        etName = findViewById(R.id.main_et_name);
        btnAddName = findViewById(R.id.main_btn_addName);
        tvName = findViewById(R.id.main_tv_name);
        etScoreName = findViewById(R.id.main_et_scorename);
        etScore = findViewById(R.id.main_et_score);
        btnAddScore = findViewById(R.id.main_btn_addScore);
        tvScore = findViewById(R.id.main_tv_score);
        btnPrevious = findViewById(R.id.main_btn_previous);
        btnNext = findViewById(R.id.main_btn_next);
        btnLoad = findViewById(R.id.main_btn_load);
        btnSave = findViewById(R.id.main_btn_save);
    }
    public void showName(){
        tvName.setText(etName.getText().toString());
    }
    private boolean nameIsEmty(){
        if(etName.getText().toString().equals("")){
            return true;
        }
        return false;
    }
    public void setBtnAddStudent(){
        btnAddName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameIsEmty()){
                    showToast(R.string.data_not_found);
                    return;
                }
                current++;
                showName();
                students.add(new Student(etName.getText().toString()));
                tvScore.setText("");
                etName.setText("");
//                loadScore = true;
            }
        });
    }

    public void setBtnAddScore(){
        btnAddScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scoreHasEmty()){
                    showToast(R.string.data_not_found);
                    return;
                }
                int score = Integer.parseInt(etScore.getText().toString());
                String scoreName = etScoreName.getText().toString();
                students.get(current).addScore(scoreName, score);
                tvScore.setText(tvScore.getText().toString()+ etScoreName.getText().toString()+" " + score +"\n");
                etScoreName.setText("");
                etScore.setText("");
                current = students.size()-1;
            }
        });
    }
    private boolean scoreHasEmty(){
        if(etScoreName.getText().toString().equals("")){
            return true;
        }
        if(etScore.getText().toString().equals("")){
            return true;
        }
        return false;
    }
    public void setBtnPrevious(){
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current == 0 || students.size() ==0){
                    return;
                }
                current--;
                tvName.setText(students.get(current).getName());
                tvScore.setText(students.get(current).scoreToString());
                }
        });
    }
    public void setBtnNext(){
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(current == students.size()-1 || students.size() ==0) {
                    return;
                }
                current++;
                tvName.setText(students.get(current).getName());
                tvScore.setText(students.get(current).scoreToString());
           }
        });
    }
    public void setBtnLoad(){
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loadSP()){
                    showToast(R.string.data_load_failed);
                }
            }
        });
    }
    public void setBtnSave(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    storeInfo();
                    showToast(R.string.store_successful);
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
    private boolean loadSP(){
        SharedPreferences sp = getSharedPreferences(Global.SP_DATA, MODE_PRIVATE);
        String studentsJsonArray = sp.getString(SP_STUDENT, "[]");
        try {
            JSONArray jsonArray = new JSONArray(studentsJsonArray);//用string放入jsonArray中
            genStudents(jsonArray); //從jsonArray取出所有學生
            if (students.size() == 0) {
                current=-1;
                return false;
            }
            current = 0;
            tvName.setText(students.get(current).getName());
            tvScore.setText(students.get(current).scoreToString());
            return true;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return false;
    }
    private void genStudents(JSONArray jsonArray) throws JSONException{
        students = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            Student student = Student.genByJsonObject(jsonArray.getJSONObject(i));
            students.add(student);
        }
    }
    private void storeInfo()throws JSONException {
        SharedPreferences sp = getSharedPreferences(Global.SP_DATA, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        JSONArray jsonArray = new JSONArray();
        for(Student student : students){
            jsonArray.put(student.getJsonObject()); //把student一一轉jsonObject後丟到jsonArray
        }
        editor.putString(SP_STUDENT, jsonArray.toString()).apply(); //把jsonArray轉成文字, 丟在SP的"Student"標籤裡
    }
    private void showToast(int msgResId){
        Toast.makeText(this, msgResId, Toast.LENGTH_LONG).show();
    }


}
