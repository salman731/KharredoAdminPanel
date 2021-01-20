package com.muqit.KharredoAdminPanel.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.muqit.KharredoAdminPanel.R;


public class PageFragment extends Fragment {

    TextInputEditText Title,Description,Tags;
    MaterialButton Submit_BTN;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_page, container, false);
        Title = inflate.findViewById(R.id.Page_title);
        Description = inflate.findViewById(R.id.Page_meta_description);
        Tags = inflate.findViewById(R.id.Page_tags);
        Submit_BTN = inflate.findViewById(R.id.Page_Submit_BTN);
        Submit_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Save Successfully......",Toast.LENGTH_LONG).show();
            }
        });
        return inflate;
    }
}