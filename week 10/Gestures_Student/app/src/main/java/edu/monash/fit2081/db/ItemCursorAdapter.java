package edu.monash.fit2081.db;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import edu.monash.fit2081.db.provider.SchemeShapes;


public class    ItemCursorAdapter extends CursorAdapter {

    private Context myContext;
    private FragmentManager myFragmentManager;

    public ItemCursorAdapter(Context context, Cursor cursor, FragmentManager fMgr) {
        super(context, cursor, 0);
        myContext = context;
        myFragmentManager = fMgr;
    }


    //"Get the data item associated with the specified position in the data set."
    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    //"Makes a new view to hold the data pointed to by cursor."
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.item_delete_edit, viewGroup, false);
    }

    //Bind an existing view to the data pointed to by cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView shapeId = (TextView) view.findViewById(R.id.delete_id);

        TextView shapeType = (TextView) view.findViewById(R.id.shapeType);
        TextView shapeX = (TextView) view.findViewById(R.id.shapeX);
        TextView shapeY = (TextView) view.findViewById(R.id.shapeY);

        // Extract properties from cursor (we only need first 4 for ListView but need the rest in case they ask for an edit operation)
        final String type = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_TYPE));
        final String x = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_X));
        final String y = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_Y));
        final String id = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.ID));
        final String radius = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_RADIUS));
        final String width = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_WIDTH));
        final String height = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_HEIGHT));
        final String borderWidth = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_BORDER_THICKNESS));
        final String color = cursor.getString(cursor.getColumnIndexOrThrow(SchemeShapes.Shape.SHAPE_COLOR));

        // Populate fields with extracted properties
        shapeType.setText(String.valueOf(type));
        shapeX.setText(x);
        shapeY.setText(y);
        shapeId.setText(id);

        Button removeBtn = (Button) view.findViewById(R.id.btnRemove);
        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteShape(id);
            }
        });

        final Button updateBtn = (Button) view.findViewById(R.id.btnEdit);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("type", type);
                bundle.putString("x", x);
                bundle.putString("y", y);
                bundle.putString("radius", radius);
                bundle.putString("width", width);
                bundle.putString("height", height);
                bundle.putString("boarderWidth", borderWidth);
                bundle.putString("color", color);
                Fragment editShape = new EditShape();

                //pack all the data for an edit operation then go to the EditShape fragment
                editShape.setArguments(bundle); //pack all the data for an edit operation
                myFragmentManager.beginTransaction().replace(R.id.fragment_bottom, editShape, "editShape").addToBackStack("editShape").commit();
            }
        });
    }


    public void deleteShape(String id) {
        //now update currently visible fragments
        //bottom fragment - the Edit/Delete ListView

        ContentResolver resolver = myContext.getApplicationContext().getContentResolver();
        resolver.delete(SchemeShapes.Shape.CONTENT_URI, SchemeShapes.Shape.ID + " = ? ", new String[]{id});
    }

}
