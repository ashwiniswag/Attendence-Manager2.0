package com.example.attendencemanager20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add;
    GridView subjects;
    List<String> names;
    List<Integer> p,a;
    MainAdapter adapter;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        add=findViewById(R.id.subject);
        subjects=findViewById(R.id.list);
        names=new ArrayList<>();
        p=new ArrayList<>();
        a=new ArrayList<>();
        adapter=new MainAdapter(this,names,p,a);
        subjects.setAdapter(adapter);
        disp();
        final FirebaseAuth mAuth=FirebaseAuth.getInstance();
        final FirebaseUser currentuser=mAuth.getCurrentUser();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alterdialog= new AlertDialog.Builder(MainActivity.this);
                alterdialog.setTitle("Enter Subject Name");
                alterdialog.setMessage("Enter Subject Name");

                final EditText input = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alterdialog.setView(input);

                alterdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alterdialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sub=input.getText().toString();
                        names.add(sub);
                        p.add(0);
                        a.add(0);
                        System.out.println(sub);
                        adapter.notifyDataSetChanged();
                        String userid=currentuser.getUid();
                        DatabaseReference ref=database.getReference().child("Users").child(userid).child("Subjects").child(sub).child("Subject name");
                        ref.setValue(sub);
                        ref=database.getReference().child("Users").child(userid).child("Subjects").child(sub).child("Present");
                        ref.setValue("0");
                        ref=database.getReference().child("Users").child(userid).child("Subjects").child(sub).child("Missed");
                        ref.setValue("0");
                    }
                });
                alterdialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){

            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                finish();
                Toast.makeText(MainActivity.this,"Signed Out Successfully",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void disp(){
        DatabaseReference ref;
        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
        final String userid=currentuser.getUid();
//        for(int i=0;i<names.size();i++){
            ref=FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("Subjects");//.child(names.get(i));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                    while (items.hasNext()) {
                        DataSnapshot item=items.next();
                        String sub = item.child("Subject name").getValue().toString();
                        String present="0";
                        present = item.child("Present").getValue().toString();
                        String missed = item.child("Missed").getValue().toString();
                        if (sub != null){
                            names.add(sub);
                            int x=Integer.parseInt(present);
                            int y=Integer.parseInt(missed);
                            System.out.println("Pre=" +x +" Ab=" + y);
                            p.add(x);
                            a.add(y);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this,"Not Changed",Toast.LENGTH_SHORT).show();
                }
            });
//        }
    }
}
