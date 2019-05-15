package com.example.moneycontrol;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;



public class TestFragment extends Fragment {
    //private Button button;
    private Resources resources;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.test_fragment, container, false);
        View view = inflater.inflate(R.layout.test_fragment, container, false);
        Button mainButton = (Button) view.findViewById(R.id.btn);
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"uh", Toast.LENGTH_LONG).show();
            }
        });
        return  view;


    }
}
