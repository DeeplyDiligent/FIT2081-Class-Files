package com.ebookfrenzy.javalayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.widget.EditText;
import android.content.res.Resources;
import android.util.TypedValue;

public class JavaLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(...) later after we have built the UI in code

        //create a Button and set its properties
        Button myButton = new Button(this);
        myButton.setText("Press Me");
        myButton.setBackgroundColor(Color.YELLOW);
        myButton.setId(R.id.myButtonId);

        //create an EditText and set its properties
        EditText myEditText = new EditText(this);
        myEditText.setId(R.id.myEditTextId);
        myEditText.setTextColor(Color.YELLOW);

        //oh boy!
        //this pattern determines how many pixels are required to make 200dp wide given the device's screen dpi
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
        myEditText.setWidth(px);

        //create a RelativeLayout and set its properties
        RelativeLayout myLayout = new RelativeLayout(this);
        myLayout.setBackgroundColor(Color.BLUE);

        //now create a Relative.LayoutParams for the Button to tell its parent Relative Layout how it wants to be sized positioned and behave
        RelativeLayout.LayoutParams buttonParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        //now create a Relative.LayoutParams for the EditText to tell its parent Relative Layout how it wants to be sized positioned and behave
        RelativeLayout.LayoutParams textParams =
                new RelativeLayout.LayoutParams(                    //this constructor takes width and height
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        //add Layout Parameter rules to an existing LayoutParam
        textParams.addRule(RelativeLayout.ABOVE, myButton.getId());
        textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textParams.setMargins(0, 0, 0, 80);

        //add Layout Parameter rules to an existing LayoutParam
        buttonParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL);

        //now lets add the Button and EditText to the RelativeLayout
        myLayout.addView(myButton, buttonParams);
        myLayout.addView(myEditText, textParams);

        //finally inflate (show the RelativeLayout as this Activity's UI)
        setContentView(myLayout);

    }
}
