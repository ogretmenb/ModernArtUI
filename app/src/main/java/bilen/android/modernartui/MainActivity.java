package bilen.android.modernartui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    private SeekBar seekBar;
    private ArrayList<TextView> listToChange= new ArrayList<>();
    // String for LogCat documentation
    private final static String TAG = "ModernArtUI";
    private final static int RGB_COLOR = 256;
    //private int previousProgress = 0;
    static private final String URL = "http://www.moma.org";
    private DialogFragment mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = (SeekBar) findViewById(R.id.seekBar);

        ViewGroup viewGroup = (ViewGroup)findViewById(R.id.views);
        listToChange = getTextViews(viewGroup);
        //Log.i(TAG,"Selected List Size:"+ String.valueOf(listToChange.size()));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Toast.makeText(getApplicationContext(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();
                Log.i(TAG, String.valueOf(progress) + "->"+String.valueOf(progress));
                for (TextView txtview : listToChange) {
                    int newColor = ((ColorDrawable) txtview.getBackground()).getColor();
                    //newColor = Color.rgb((Color.red(newColor) + (progress-previousProgress)*RGB_COLOR/100)%RGB_COLOR,(Color.green(newColor)+(progress-previousProgress)*RGB_COLOR/100)%RGB_COLOR, (Color.blue(newColor)+(progress-previousProgress)*RGB_COLOR/100)%RGB_COLOR) ;

                    float[] hsvColor = {0, 1, 1};
                    // generate only hue component in range [0, 360),
                    // leaving saturation and brightness maximum possible
                    Color.colorToHSV(newColor,hsvColor);
                    hsvColor[0] += 360f * (progress) / 100;
                    hsvColor[0] %=360;
                    newColor =Color.HSVToColor(hsvColor);

                    Log.i(TAG, "--->Id:" + String.valueOf(txtview.getId()) + ", Color:" + Integer.toHexString(newColor));

                    txtview.setBackgroundColor(newColor);

                }

                //previousProgress = progress;    //this progress becomes previous progress!
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Create a new AlertDialogFragment
        mDialog = AlertDialogFragment.newInstance();

        // Show AlertDialogFragment
        mDialog.show(getFragmentManager(), "Alert");



        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static ArrayList<TextView> getTextViews(ViewGroup viewGroup)
    {
        ArrayList<TextView> list = new ArrayList<>();

        if (viewGroup.getChildCount() == 0)
            return list;
        else
        {
            for (int index = 0; index < viewGroup.getChildCount();index ++)
            {
                View view = viewGroup.getChildAt(index);
                if (view instanceof TextView) {
                    TextView textView= (TextView) view;
                    int textViewColor = ((ColorDrawable)textView.getBackground()).getColor();
                    if (textViewColor != Color.WHITE && textViewColor != Color.GRAY)
                        list.add(textView);
                }
                else if (view instanceof ViewGroup)
                {
                    list.addAll(getTextViews((ViewGroup)view));
                }

            }
        }

        return list;
    }

    // Class that creates the AlertDialog
    public static class AlertDialogFragment extends DialogFragment {

        public static AlertDialogFragment newInstance() {
            return new AlertDialogFragment();
        }

        // Build AlertDialog using AlertDialog.Builder
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new AlertDialog.Builder(getActivity())
                    .setMessage("Click Below to learn more?")

                            // User cannot dismiss dialog by hitting back button
                    .setCancelable(false)

                            // Set up No Button
                    .setNegativeButton("Visit MOMA",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent baseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
                                    startActivity(baseIntent);
                                }
                            })

                            // Set up Yes Button
                    .setPositiveButton("Not Now",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        final DialogInterface dialog, int id) {
                                   dismiss();
                                }
                            }).create();
        }
    }
}
