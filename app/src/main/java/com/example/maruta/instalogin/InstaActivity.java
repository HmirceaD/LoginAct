package com.example.maruta.instalogin;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InstaActivity extends AppCompatActivity {

    private ListView userList;
    private List<String> list1;
    private ArrayAdapter<String> listAdp;

    public void getPhoto(){

        Intent it = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(it, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            /*Permission Granted*/
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                getPhoto();

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInf = getMenuInflater();

        mInf.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*Upload An Image to the server*/
        if(item.getItemId() == R.id.share){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                }else {

                    getPhoto();
                }


            }else {

                getPhoto();
            }
        }

        /*Logout*/
        if(item.getItemId() == R.id.logout){

            ParseUser crrUser = ParseUser.getCurrentUser();

            crrUser.logOutInBackground(new LogOutCallback() {

                @Override
                public void done(ParseException e) {

                    if(e == null){

                        Toast.makeText(InstaActivity.this, "Logged out", Toast.LENGTH_SHORT).show();

                    }else {

                        Toast.makeText(InstaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            Intent it = new Intent(this, MainActivity.class);
            startActivity(it);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta);

        displayUsers();


    }

    private void displayUsers() {

        list1 = new ArrayList<String>();

        listAdp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list1);

        userList = findViewById(R.id.userList);

        userList.setAdapter(listAdp);

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                for(ParseUser o:objects){

                    if(!o.getUsername().equals(ParseUser.getCurrentUser().getUsername())){

                        list1.add(o.getUsername());
                    }
                }

                userList.setAdapter(listAdp);
            }
        });

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            /*select the user to see his feed*/
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent it = new Intent(InstaActivity.this, UserfeedAct.class);

                it.putExtra("username", list1.get(position));

                startActivity(it);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == 1 && resultCode == RESULT_OK && data != null){

            Uri selectedImage = data.getData();

            try {

                /*Use this to store the image in the database*/

                Bitmap bitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitMap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] bArr = stream.toByteArray();

                ParseFile fl = new ParseFile("image.png", bArr);

                ParseObject o = new ParseObject("Image");

                o.put("image", fl);

                o.put("username", ParseUser.getCurrentUser().getUsername());

                o.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {

                        if(e == null){

                            Toast.makeText(InstaActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(InstaActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            } catch (IOException e) {

                e.printStackTrace();
            }

        }

    }
}
