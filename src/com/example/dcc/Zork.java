package com.example.dcc;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dcc.helpers.ObjectStorage;


/**
 * Created by Sam on 5/31/13.
 */
public class Zork extends Fragment implements View.OnClickListener {
    View view;

    EditText responseText;
    Button enterButton;
    Button clearButton;
    Button restartButton;
    TextView troll;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.zork,
                container, false);
        responseText = (EditText) view.findViewById(R.id.response);
        enterButton = (Button) view.findViewById(R.id.enterbutton);
        troll = (TextView) view.findViewById(R.id.trolltext);
        restartButton = (Button) view.findViewById(R.id.restart);
        clearButton = (Button) view.findViewById(R.id.clear);
        enterButton.setOnClickListener(this);
        clearButton.setOnClickListener(this);
        restartButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.enterbutton:
                if(responseText.getText().toString().equalsIgnoreCase("")){
                return;
            }
                if(responseText.getText().toString().equalsIgnoreCase("attack terrible troll with nasty knife")){
                troll.setText("The terrible troll dies a horrible death. Would you like to loot the body?");
                    responseText.setText("");
            }
            if(troll.getText().toString().equalsIgnoreCase("The terrible troll dies a horrible death. Would you like to loot the body?")){
                if(responseText.getText().toString().equalsIgnoreCase("yes")){
                    troll.setText("Congratulations you found 1,000 gold!");
                    responseText.setText("");
                }
            }
            break;
            case R.id.clear:
                responseText.setText("");
                break;
            case R.id.restart:
                troll.setText("The terrible troll raises his sword.");
                responseText.setText("");
                break;
        }
    }
}