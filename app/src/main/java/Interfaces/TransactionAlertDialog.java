package Interfaces;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.nonequi.R;

public class TransactionAlertDialog extends DialogFragment {

//    SendMoney sendMoney = new SendMoney();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_transaction).setPositiveButton(R.string.confirm_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((SendMoney) getActivity()).makeTransaction();
            }
        }).setNegativeButton(R.string.cancel_dialog, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*sendMoney.phoneNumber.setText("");
                sendMoney.moneyToSend.setText("");*/

                dialogInterface.cancel();
            }
        });

        return builder.create();
    }
}
