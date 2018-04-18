package de.brettin.leon.travelfriend.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.TextView;

import de.brettin.leon.travelfriend.R;
import de.brettin.leon.travelfriend.resources.TfDatabase;
import de.brettin.leon.travelfriend.resources.TfPositionCheckRes;
import de.brettin.leon.travelfriend.resources.TfUserNameRes;

/**
 * Created by Leon on 08.04.18.
 */

public class TfAccountFragment extends Fragment{

    public static TfAccountFragment newInstance() {
        TfAccountFragment fragment = new TfAccountFragment();
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.accountfragment, container, false);

        SetUpClass setUpClass = new SetUpClass(view);
        setUpClass.setUpUsernameCard();
        setUpClass.setUpPositionCheck();
        setUpClass.setUpUpdatePosition();

        return view;
    }
}

/**
 * Class for Setting up the different cards.
 * This adds some nice wrapping around the fragment
 */
class SetUpClass {

    private View mView;

    private String mCurrentUsername;

    private AppCompatCheckBox mPositionCheckbox;

    private FloatingActionButton mUpdatePositionButton;
    private ProgressBar mUpdatePositionProgressBar;
    private TextView mUpdatePositionText;

    public SetUpClass(View view) {
        mView = view;

        mPositionCheckbox = mView.findViewById(R.id.positionCheckBox);

        mUpdatePositionButton = mView.findViewById(R.id.updatePositionButton);
        mUpdatePositionProgressBar = mView.findViewById(R.id.updatePositionProgressBar);
        mUpdatePositionText = mView.findViewById(R.id.updatePositionText);
    }

    /**
     * Setting up some cool interactive things with the username card
     */
    void setUpUsernameCard() {
        final FloatingActionButton changeUsernameButton = mView.findViewById(R.id.changeUsernameButton);
        final TextInputEditText usernameInputText = mView.findViewById(R.id.usernamehint);

        mCurrentUsername = TfUserNameRes.getInstance(mView.getContext()).getUsername();
        usernameInputText.setText(mCurrentUsername);

        usernameInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals(mCurrentUsername)) {
                    changeUsernameButton.setImageResource(R.drawable.ic_autorenew_black_24dp);
                } else {
                    changeUsernameButton.setImageResource(R.drawable.ic_done_black_24dp);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Do nothing
            }
        });

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newUserName = usernameInputText.getText().toString();
                if (!newUserName.equals(mCurrentUsername)) {
                    TfUserNameRes.getInstance(view.getContext()).setUsername(newUserName);
                    mCurrentUsername = newUserName;

                    changeUsernameButton.setImageResource(R.drawable.ic_done_black_24dp);
                }

                usernameInputText.onEditorAction(EditorInfo.IME_ACTION_DONE);
            }
        });
    }

    /**
     * Set up the functionality for the Position Checkbox
     */
    void setUpPositionCheck() {
        mPositionCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TfPositionCheckRes checkPositionResource = TfPositionCheckRes.getInstance(view.getContext());
                checkPositionResource.setCheckPosition(mPositionCheckbox.isChecked());

            }
        });
    }

    /**
     * Set up the functionality for the Update Position Field
     */
    void setUpUpdatePosition() {
        mUpdatePositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdatePositionProgressBar.setVisibility(View.VISIBLE);
                mUpdatePositionButton.setVisibility(View.GONE);

                (new TfDatabase(view.getContext())).updateOwnPosition(view.getContext());

                new CountDownTimer(view.getContext().getResources().getInteger(R.integer.updatePositionMaximumSeconds), 1000) {

                    public void onTick(long millisUntilFinished) {
                        mUpdatePositionText.setText(mView.getResources().getString(R.string.update_position_timing, millisUntilFinished / 1000));
                    }

                    public void onFinish() {
                        mUpdatePositionText.setText(R.string.update_position);
                        mUpdatePositionProgressBar.setVisibility(View.GONE);
                        mUpdatePositionButton.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });
    }
}