package com.nouveau.puzzleattack;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    private MainActivityView mMainActivityView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mMainActivityView = (MainActivityView)findViewById(R.id.MainActivityView);
        // rend visible la vue
        mMainActivityView.setVisibility(View.VISIBLE);
    }
}
