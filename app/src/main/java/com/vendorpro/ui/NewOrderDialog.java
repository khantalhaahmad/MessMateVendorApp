package com.vendorpro.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.vendorpro.R;
import com.vendorpro.model.Order;
import com.vendorpro.model.OrderItem;

import java.util.List;
import java.util.Locale;

public class NewOrderDialog extends Dialog {

    private final Order order;
    private final Listener listener;

    private TextView tvTimer;
    private TextView tvOrderId;
    private TextView tvItems;
    private TextView tvTotal;

    private Button btnAccept;
    private Button btnReject;

    private CountDownTimer countDownTimer;

    private MediaPlayer mediaPlayer;

    public interface Listener {

        void onAccept(Order order);

        void onReject(Order order);
    }

    public NewOrderDialog(
            @NonNull Context context,
            Order order,
            Listener listener
    ) {
        super(context);

        this.order = order;
        this.listener = listener;

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCancelable(false);

        initUI(context);
    }

    private void initUI(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);

        setContentView(inflater.inflate(
                R.layout.dialog_new_order,
                null
        ));

        tvTimer = findViewById(R.id.tvTimer);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvItems = findViewById(R.id.tvItems);
        tvTotal = findViewById(R.id.tvTotal);

        btnAccept = findViewById(R.id.btnAccept);
        btnReject = findViewById(R.id.btnReject);

        setupOrderDetails();

        startTimer();

        playAlertSound();

        btnAccept.setOnClickListener(v -> {

            stopTimer();

            stopSound();

            listener.onAccept(order);

            dismiss();
        });

        btnReject.setOnClickListener(v -> {

            stopTimer();

            stopSound();

            listener.onReject(order);

            dismiss();
        });

        makeFullscreen();
    }

    private void setupOrderDetails() {

        if(order == null) return;

        String id = order.getId();

        if(id != null && id.length() > 8){
            id = id.substring(0,8);
        }

        tvOrderId.setText("Order #" + id);

        tvTotal.setText(
                String.format(
                        Locale.getDefault(),
                        "₹%.2f",
                        order.getTotalAmount()
                )
        );

        List<OrderItem> items = order.getItems();

        StringBuilder builder = new StringBuilder();

        if(items != null){

            for(OrderItem item : items){

                builder.append(item.getQuantity())
                        .append(" x ")
                        .append(item.getName())
                        .append("\n");

            }

        }

        tvItems.setText(builder.toString());
    }

    private void startTimer(){

        countDownTimer = new CountDownTimer(60000,1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);

                tvTimer.setText(seconds + "s");

                if(seconds < 10){

                    tvTimer.setTextColor(Color.RED);

                }

            }

            @Override
            public void onFinish() {

                stopSound();

                listener.onReject(order);

                dismiss();

            }
        };

        countDownTimer.start();
    }

    private void stopTimer(){

        if(countDownTimer != null){

            countDownTimer.cancel();

        }
    }

    private void playAlertSound(){

        try{

            mediaPlayer = MediaPlayer.create(
                    getContext(),
                    R.raw.new_order
            );

            mediaPlayer.setLooping(true);

            mediaPlayer.start();

        }catch(Exception e){

            e.printStackTrace();

        }
    }

    private void stopSound(){

        if(mediaPlayer != null){

            mediaPlayer.stop();

            mediaPlayer.release();

            mediaPlayer = null;

        }
    }

    private void makeFullscreen(){

        Window window = getWindow();

        if(window != null){

            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
            );

        }
    }

}