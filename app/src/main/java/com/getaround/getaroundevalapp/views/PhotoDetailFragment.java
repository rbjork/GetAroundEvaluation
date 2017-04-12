package com.getaround.getaroundevalapp.views;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.getaround.getaroundevalapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoDetailFragment.onFragmentDetailListener} interface
 * to handle interaction events.
 * Use the {@link PhotoDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static String IMAGE_URL = "param1";
    // TODO: Rename and change types of parameters
    private String img_url;
    private ImageView photoView;
    private TextView phototitle;
    private Button returnButton;
    private onFragmentDetailListener mListener;

    public PhotoDetailFragment() {
        // Required empty public constructor
    }

    public void replacePhoto(String url){

    }

    // TODO: Rename and change types and number of parameters
    public static PhotoDetailFragment newInstance(String param1) {
        PhotoDetailFragment fragment = new PhotoDetailFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            img_url = getArguments().getString(IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        photoView = (ImageView) v.findViewById(R.id.photoview);
        phototitle = (TextView)v.findViewById(R.id.phototxt);
        returnButton = (Button)v.findViewById(R.id.returnbtn);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentDetailListener) {
            mListener = (onFragmentDetailListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement onFragmentDetailListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onFragmentDetailListener {
        // TODO: Update argument type and name
        void onFragmentDetailListener();
    }
}
