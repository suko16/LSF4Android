package de.ur.mi.lsf4android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    public static final String TITEL_EXTRA = "titel_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String titel = intent.getStringExtra(TITEL_EXTRA);

        TextView tvTitel = (TextView) findViewById(R.id.tv_titel);
        tvTitel.setText(titel);
    }
}
