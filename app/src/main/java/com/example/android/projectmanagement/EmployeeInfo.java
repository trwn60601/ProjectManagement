package com.example.android.projectmanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.projectmanagement.database.EmployeeSQL;

public class EmployeeInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar=findViewById(R.id.InfoEmployeeToolbar);
        setActionBar(toolbar);
        setContentView(R.layout.activity_employee_info);
        Bundle bundle=getIntent().getExtras();
        if (bundle ==null){
            return;
        }
        EmployeeSQL employeeSQL= (EmployeeSQL) bundle.get("data");
        TextView textView;
        textView =findViewById(R.id.nameEI);
        textView.setText(employeeSQL.name);
        textView=findViewById(R.id.phoneEI);
        textView.setText(employeeSQL.phone);
        textView=findViewById(R.id.emailEI);
        textView.setText(employeeSQL.email);
        textView=findViewById(R.id.jobEI);
        textView.setText(employeeSQL.job);
        textView=findViewById(R.id.addressEI);
        textView.setText(employeeSQL.address);
        textView=findViewById(R.id.salaryEI);
        textView.setText(employeeSQL.salary);
        textView=findViewById(R.id.joinedContentEI);
        textView.setText("");
        textView=findViewById(R.id.joiningContentEI);
        textView.setText("");

        ImageView imageView=findViewById(R.id.BackEmployee);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}