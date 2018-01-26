package edu.monash.fit2081.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

public class EditShape extends Fragment {
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
    private int selectedColor;

    private String idStr;
    private String xValueStr;
    private String yValueStr;
    private String borderValueStr;
    private String radiusValueStr;
    private String widthValueStr;
    private String heightValueStr;
    private String colorStr;
    private String typeStr;

    public EditShape() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_edit_shape, container, false);
        v.findViewById(R.id.update_radio_circle).setOnClickListener(clickListener);
        v.findViewById(R.id.update_radio_rectangle).setOnClickListener(clickListener);

        FloatingActionButton editShapeBtn = (FloatingActionButton) v.findViewById(R.id.update_shape_btn);

        Bundle myBundle = this.getArguments();

        idStr = myBundle.getString("id");
        widthValueStr = myBundle.getString("width");
        heightValueStr = myBundle.getString("height");
        xValueStr = myBundle.getString("x");
        yValueStr = myBundle.getString("y");
        borderValueStr = myBundle.getString("boarderWidth");
        radiusValueStr = myBundle.getString("radius");
        radiusValueStr = myBundle.getString("radius");
        colorStr = myBundle.getString("color");
        selectedColor = Integer.parseInt(colorStr);
        typeStr = myBundle.getString("type");

        if (typeStr.equals("Circle")) {
            ((RadioButton) v.findViewById(R.id.update_radio_circle)).setChecked(true);

        } else {
            ((RadioButton) v.findViewById(R.id.update_radio_rectangle)).setChecked(true);
        }


        width_label = ((TextView) v.findViewById(R.id.update_width_label));
        width_value = ((EditText) v.findViewById(R.id.update_width_value));
        width_value.setText(widthValueStr);

        height_label = ((TextView) v.findViewById(R.id.update_height_label));
        height_value = ((EditText) v.findViewById(R.id.update_height_value));
        height_value.setText(heightValueStr);

        radius_label = ((TextView) v.findViewById(R.id.update_radius_label));
        radius_value = ((EditText) v.findViewById(R.id.update_radius_value));
        radius_value.setText(radiusValueStr);

        xValue = (EditText) v.findViewById(R.id.update_x_value);
        xValue.setText(xValueStr);

        yValue = (EditText) v.findViewById(R.id.update_y_value);
        yValue.setText(yValueStr);

        borderValue = (EditText) v.findViewById(R.id.update_border_value);
        borderValue.setText(borderValueStr);

        colorBtn = (FloatingActionButton) v.findViewById(R.id.update_selected_color);
        //colorBtn.setBackgroundColor(Integer.parseInt(colorStr));
        colorBtn.setBackgroundTintList(ColorStateList.valueOf(selectedColor));


        colorBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showSelectColor(v);
            }
        });
        if (((RadioButton) v.findViewById(R.id.update_radio_circle)).isChecked())
            showHide(R.id.update_radio_circle);
        else
            showHide(R.id.update_radio_rectangle);

        editShapeBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateShape(v);
            }
        });


        // Return the layout for this fragment
        return v;
    }

    public void updateShape(View view) {
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
        contentValues.put(SchemeShapes.Shape.SHAPE_TYPE, typeStr);
        contentValues.put(SchemeShapes.Shape.SHAPE_X, xValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_Y, yValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_RADIUS, radiusValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_WIDTH, widthValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_HEIGHT, heightValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS, borderValueInt);
        contentValues.put(SchemeShapes.Shape.SHAPE_COLOR, selectedColor);

        resolver.update(SchemeShapes.Shape.CONTENT_URI, contentValues, "_id=" + idStr, null);

    }


    public void showHide(int id) {
        switch (id) {
            case R.id.update_radio_circle:
                width_label.setVisibility(View.INVISIBLE);
                width_value.setVisibility(View.INVISIBLE);

                height_label.setVisibility(View.INVISIBLE);
                height_value.setVisibility(View.INVISIBLE);


                radius_label.setVisibility(View.VISIBLE);
                radius_value.setVisibility(View.VISIBLE);


                break;
            case R.id.update_radio_rectangle:
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
            // Is the button checked?
            boolean checked = ((RadioButton) view).isChecked();

            // Check which radio button was clicked
            switch (view.getId()) {
                case R.id.update_radio_circle:
                    if (checked)
                        typeStr = "Circle";
                    showHide(R.id.update_radio_circle);
                    break;
                case R.id.update_radio_rectangle:
                    if (checked)
                        typeStr = "Rectangle";
                    showHide(R.id.update_radio_rectangle);

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
                            colorBtn.setBackgroundTintList(ColorStateList.valueOf(selectedColor));
                        } else {
                            Toast.makeText(getContext(), "Dialog cancelled", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).build().show(getFragmentManager(), "dialog_demo_1");
    }
}



