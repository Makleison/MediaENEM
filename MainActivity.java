package com.enemmedia.app;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Button;
import java.util.Locale;

public class MainActivity extends Activity {
    private final String[] names = {
        "Linguagens", "Ciências Humanas", "Ciências da Natureza", "Matemática", "Redação"
    };
    private final int[] scores = {456, 500, 500, 500, 500};
    private TextView averageView, totalView;
    private TextView[] scoreViews = new TextView[5];
    private SeekBar[] bars = new SeekBar[5];
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + 0.5f); }

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(245,247,251));
        getWindow().setNavigationBarColor(Color.rgb(245,247,251));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(24), dp(20), dp(24));
        scroll.addView(root);

        TextView title = text("ENEM Média", 30, Color.rgb(20,30,50), true);
        root.addView(title);
        TextView subtitle = text("Simule suas notas arrastando os controles.", 14, Color.GRAY, false);
        LinearLayout.LayoutParams subLp = new LinearLayout.LayoutParams(-1, -2);
        subLp.bottomMargin = dp(22);
        root.addView(subtitle, subLp);

        for (int i = 0; i < names.length; i++) {
            final int index = i;
            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(dp(14), dp(12), dp(14), dp(10));
            card.setBackgroundColor(Color.WHITE);
            LinearLayout.LayoutParams cp = new LinearLayout.LayoutParams(-1, -2);
            cp.bottomMargin = dp(12);
            root.addView(card, cp);

            LinearLayout line = new LinearLayout(this);
            line.setGravity(Gravity.CENTER_VERTICAL);
            TextView label = text(names[i], 16, Color.rgb(35,45,65), true);
            line.addView(label, new LinearLayout.LayoutParams(0, -2, 1));
            scoreViews[i] = text(String.valueOf(scores[i]), 18, Color.rgb(37,99,235), true);
            line.addView(scoreViews[i]);
            card.addView(line);

            bars[i] = new SeekBar(this);
            bars[i].setMax(1000);
            bars[i].setProgress(scores[i]);
            card.addView(bars[i]);
            bars[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    scores[index] = progress;
                    scoreViews[index].setText(String.valueOf(progress));
                    updateAverage();
                }
                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        LinearLayout result = new LinearLayout(this);
        result.setOrientation(LinearLayout.VERTICAL);
        result.setGravity(Gravity.CENTER);
        result.setPadding(dp(18), dp(22), dp(18), dp(22));
        result.setBackgroundColor(Color.rgb(226,239,255));
        LinearLayout.LayoutParams rp = new LinearLayout.LayoutParams(-1, -2);
        rp.topMargin = dp(8);
        root.addView(result, rp);

        TextView caption = text("MÉDIA GERAL", 13, Color.rgb(70,90,120), true);
        result.addView(caption);
        averageView = text("", 38, Color.rgb(22,101,52), true);
        result.addView(averageView);
        totalView = text("", 14, Color.rgb(70,90,120), false);
        result.addView(totalView);

        Button reset = new Button(this);
        reset.setText("ZERAR NOTAS");
        LinearLayout.LayoutParams bp = new LinearLayout.LayoutParams(-1, -2);
        bp.topMargin = dp(14);
        root.addView(reset, bp);
        reset.setOnClickListener(v -> {
            for (int i = 0; i < 5; i++) bars[i].setProgress(0);
        });

        updateAverage();
        setContentView(scroll);
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private void updateAverage() {
        int total = 0;
        for (int score : scores) total += score;
        double avg = total / 5.0;
        if (averageView != null) averageView.setText(String.format(Locale.forLanguageTag("pt-BR"), "%.2f", avg));
        if (totalView != null) totalView.setText("Total: " + total + " pontos  •  5 áreas");
    }
}
