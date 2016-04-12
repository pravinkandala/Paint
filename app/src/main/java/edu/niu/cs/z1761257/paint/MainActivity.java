package edu.niu.cs.z1761257.paint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.Image;
import android.provider.MediaStore;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends Activity implements View.OnClickListener {

    //variables
    private DrawingView drawingView;
    private ImageButton currentColor;
    private Button brushBtn, newBtn, eraseBtn, saveBtn;

    private float smallBrush, mediumBrush, largeBrush;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = (DrawingView)findViewById(R.id.drawing);

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.colors);

        currentColor = (ImageButton)paintLayout.getChildAt(0);
        currentColor.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.paint_pressed,null));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawingView.setPaintColor(currentColor.getTag().toString());
        drawingView.setBrushSize(mediumBrush);

        brushBtn = (Button)findViewById(R.id.brushButton);
        brushBtn.setOnClickListener(this);

        saveBtn = (Button)findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(this);

        eraseBtn = (Button)findViewById(R.id.eraseButton);
        eraseBtn.setOnClickListener(this);

        newBtn = (Button)findViewById(R.id.newButton);
        newBtn.setOnClickListener(this);

    }//end of onCreate


    //onCreate method for all of the buttons
    //view v will be the specific button that was clicked


    @Override
    public void onClick(View v){
        if(v.getId() == R.id.brushButton){
            final Dialog brushDialog = new Dialog(this);
            brushDialog.setTitle("Select a Brush size:");
            brushDialog.setContentView(R.layout.brush_choice);

            brushDialog.show();
            Button smallBtn = (Button)brushDialog.findViewById(R.id.brushButton);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setBrushSize(smallBrush);
                    drawingView.setLastBrushSize(smallBrush);

                    drawingView.setErase(false);

                    brushDialog.dismiss();
                }
            });



        }//end of brush Button
        //handle erase button

        else if(v.getId()==R.id.eraseButton){

            final Dialog eraseDialog = new Dialog(this);
            eraseDialog.setTitle("Select an eraser size:");
            eraseDialog.setContentView(R.layout.eraser_choice);
            eraseDialog.show();

            Button smallBtn = (Button)eraseDialog.findViewById(R.id.smallEraseButton);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawingView.setBrushSize(smallBrush);
                    drawingView.setErase(true);
                    eraseDialog.dismiss();
                }
            });

        }//end of erase Button

        //handle the new Button
        else if(v.getId() == R.id.newButton){

            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New Drawing");
            newDialog.setMessage("Start a new drawing (you will lose the current drawing)?");

            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawingView.newDrawing();

                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            newDialog.show();

        }//end of new button

        else if (v.getId()==R.id.saveButton){
            final AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
            saveDialog.setTitle("Save Drawing");
            saveDialog.setMessage("Save drawing to device gallery");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawingView.setDrawingCacheEnabled(true);

                    String savedImageURI = MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(),
                            drawingView.getDrawingCache(),
                            UUID.randomUUID().toString() + ".png",
                            "drawing"
                    );

                    if (savedImageURI != null) {
                        Toast.makeText(getApplicationContext(), "Saved Successfull. " + savedImageURI.toString(), Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Drawing could'nt be saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();
            drawingView.destroyDrawingCache();

        }//end of save button

        //Selecting a paint color


    }//end of onClick
    public void paintClicked(View view){
        drawingView.setErase(false);
        drawingView.setBrushSize(drawingView.getLastBrushSize());

        if(view != currentColor){
            ImageButton imageButton = (ImageButton)view;
            String color = imageButton.getTag().toString();

            drawingView.setPaintColor(color);
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed, null));
            currentColor.setImageDrawable(getResources().getDrawable(R.drawable.paint_color, null));
            currentColor= (ImageButton)imageButton;
        }
    }//end of paintClicked
}//end of MainActivity
