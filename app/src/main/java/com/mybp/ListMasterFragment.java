package com.mybp;

import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListMasterFragment extends ListFragment {
    private OnPlansSelectedListener onPlansSelectedListener = null;

    public ListMasterFragment() {
        // Required empty public constructor
    }

    protected void setOnPlansSelectedListener(OnPlansSelectedListener listener) {
        onPlansSelectedListener = listener;
    }

    protected OnPlansSelectedListener getOnPlansSelectedListener() {
        return onPlansSelectedListener;
    }

    public interface OnPlansSelectedListener {
        public void onItemSelected(String planDescription);
    }
}
