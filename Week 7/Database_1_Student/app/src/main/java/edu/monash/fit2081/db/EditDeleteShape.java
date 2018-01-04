package edu.monash.fit2081.db;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import edu.monash.fit2081.db.provider.ShapesDbHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditDeleteShape extends Fragment {
    ShapesDbHelper dbHelper;
    LayoutInflater myInflater = null;

    public EditDeleteShape() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        dbHelper = new ShapesDbHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myInflater = inflater;
        View v = inflater.inflate(R.layout.fragment_edit_delete_shape, container, false);

        ListView listView = (ListView) v.findViewById(R.id.list_edit_delete_shapes);
        listView.addHeaderView(myInflater.inflate(R.layout.edit_delete_shape_list_header, null));
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "Delete Shape", Toast.LENGTH_LONG).show();
//            }
//        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadListShapes();
    }

    public void LoadListShapes() {
        Cursor cursor = dbHelper.getAllShapes();
        ListView lvItems = (ListView) getActivity().findViewById(R.id.list_edit_delete_shapes);

        ItemCursorAdapter itemAdaptor = new ItemCursorAdapter(getContext(), cursor, getActivity().getSupportFragmentManager());
        lvItems.setAdapter(itemAdaptor);
    }
}
