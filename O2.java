package com.example.Eric_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class O2 extends AppCompatDialogFragment {
    private EditText editText4;
    private EditText editText5;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_point,null);

        builder.setView(view)
                //  .setTitle("Add")
                .setNegativeButton("cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                    }
                })
                .setPositiveButton("Add",new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){

                    }
                });

        editText4 = view.findViewById(R.id.editText4);
        editText5 = view.findViewById(R.id.editText5);

        return builder.create();

    }

}
