package com.example.attendencemanager20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

class MainAdapter extends BaseAdapter {
    List<Integer> p;
    List<Integer> a;//=new ArrayList<Integer>();
    Context applicationcontext;
    List<String> names;
    LayoutInflater inflater;
    FirebaseDatabase database=FirebaseDatabase.getInstance();

    public MainAdapter(Context c,List<String> name, List<Integer> p,List<Integer> a){
        p=new ArrayList<>();
        a=new ArrayList<>();
        this.names=name;
        int v=names.size();
        this.applicationcontext=c;
        this.p=p;
        this.a=a;
        if(v != 0){
            for(int i=0;i<names.size();i++){
                System.out.println(names.get(i));
            }
            String m=names.get(names.size());
            System.out.println("gddfvj" + m);
        }
        inflater=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        p.add(0);
        a.add(0);
        return names.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view=inflater.inflate(R.layout.views,null);}
        TextView sname=view.findViewById(R.id.subject);
        Button attend=view.findViewById(R.id.attend);
        Button bunk=view.findViewById(R.id.bunk);
        sname.setText(names.get(i));
            System.out.println("mainadapter "+names.get(i));
            final int d=i;
            final TextView pre=view.findViewById(R.id.pre);
            final TextView missed=view.findViewById(R.id.missed);
            final TextView percent=view.findViewById(R.id.percent);
//            pre.setText(p.get(d));
//            missed.setText(a.get(d));
//            int c=(p.get(d)*100/(p.get(d)+a.get(d)));
//            percent.setText(c);
            attend.setOnClickListener(new View.OnClickListener() {
                int c;
                @Override
                public void onClick(View view) {
                    p.set(d,p.get(d)+1);
                    System.out.println(p.get(d));
                    System.out.println(a.get(d));
                    c=(p.get(d)*100/(p.get(d)+a.get(d)));
                    pre.setText("Present: "+ p.get(d));
                    percent.setText(c+"%");
                    DatabaseReference ref=database.getReference().child("Subjects").child(names.get(d)).child("Present");
                    ref.setValue(p.get(d));
                }
            });
            bunk.setOnClickListener(new View.OnClickListener() {
                int c;
                @Override
                public void onClick(View view) {
                    a.set(d,a.get(d)+1);
                    missed.setText("Absent: " + a.get(d));
                    c=(p.get(d)*100/(p.get(d)+a.get(d)));
                    System.out.println(p.get(d));
                    System.out.println(a.get(d));
                    percent.setText(c+"%");
                    DatabaseReference ref=database.getReference().child("Subjects").child(names.get(d)).child("Missed");
                    ref.setValue(a.get(d));
                }
            });
        return view;
    }
}
