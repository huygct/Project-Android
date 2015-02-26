package com.gct;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class Mouse_Activity extends Activity implements View.OnTouchListener {

    private Client mClient;

    private ImageView mImageView;
    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;
    Data data = new Data();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new connectTask().execute();
        // action mouse
        mRrootLayout = (ViewGroup) findViewById(R.id.root);
        mImageView = (ImageView) mRrootLayout.findViewById(R.id.imageView);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(350, 150);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);



//        Button shutdown_btn = (Button) findViewById(R.id.button_shutdown);
//
//        new connectTask().execute();
//        shutdown_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Data data = new Data("xxx", "Shutdown");
//                if(mClient != null) {
//                    mClient.sendMessage(data);
//                }
//            }
//        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;

                data.setId(String.valueOf(X));
                data.setMessage(String.valueOf(Y));
                mClient.sendMessage(data);

                view.setLayoutParams(layoutParams);
                break;
        }
        mRrootLayout.invalidate();
        return true;
    }

    public class connectTask extends AsyncTask<Data, Data, Client> {

        @Override
        protected Client doInBackground(Data... data) {
            // we create a Client object and
            mClient = new Client(new Client.OnMessageReceived() {
                @Override
                public void messageReceived(Data data) {
                    // this method calls the onProgressUpdate
                    publishProgress(data); // đây chính là hàm để gọi phương thức onProgressUpdate
                }
            });
            mClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(Data... values) {
            super.onProgressUpdate(values);

            Log.e("TCP", "Nhan dc");
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home_Activity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
