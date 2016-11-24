package edu.ucsc.giggle.gig;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by JanJan on 10/10/2016.
 */
public class AboutTabFragment extends Fragment {
    static final String TAG = "AboutTabFragment";
    RecyclerView recyclerView;
    View rootView;
    EditText editText;
    User mUser;
    DatabaseReference aboutRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_view);
        setupRecyclerView(recyclerView);

        mUser = new User(getArguments());

        return rootView;
    }

    public void onViewCreated( View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Create a new Adapter
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);
        // Connect to the Firebase database
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Get a reference to the todoItems child items it the database
        final DatabaseReference aboutRef = database.getReference("about").child(mUser.username);

        // Assign a listener to detect changes to the child items
        // of the database reference.
        aboutRef.addChildEventListener(new ChildEventListener(){

            // This function is called once for each child that exists
            // when the listener is added. Then it is called
            // each time a new child is added.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                String value = dataSnapshot.getValue(String.class);
                //adapter.add(value);
                editText.setText("");
                editText.setText(value);
            }

            // This function is called each time a child item is removed.
            public void onChildRemoved(DataSnapshot dataSnapshot){
                String value = dataSnapshot.getValue(String.class);
                adapter.remove(value);
                //Log.d(TAG, "****************************************************************delete");
            }

            // The following functions are also required in ChildEventListener implementations.
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName){}
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName){}

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG:", "Failed to read value.", error.toException());
            }
        });

        editText = (EditText) rootView.findViewById(R.id.edit_about_text);

        OnFocusChangeListener ofcListener = new AboutTabFragment.MyFocusChangeListener();
        editText.setOnFocusChangeListener(ofcListener);

        final Button saveButton = (Button)rootView.findViewById(R.id.save_button);
        final Button editButton = (Button)rootView.findViewById(R.id.edit_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Create a new child with a auto-generated ID.
                aboutRef.getRef().removeValue();
                DatabaseReference childRef = aboutRef.push();

                // Set the child's data to the value passed in from the text box.
                childRef.setValue(editText.getText().toString());

                String result = editText.getText().toString();
                editText.setEnabled(false);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editText.setEnabled(true);
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>{
        private String[] mValues;
        private Context mContext;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView mTextView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(android.R.id.text1);
            }

        }

        public String getValueAt(int position) {
            return mValues[position];
        }

        public SimpleStringRecyclerViewAdapter(Context context, String[] items) {
            mContext = context;
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.mTextView.setText(mValues[position]);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, getValueAt(position), Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.length;
        }
    }
    private class MyFocusChangeListener implements OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.edit_about_text && !hasFocus) {
                //sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
                InputMethodManager imm =  (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                // Create a new child with a auto-generated ID.
                // Set the child's data to the value passed in from the text box.
                //childRef.setValue(editText.getText().toString());
                //String result = editText.getText().toString();
                //editText.setText( result, TextView.BufferType.EDITABLE);


            }
        }
    }
}
