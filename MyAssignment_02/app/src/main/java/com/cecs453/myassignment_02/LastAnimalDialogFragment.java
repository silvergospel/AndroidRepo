package com.cecs453.myassignment_02;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Michael on 10/14/2015.
 */
public class LastAnimalDialogFragment extends DialogFragment{
    DialogInputListener dialogInputListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.alert_dialog));
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogInputListener.onPositive(LastAnimalDialogFragment.this);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogInputListener.onNegative(LastAnimalDialogFragment.this);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            dialogInputListener = (DialogInputListener) activity;
        } catch (Exception e){
            Log.d("onAttach FAILED!!!!","WHY?!");
            return;
        }
    }

    public interface DialogInputListener{
        public void onPositive(DialogFragment dialogFragment);
        public void onNegative(DialogFragment dialogFragment);
    }
}
