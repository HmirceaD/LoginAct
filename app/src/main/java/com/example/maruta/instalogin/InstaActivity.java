package com.example.maruta.instalogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class InstaActivity extends AppCompatActivity {

    private ListView userList;
    private List<String> list1;
    private ArrayAdapter<String> listAdp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        list1 = new ArrayList<String>();

        listAdp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);

        userList = findViewById(R.id.userList);

        userList.setAdapter(listAdp);

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                for(ParseUser o:objects){

                    list1.add(o.getUsername());

                }

                userList.setAdapter(listAdp);
            }
        });


    }
}
