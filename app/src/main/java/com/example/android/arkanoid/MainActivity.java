package com.example.android.arkanoid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class MainActivity extends AppCompatActivity {

    private Game game;
    private UpdateThread myThread;
    private Handler updateHandler;
    private int selectedController;
    String[] controllers = {"Touch", "Accelerometro"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // imposta l'orientamento dello schermo
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scegli come Controllare il Paddle");
        builder.setItems(controllers, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedController = which;
            }
        });
        builder.show();
        // crea un nuovo gioco
        game = new Game(this, 3, 0, selectedController);
        setContentView(game);

        // vytvori handler a thread
        //crea un gestore di thread
        VytvorHandler();
        myThread = new UpdateThread(updateHandler);
        myThread.start();
    }

    //metodo per governare il tasto back del sistema operativo
    @Override
    public void onBackPressed(){
        Intent toMainMenu = new Intent(this, Main_Menu.class);
        startActivity(toMainMenu);
    }

    private void VytvorHandler() {
        updateHandler = new Handler() {
            public void handleMessage(Message msg) {
                game.invalidate();
                game.update();
                super.handleMessage(msg);
            }
        };
    }
    //zastavSnimanie = smetti si sparare
    protected void onPause() {
        super.onPause();
        game.smettiDiSparare();
    }

    //spusti Snimanie = Esegui scansione
    protected void onResume() {
        super.onResume();
        game.iniziaSparare();
    }

}
