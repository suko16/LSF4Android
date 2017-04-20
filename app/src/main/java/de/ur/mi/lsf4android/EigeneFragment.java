package de.ur.mi.lsf4android;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EigeneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EigeneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EigeneFragment extends android.support.v4.app.Fragment {

    public EigeneFragment() {
        // Required empty public constructor
    }

    public static EigeneFragment newInstance() {
        EigeneFragment fragment = new EigeneFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eigene, container, false);
    }
}
