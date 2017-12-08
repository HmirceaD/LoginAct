package com.example.maruta.instalogin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class UserfeedAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfeed);

        LinearLayout background = findViewById(R.id.linBack);

        Intent it = getIntent();

        /*User that was selected from the list*/
        ParseQuery<ParseObject> imgQuery = new ParseQuery<ParseObject>("Image");

        imgQuery.whereEqualTo("username", it.getStringExtra("username"));

        imgQuery.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e == null){

                    if(objects.size() > 0){

                        for(ParseObject o:objects){

                            /*Create An image for every image in the database*/

                            ImageView img = new ImageView(UserfeedAct.this);

                            img.setLayoutParams(new android.view.ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));

                            ParseFile pFl = (ParseFile) o.get("image");

                            pFl.getDataInBackground(new GetDataCallback() {

                                /*Set the image background to that of the image from the database*/
                                @Override
                                public void done(byte[] data, ParseException e) {

                                    if(e == null && data != null){

                                        Bitmap bit = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        img.setImageBitmap(bit);
                                    }

                                }
                            });

                            /*Add it to the layout*/
                            background.addView(img);
                        }

                    }else {

                        Toast.makeText(UserfeedAct.this, "Nothing to show", Toast.LENGTH_SHORT).show();
                    }

                }else {

                    Toast.makeText(UserfeedAct.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}
