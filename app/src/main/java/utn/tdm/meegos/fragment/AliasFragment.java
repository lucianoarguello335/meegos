package utn.tdm.meegos.fragment;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputLayout;

import utn.tdm.meegos.R;
import utn.tdm.meegos.activity.ChatContactActivity;
import utn.tdm.meegos.database.MeegosSQLHelper;

public class AliasFragment extends DialogFragment {

    public static final String CONTACTO_LOOKUP = "contacto_lookup";
    String contactoLookup = "";
    String alias = "";
    EditText aliasEditText;
    TextInputLayout aliasTIL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contactoLookup = getArguments().getString(CONTACTO_LOOKUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.alias_fragment, container, false);
        aliasTIL = view.findViewById(R.id.aliasTIL);
        aliasEditText = view.findViewById(R.id.alias);
        aliasEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0) {
                    aliasTIL.setError(null);
                    aliasTIL.setErrorEnabled(false);
                }
            }
        });

        Button aliasSignUpButton = view.findViewById(R.id.alias_sign_up);
        aliasSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alias = aliasEditText.getText().toString();
                if (alias.length() < 5) {
                    aliasTIL.setError(getString(R.string.min_alias_length));
                    aliasTIL.setErrorEnabled(true);
                } else {
                    MeegosSQLHelper db = new MeegosSQLHelper(getContext());

                    db.insertAlias(contactoLookup, alias);
                    db.close();
                    dismiss();
                    Toast.makeText(getActivity(), R.string.alias_registered, Toast.LENGTH_LONG).show();

                    // TODO: IR al chat.
                    Bundle bundle = new Bundle();
                    bundle.putString("alias", alias);
                    bundle.putString("lookupKey", contactoLookup);
                    Intent intent = new Intent(getContext(), ChatContactActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        return view;
    }
}
