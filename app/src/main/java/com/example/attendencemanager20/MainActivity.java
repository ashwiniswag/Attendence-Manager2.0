package com.example.attendencemanager20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
        add=findViewById(R.id.subject);
        subjects=findViewById(R.id.list);
        names=new ArrayList<>();
        p=new ArrayList<>();
        a=new ArrayList<>();
        adapter=new MainAdapter(this,names,p,a);
        subjects.setAdapter(adapter);
        disp();
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
                        System.out.println(sub);
                        adapter.notifyDataSetChanged();
                        DatabaseReference ref=database.getReference().child("Subjects").child(sub).child("Subject name");
                        ref.setValue(sub);
                    }
                });
                alterdialog.show();
            }
        });
    }

    public void disp(){
        DatabaseReference ref;

//        for(int i=0;i<names.size();i++){
            ref=FirebaseDatabase.getInstance().getReference().child("Subjects");//.child(names.get(i));
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Iterator<DataSnapshot> items=dataSnapshot.getChildren().iterator();
                    while (items.hasNext()) {
                        DataSnapshot item=items.next();
                        String sub = item.child("Subject name").getValue().toString();
                        String present = item.child("Present").getValue().toString();
                        String missed = item.child("Missed").getValue().toString();
                        if (sub != null){
                            names.add(sub);
                            p.add(Integer.parseInt(present));
                            a.add(Integer.parseInt(missed));
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
