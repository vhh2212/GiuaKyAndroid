package com.vhh.vanhai.gkk;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseReference mData;
    private ListView listGia;
    private ArrayList<GiaRau> mArrayList;
    private EditText edtAnh, edtLoaiRau, edtGia;
    private Button btnThem;
    private ArrayAdapter adapter;
    private String userId;
    private GiaRau gr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listGia = (ListView) findViewById(R.id.listGia);
        edtAnh = (EditText) findViewById(R.id.editAnh);
        edtLoaiRau = (EditText) findViewById(R.id.editLRau);
        edtGia = (EditText) findViewById(R.id.editGia);
        btnThem = (Button) findViewById(R.id.buttonClick);

        mData = FirebaseDatabase.getInstance().getReference("Giá Rau-Củ-Quả");
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imgView = edtAnh.getText().toString();
                String loairau = edtLoaiRau.getText().toString();
                String gia = edtGia.getText().toString();
                if(TextUtils.isEmpty(imgView)){
                    Toast.makeText(MainActivity.this,"Chưa nhập vào link ảnh",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(loairau)){
                    Toast.makeText(MainActivity.this,"Chưa nhập vào loại Rau-Củ-Quả",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(gia)){
                    Toast.makeText(MainActivity.this,"Chưa nhập vào giá hàng",Toast.LENGTH_LONG).show();
                    return;
                }
                //Toast.makeText(MainActivity.this, loairau + ": " + gia + "/kg",Toast.LENGTH_SHORT).show();
                GiaRau giarau = new GiaRau(loairau,gia,imgView);
                mData.child("Mặt Hàng").child(giarau.LoaiRCQ).setValue(giarau);

            }
        });
        mArrayList = new ArrayList<>();

        mData.child("Mặt Hàng").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GiaRau grau = dataSnapshot.getValue(GiaRau.class);
                mArrayList.add(new GiaRau(grau.LoaiRCQ.toString(),grau.Gia.toString()+"/kg",grau.urlImage.toString()));
                setDataListView();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                GiaRau grau = dataSnapshot.getValue(GiaRau.class);
                mArrayList.add(new GiaRau(grau.LoaiRCQ.toString(),grau.Gia.toString()+"/kg",grau.urlImage.toString()));
                setDataListView();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listGia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this,"Đã Chọn",Toast.LENGTH_SHORT).show();
                GiaRau grau = mArrayList.get(i);
                edtAnh.setText(grau.urlImage.toString());
                edtLoaiRau.setText(grau.LoaiRCQ.toString());
                edtGia.setText(grau.Gia.toString());

            }
        });

        listGia.setOnItemLongClickListener(new ItemLongClickRemove());
    }
    private class ItemLongClickRemove implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setMessage("Bán có muốn xóa sản phẩm này!");
            alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    GiaRau grau = mArrayList.get(position);
                    mData.child("Mặt Hàng").child(grau.LoaiRCQ.toString()).removeValue();
                    mArrayList.remove(grau);
                    setDataListView();
                    Toast.makeText(MainActivity.this,"Đã Xóa",Toast.LENGTH_SHORT).show();
                }
            });
            alertDialogBuilder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //không làm gì
                }
            });
            alertDialogBuilder.show();
            return true;
        }
    }
    private void setDataListView(){
        MyArrayAdapter giarauadapter = new MyArrayAdapter(this, mArrayList);
        listGia.setAdapter(giarauadapter);
    }
}
