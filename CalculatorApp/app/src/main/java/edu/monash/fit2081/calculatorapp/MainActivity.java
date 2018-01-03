package edu.monash.fit2081.calculatorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private double valueOne = Double.NaN;
    private double valueTwo;
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';
    private static final char NO_OPERATION = '?';

    private char CURRENT_ACTION;
    private DecimalFormat decimalFormat;
    public TextView interScreen;  // Intermediate result Screen
    private TextView resultScreen; // Result Screen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reference both TextViews
        interScreen = (TextView) findViewById(R.id.InterScreen);
        resultScreen = (TextView) findViewById(R.id.resultScreen);
        decimalFormat = new DecimalFormat("#.##########");
    }

    public void buttonClick(View v){
        interScreen.setText(interScreen.getText() + ((Button) v).getText().toString());
    }

    public void buttonSevenClick(View v) {
        interScreen.setText(interScreen.getText() + "7");
    }

    public void buttonOperationClick(View v) {
        computeCalculation();
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        } else {
            String buttonText = ((Button) v).getText().toString();
            if (buttonText.equals("/")){
                CURRENT_ACTION = DIVISION;
            } else if (buttonText.equals("+")){
                CURRENT_ACTION = ADDITION;
            } else if (buttonText.equals("*")){
                CURRENT_ACTION = MULTIPLICATION;
            } else if (buttonText.equals("-")){
                CURRENT_ACTION = SUBTRACTION;
            }
            resultScreen.setText(decimalFormat.format(valueOne) + " " + buttonText);
            interScreen.setText("");
        }
    }

    public void buttonEqualClick(View v) {

        /*
        * Call ComputeCalculation method
        * Update the result TextView by adding the '=' char and result of operation
        * Reset valueOne
        * Set CURRENT_ACTION to NO_OPERATION
        * */
        computeCalculation();
        resultScreen.setText(resultScreen.getText().toString()+ " " + decimalFormat.format(valueTwo) + " = " + valueOne);
        interScreen.setText("");
        valueOne = Double.NaN;
        valueTwo = Double.NaN;
        CURRENT_ACTION = NO_OPERATION;
    }

    public void buttonClearClick(View v) {
        /*
        * if the intermediate TextView has text then
        *       delete the last character
        * else
              * reset valueOne, valueTwo, the content of result TextView,
              * and the content of intermediate TextView
        * */
        String interscreenText = interScreen.getText().toString();
        if (!interscreenText.equals("")){
            interScreen.setText(interscreenText.substring(0, interscreenText.length()-1));
        }else{
            valueOne = Double.NaN;
            valueTwo = Double.NaN;
            resultScreen.setText("");
            interScreen.setText("");
        }
    }


    private void computeCalculation() {

        if (!Double.isNaN(valueOne)) {
            String valueTwoString = interScreen.getText().toString();
            if (!valueTwoString.equals("")) {
                valueTwo = Double.parseDouble(valueTwoString);
                interScreen.setText(null);
                if (CURRENT_ACTION == ADDITION)
                    valueOne = this.valueOne + valueTwo;
                else if (CURRENT_ACTION == SUBTRACTION)
                    valueOne = this.valueOne - valueTwo;
                else if (CURRENT_ACTION == MULTIPLICATION)
                    valueOne = this.valueOne * valueTwo;
                else if (CURRENT_ACTION == DIVISION)
                    valueOne = this.valueOne / valueTwo;
            }
        } else {
            try {
                valueOne = Double.parseDouble(interScreen.getText().toString());
            } catch (Exception e) {
            }

        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
