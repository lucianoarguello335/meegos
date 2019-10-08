package utn.tdm.meegos.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import utn.tdm.meegos.R;
import utn.tdm.meegos.task.ServerTask;
import utn.tdm.meegos.preferences.MeegosPreferences;
import utn.tdm.meegos.util.XMLDataBlock;
import utn.tdm.meegos.util.XMLUtil;

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

    EditText usernameEditText;
    EditText passwordEditText;

    TextInputLayout usernameTIL;
    TextInputLayout passwordTIL;

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

        usernameTIL = view.findViewById(R.id.loginUsernameTIL);
        passwordTIL = view.findViewById(R.id.loginPasswordTIL);

        usernameEditText = view.findViewById(R.id.loginUsername);
        passwordEditText = view.findViewById(R.id.loginPassword);

        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    usernameTIL.setError(null);
                    usernameTIL.setErrorEnabled(false);
                }
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    passwordTIL.setError(null);
                    passwordTIL.setErrorEnabled(false);
                }
            }
        });

        Button signUpButton = view.findViewById(R.id.login_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                if (checkFields(username, password)){
                    XMLDataBlock requestBodyBlock = XMLUtil.getDataBlockRegisterUser(username, password);

                    new ServerTask(getActivity().getApplicationContext(), view,
                        new ServerTask.ServerListener() {
                            @Override
                            public void toDoOnSuccessPostExecute(XMLDataBlock responseXMLDataBlock) {
                                if (responseXMLDataBlock.getAttribute("type").equals("success")) {
                                    //Registramos el usuario en las preferences
                                    XMLDataBlock userblock = responseXMLDataBlock.getChildBlock("user");
                                    MeegosPreferences.setUsername(getActivity(), userblock.getAttribute(USERNAME));
                                    MeegosPreferences.setPassword(getActivity(), password);

//                            Notificamos el registro
                                    // TODO: Reemplazar por una notificacion
                                    Toast.makeText(getContext(), "El usuario: " + userblock.getAttribute(USERNAME) + " ha sido creado.", Toast.LENGTH_SHORT).show();
                                    dismiss();
//                            startActivity(new Intent(getActivity(), ChatContactActivity.class));
                                }
                            }
                        }).execute(requestBodyBlock);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private Boolean checkFields(String username, String password){
        if(username.length() < 5) {
            usernameTIL.setError(getString(R.string.min_username_length));
            usernameTIL.setErrorEnabled(true);
        return false;
        }
        if(password.length() < 6) {
            passwordTIL.setError(getString(R.string.min_password_length));
            passwordTIL.setErrorEnabled(true);
            return false;
        }
        return true;
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
