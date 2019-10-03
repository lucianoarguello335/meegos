package utn.tdm.meegos.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.Hashtable;
import java.util.UUID;

import utn.tdm.meegos.R;
import utn.tdm.meegos.Task.ServerTask;
import utn.tdm.meegos.activity.ChatContactActivity;
import utn.tdm.meegos.util.XMLDataBlock;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogInFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogInFragment} factory method to
 * create an instance of this fragment.
 */
public class LogInFragment extends DialogFragment {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    String username = "";
    String password = "";

    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(USERNAME);
            password = getArguments().getString(PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.log_in_fragment, container, false);
        Button signUpButton = (Button) view.findViewById(R.id.login_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = (EditText) view.findViewById(R.id.loginUsername);
                EditText password = (EditText) view.findViewById(R.id.loginPassword);

                Hashtable<String, String> requestAttributes = new Hashtable<>();
                requestAttributes.put("id", UUID.randomUUID().toString());
                requestAttributes.put("name", "register-user");
                XMLDataBlock requestBodyBlock = new XMLDataBlock("action", null, requestAttributes);
                XMLDataBlock actionDetailBlock = new XMLDataBlock("action-detail", requestBodyBlock, null);
                Hashtable<String, String> user = new Hashtable<>();
                user.put(USERNAME, username.getText().toString());
                user.put(PASSWORD, password.getText().toString());
                XMLDataBlock userBlock = new XMLDataBlock(
                        "user",
                        actionDetailBlock,
                        user
                );
                actionDetailBlock.addChild(userBlock);
                requestBodyBlock.addChild(actionDetailBlock);
                new ServerTask(getActivity().getApplicationContext(),
                    new ServerTask.ServerListener() {
                    @Override
                    public void toDoOnSuccessPostExecute(XMLDataBlock xmlDataBlock) {
                        dismiss();
                        startActivity(new Intent(getActivity(), ChatContactActivity.class));
                    }
                }).execute(requestBodyBlock);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(View view) {
        if (mListener != null) {
            mListener.onFragmentInteraction(null);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
