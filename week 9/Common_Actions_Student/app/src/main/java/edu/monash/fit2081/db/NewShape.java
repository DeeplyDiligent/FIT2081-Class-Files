package edu.monash.fit2081.db;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.thebluealliance.spectrum.SpectrumDialog;

import edu.monash.fit2081.db.provider.SchemeShapes;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewShape extends Fragment {


    public NewShape() {
        // Required empty public constructor
    }

    private TextView width_label;
    private EditText width_value;

    private TextView height_label;
    private EditText height_value;


    private TextView radius_label;
    private EditText radius_value;

    private EditText xValue;
    private EditText yValue;
    private EditText borderValue;

    private FloatingActionButton colorBtn;

    private String shapeType = "";
    private int selectedColor = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_new_shape, container, false);

        v.findViewById(R.id.radio_circle).setOnClickListener(clickListener);
        v.findViewById(R.id.radio_rectangle).setOnClickListener(clickListener);

        FloatingActionButton addNewShapeBtn = (FloatingActionButton) v.findViewById(R.id.add_new_shape_btn);
        width_label = ((TextView) v.findViewById(R.id.width_label));
        width_value = ((EditText) v.findViewById(R.id.width_value));

        height_label = ((TextView) v.findViewById(R.id.height_label));
        height_value = ((EditText) v.findViewById(R.id.height_value));


        radius_label = ((TextView) v.findViewById(R.id.radius_label));
        radius_value = ((EditText) v.findViewById(R.id.radius_value));

        xValue = (EditText) v.findViewById(R.id.x_value);
        yValue = (EditText) v.findViewById(R.id.y_value);
        borderValue = (EditText) v.findViewById(R.id.border_value);
        colorBtn = (FloatingActionButton) v.findViewById(R.id.selected_color);
        if (selectedColor == -1) {
            selectedColor = ContextCompat.getColor(getContext(), R.color.md_blue_500);
            // add to Shared Preferences to be used later by the viewshapes
            getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit().putInt("selectColor", selectedColor).apply();

        }
        colorBtn.setBackgroundTintList(ColorStateList.valueOf(selectedColor));

        colorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSelectColor(v);
            }
        });
        addNewShapeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newShape(v);
            }
        });

        if (shapeType.equals("")) {
            ((RadioButton) v.findViewById(R.id.radio_circle)).setChecked(true);
            shapeType = "Circle";
        }
        if (((RadioButton) v.findViewById(R.id.radio_circle)).isChecked())
            showHide(R.id.radio_circle);
        else
            showHide(R.id.radio_rectangle);

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (shapeType.equals("")) {
            ((RadioButton) getActivity().findViewById(R.id.radio_circle)).setChecked(true);
            shapeType = "Circle";
        }
        if (((RadioButton) getActivity().findViewById(R.id.radio_circle)).isChecked())
            showHide(R.id.radio_circle);
        else
            showHide(R.id.radio_rectangle);
    }

    public void newShape(View view) {

        ContentResolver resolver = getActivity().getApplicationContext().getContentResolver();


        int xValueInt = Integer.parseInt(xValue.getText().toString());
        int yValueInt = Integer.parseInt(yValue.getText().toString());
        int borderValueInt = Integer.parseInt(borderValue.getText().toString());

        int radiusValueInt = 0;
        if (!TextUtils.isEmpty(radius_value.getText().toString()))
            radiusValueInt = Integer.parseInt(radius_value.getText().toString());

        int widthValueInt = 0;
        if (!TextUtils.isEmpty(width_value.getText().toString()))
            widthValueInt = Integer.parseInt(width_value.getText().toString());

        int heightValueInt = 0;
        if (!TextUtils.isEmpty(height_value.getText().toString()))
            heightValueInt = Integer.parseInt(height_value.getText().toString());

        ContentValues contentValues = new ContentValues();
        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, shapeType);
        contentValues.put(SchemeShapes.Shape.SHAPE_X, xValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, yValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, radiusValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, widthValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, heightValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, borderValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, selectedColor);

        resolver.insert(SchemeShapes.Shape.CONTENT_URI, contentValues);
    }

    public void showHide(int id) {
        switch (id) {
            case R.id.radio_circle:
                width_label.setVisibility(View.INVISIBLE);
                width_value.setVisibility(View.INVISIBLE);

                height_label.setVisibility(View.INVISIBLE);
                height_value.setVisibility(View.INVISIBLE);


                radius_label.setVisibility(View.VISIBLE);
                radius_value.setVisibility(View.VISIBLE);

                break;
            case R.id.radio_rectangle:
                radius_label.setVisibility(View.INVISIBLE);
                radius_value.setVisibility(View.INVISIBLE);
                width_label.setVisibility(View.VISIBLE);
                width_value.setVisibility(View.VISIBLE);

                height_label.setVisibility(View.VISIBLE);
                height_value.setVisibility(View.VISIBLE);

                break;
        }
    }


    View.OnClickListener clickListener = new View.OnClickListener() {
        public void onClick(View view) {
            // Is the button now checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.radio_circle:
                    if (checked)
                        shapeType = "Circle";
                    showHide(R.id.radio_circle);
                    break;
                case R.id.radio_rectangle:
                    if (checked)
                        shapeType = "Rectangle";
                    showHide(R.id.radio_rectangle);

                    break;
            }
            // do something here
        }
    };

    public void showSelectColor(View v) {
        new SpectrumDialog.Builder(getContext())
                .setColors(R.array.demo_colors)
                .setSelectedColor(selectedColor)
                .setDismissOnColorSelected(true)
                .setOutlineWidth(2)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            selectedColor = color;
                            //colorBtn.setBackgroundColor(selectedColor);
                            colorBtn.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                        } else {
                            Toast.makeText(getContext(), "Dialog cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).build().show(getFragmentManager(), "dialog_demo_1");
    }


}

