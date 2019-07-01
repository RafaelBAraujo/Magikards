package magikards.main;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity {

    ProgressBar playershp;
    ProgressBar enemyshp;
    MediaPlayer mediaPlayer;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mediaPlayer = MediaPlayer.create(this, R.raw.battle);

         this.playershp = (ProgressBar) this.findViewById(R.id.playerHp);
         this.enemyshp = (ProgressBar) this.findViewById(R.id.enemyHp);

         TextView gameText = (TextView) this.findViewById(R.id.gameText);
         Typeface face = Typeface.createFromAsset(getAssets(), "fonts/game_font.ttf");
         gameText.setTypeface(face);

        TextView playerLevel = (TextView) this.findViewById(R.id.playerLevel);
        playerLevel.setTypeface(face);

        TextView enemyLevel = (TextView) this.findViewById(R.id.enemyLevel);
        enemyLevel.setTypeface(face);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume(){
        super.onResume();

        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        Game game = new Game(this);
        game.startGame();

    }

    @Override
    public void onStop(){
        super.onStop();
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    public void stopMusic(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
    }

    public void updateGameText(String text) {
        ((TextView) findViewById(R.id.gameText)).setText(text);
    }

    public void disableButtons() {
        for(int i = 1; i < 5; i++) {
            Button button = (Button) this.findViewById(this.getResources().getIdentifier("move" + i, "id",
                    this.getPackageName()));

            button.setEnabled(false);
        }
    }

    public void enableButtons() {
        for(int i = 1; i < 5; i++) {
            Button button = (Button) this.findViewById(this.getResources().getIdentifier("move" + i, "id",
                    this.getPackageName()));

            button.setEnabled(true);
        }
    }

    public void changeProgressBarColors() {

        Drawable progressDrawablePlayer = this.playershp.getProgressDrawable().mutate();
        progressDrawablePlayer.setColorFilter(this.getResources().getColor(R.color.colorHitPoints),
                PorterDuff.Mode.SRC_IN);
        this.playershp.setProgressDrawable(progressDrawablePlayer);

        Drawable progressDrawableEnemy = this.enemyshp.getProgressDrawable().mutate();
        progressDrawableEnemy.setColorFilter(this.getResources().getColor(R.color.colorHitPoints),
                PorterDuff.Mode.SRC_IN);
        this.enemyshp.setProgressDrawable(progressDrawableEnemy);

    }

    public void updatePlayersProgressBar(int progress) {
        if(progress < 0){
            progress = 0;
        }
        this.playershp.setProgress(progress);
    }

    public void updateEnemysProgressBar(int progress) {
        if(progress < 0){
            progress = 0;
        }
        this.enemyshp.setProgress(progress);
    }

    public void setPlayersProgressBarMax(int max){
        this.playershp.setMax(max);
    }

    public void setEnemysProgressBarMax(int max){
        this.enemyshp.setMax(max);
    }

}
