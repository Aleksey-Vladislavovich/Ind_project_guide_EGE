package com.example.ver3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class MyDialogFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        названия предметов 2
        final String[] objNamesArray = {"Математика", "Информатика", "Русский язык", "Физика",
        "История", "Обществознание", "Химия", "Биология"};
        final boolean[] checkedItemsArray = {true, false, true, false, false, false, false, false};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите предметы для сдачи ЕГЭ")
                .setMultiChoiceItems(objNamesArray, checkedItemsArray,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which, boolean isChecked) {
                                checkedItemsArray[which] = isChecked;
                            }
                        })
                .setPositiveButton("Готово",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                StringBuilder state = new StringBuilder();
                                for (int i = 0; i < objNamesArray.length; i++) {
                                    if (checkedItemsArray[i]) {
                                        state.append(objNamesArray[i]);
                                        state.append(" выбран\n");
                                    }
                                }
                                ((Base) getActivity()).okClicked(checkedItemsArray);

                                Toast.makeText(getActivity(),
                                        state.toString(), Toast.LENGTH_LONG)
                                        .show();
                            }
                        });

        return builder.create();
    }
}
