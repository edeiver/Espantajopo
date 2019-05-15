package com.example.moneycontrol;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tapadoo.alerter.Alerter;


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
                Alerter.create(getActivity()).setTitle("Test").setText("Este es in test").setBackgroundColorRes(R.color.blue_d).setIcon(R.drawable.ic_person).show();
                Toast.makeText(getActivity(),"uh", Toast.LENGTH_LONG).show();
            }
        });
        return  view;


    }
}
