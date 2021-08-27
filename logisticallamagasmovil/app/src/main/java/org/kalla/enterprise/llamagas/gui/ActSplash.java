package org.kalla.enterprise.llamagas.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.kalla.enterprise.llamagas.R;

public class ActSplash extends Activity   {

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_splash);
        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });
    }
}