package com.mybp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by kerick on 6/12/17.
 */

public class Span {
    public Span() {

    }

    public static void span(String text, String subText, Switch switchView) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, text.length(), 0);
        spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), 0);

        SpannableString spanSubText = new SpannableString(subText);
        spanSubText.setSpan(new RelativeSizeSpan(0.75f), 0, subText.length(), 0);
        spanSubText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, subText.length(), 0);
        switchView.setText("");
        switchView.append(spanText);
        switchView.append("\n");
        switchView.append(spanSubText);
    }

    public static void span(String text, String subText, TextView textView) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, text.length(), 0);
        spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), 0);

        SpannableString spanSubText = new SpannableString(subText);
        spanSubText.setSpan(new RelativeSizeSpan(0.75f), 0, subText.length(), 0);
        spanSubText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, subText.length(), 0);
        textView.setText("");
        textView.append(spanText);
        textView.append("\n");
        textView.append(spanSubText);
    }

    public static void span(String text, Switch switchView) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, text.length(), 0);
        spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), 0);

        switchView.setText("");
        switchView.append(spanText);
    }

    public static void span(String text, TextView textView) {
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(Color.GRAY), 0, text.length(), 0);
        spanText.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), 0);

        textView.setText("");
        textView.append(spanText);
    }
}
