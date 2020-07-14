package br.com.sidney.petshow.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import br.com.sidney.petshow.R;

/**
 * @author Sidney Toshidi Oku
 */
public class UIUtils {

    private static boolean email;
    private static boolean sms;

    public static void setEmail(boolean email) {
        UIUtils.email = email;
    }

    public static boolean getEmail() { return email; }

    public static boolean getSms() {
        return sms;
    }

    public static void setSms(boolean sms) {
        UIUtils.sms = sms;
    }

    public static AlertDialog createDialog(Context context, int title, int message,
                               int positiveLabel, DialogInterface.OnClickListener positiveListener,
                               int negativeLabel, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        TextView txtTitle = new TextView(context);
        txtTitle.setText(title);
        txtTitle.setBackgroundColor(Color.parseColor("#ffa92f"));
        txtTitle.setPadding(10, 10, 10, 10);
        txtTitle.setGravity(Gravity.CENTER);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setTextSize(20);

        builder.setCustomTitle(txtTitle);

        builder.setMessage(message);
        builder.setPositiveButton(positiveLabel, positiveListener);

        if (negativeLabel > -1 && negativeListener != null) {
            builder.setNegativeButton(negativeLabel, negativeListener);
        }

        return builder.create();
    }

    public static android.app.AlertDialog createDialogSuporte(final Context context, int title, int positiveLabel, DialogInterface.OnClickListener positiveListener,
                                                              int negativeLabel, DialogInterface.OnClickListener negativeListener, final CharSequence[] opcaoPedido) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);

        TextView txtTitle = new TextView(context);
        txtTitle.setText(title);
        txtTitle.setBackgroundColor(Color.parseColor("#ffa92f"));
        txtTitle.setPadding(10, 10, 10, 10);
        txtTitle.setGravity(Gravity.CENTER);
        txtTitle.setTextColor(Color.WHITE);
        txtTitle.setTextSize(20);

        builder.setCustomTitle(txtTitle);

        setEmail(false);
        setSms(false);
        builder.setSingleChoiceItems(opcaoPedido, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opcaoPedido[which].equals("E-mail")) {
                    setEmail(true);
                    setSms(false);
                } else if (opcaoPedido[which].equals("Mensagem (SMS)")) {
                    setEmail(false);
                    setSms(true);
                }
            }
        });

        builder.setPositiveButton(positiveLabel, positiveListener);

        if (negativeLabel > -1 && negativeListener != null) {
            builder.setNegativeButton(negativeLabel, negativeListener);
        }

        return builder.create();
    }
    
    public static AlertDialog createDialog(Context context, int title, int message) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };

        return createDialog(context, title, message, R.string.txt_fechar, listener, -1, null);
    }

}
