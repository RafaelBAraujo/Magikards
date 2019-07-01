package magikards.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Random;

import magikards.main.data.Pokemon;
import magikards.main.data.PokemonMove;
import magikards.main.data.PokemonStatus;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class Game {

    private static final String TAG = "Game";

    private GameActivity gameActivity;
    private Pokemon playersPokemon;
    private Pokemon enemysPokemon;
    private boolean preparingArena = true;
    private ProgressBar playersProgressBar;
    private ProgressBar enemysProgressBar;
    private TextView enemyLevel;
    private TextView playerLevel;
    private int playerMaxHP;
    private int enemyMaxHP;
    MoveEffect moveEffect;
    MoveEffect moveEffectEnemy;

    public Game(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        this.playersProgressBar = (ProgressBar) this.gameActivity.findViewById(R.id.playerHp);
        this.enemysProgressBar = (ProgressBar) this.gameActivity.findViewById(R.id.enemyHp);
        this.playerLevel = (TextView) this.gameActivity.findViewById(R.id.playerLevel);
        this.enemyLevel = (TextView) this.gameActivity.findViewById(R.id.enemyLevel);
        playersPokemon = PokemonPicker.pickPokemon(gameActivity.getApplicationContext());
        enemysPokemon = PokemonPicker.pickPokemon(gameActivity.getApplicationContext());
        this.playerMaxHP = playersPokemon.getPontosDeVida();
        this.enemyMaxHP = enemysPokemon.getPontosDeVida();
        prepareArena();
        updateMoves(playersPokemon.getAtaques());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void startGame() {

        updateGameLog("A wild " + enemysPokemon.getNome() + " appeared!",
                             "Go! " + playersPokemon.getNome() + "!");

    }

    private void updateMoves(ArrayList<PokemonMove> moves) {

        for(int i = 1; i < 5; i++) {
            Button button = (Button) this.gameActivity.findViewById(this.gameActivity.getResources().getIdentifier("move" + i, "id",
                    this.gameActivity.getPackageName()));

            button.setText(moves.get(i-1).getNome());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int viewId = v.getId();
                    PokemonMove move;
                    switch(viewId) {
                        case R.id.move1:
                            gameActivity.disableButtons();
                            if(checkParalysis(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            if(checkConfusion(playersPokemon)){
                                startNewRound();
                                break;
                            }

                            move = playersPokemon.getAtaques().get(0);
                            updateGameLog(playersPokemon.getNome() + " used " + move.getNome() + "!");
                            moveEffect = new MoveEffect(Game.this, move, playersPokemon, enemysPokemon);
                            moveEffect.makeMove();
                            if(moveEffect.wasMoveSuccesful()) {
                                if(move.getNome().compareTo("dream eater") == 0) {
                                    if(!enemysPokemon.hasStatus(PokemonStatus.Asleep)){
                                        updateGameLog(true, enemysPokemon.getNome() + " is not asleep!");
                                    }
                                }
                                gameActivity.updateEnemysProgressBar(enemysPokemon.getPontosDeVida());
                                if(move.isEffective(enemysPokemon.getFraquezas()))
                                    updateGameLog("It's super effective!");
                                else if(move.isLessEffective(enemysPokemon.getResistencias()))
                                    updateGameLog("It's not very effective...");
                            }
                            else
                                updateGameLog(playersPokemon.getNome() + "'s attack missed!");

                            checkPokemonStatus();
                            startNewRound();
                            break;

                        case R.id.move2:
                            gameActivity.disableButtons();
                            if(checkParalysis(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            if(checkConfusion(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            move = playersPokemon.getAtaques().get(1);
                            updateGameLog(playersPokemon.getNome() + " used " + move.getNome() + "!");
                            moveEffect = new MoveEffect(Game.this, move, playersPokemon, enemysPokemon);
                            moveEffect.makeMove();
                            if(moveEffect.wasMoveSuccesful()) {
                                if(move.getNome().compareTo("dream eater") == 0) {
                                    if(!enemysPokemon.hasStatus(PokemonStatus.Asleep)){
                                        updateGameLog(true, enemysPokemon.getNome() + " is not asleep!");
                                    }
                                }
                                gameActivity.updateEnemysProgressBar(enemysPokemon.getPontosDeVida());
                                if(move.isEffective(enemysPokemon.getFraquezas()))
                                    updateGameLog("It's super effective!");
                                else if(move.isLessEffective(enemysPokemon.getResistencias()))
                                    updateGameLog("It's not very effective...");
                            }
                            else
                                updateGameLog(playersPokemon.getNome() + "'s attack missed!");

                            checkPokemonStatus();
                            startNewRound();
                            break;

                        case R.id.move3:
                            gameActivity.disableButtons();
                            if(checkParalysis(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            if(checkConfusion(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            move = playersPokemon.getAtaques().get(2);
                            updateGameLog(playersPokemon.getNome() + " used " + move.getNome() + "!");
                            moveEffect = new MoveEffect(Game.this, move, playersPokemon, enemysPokemon);
                            moveEffect.makeMove();
                            if(moveEffect.wasMoveSuccesful()) {
                                if(move.getNome().compareTo("dream eater") == 0) {
                                    if(!enemysPokemon.hasStatus(PokemonStatus.Asleep)){
                                        updateGameLog(true, enemysPokemon.getNome() + " is not asleep!");
                                    }
                                }
                                gameActivity.updateEnemysProgressBar(enemysPokemon.getPontosDeVida());
                                if(move.isEffective(enemysPokemon.getFraquezas()))
                                    updateGameLog("It's super effective!");
                                else if(move.isLessEffective(enemysPokemon.getResistencias()))
                                    updateGameLog("It's not very effective...");
                            }
                            else
                                updateGameLog(playersPokemon.getNome() + "'s attack missed!");

                            checkPokemonStatus();
                            startNewRound();
                            break;

                        case R.id.move4:
                            gameActivity.disableButtons();
                            if(checkParalysis(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            if(checkConfusion(playersPokemon)){
                                startNewRound();
                                break;
                            }
                            move = playersPokemon.getAtaques().get(3);
                            updateGameLog(playersPokemon.getNome() + " used " + move.getNome() + "!");
                            moveEffect = new MoveEffect(Game.this, move, playersPokemon, enemysPokemon);
                            moveEffect.makeMove();
                            if(moveEffect.wasMoveSuccesful()) {
                                if(move.getNome().compareTo("dream eater") == 0) {
                                    if(!enemysPokemon.hasStatus(PokemonStatus.Asleep)){
                                        updateGameLog(true, enemysPokemon.getNome() + " is not asleep!");
                                    }
                                }
                                gameActivity.updateEnemysProgressBar(enemysPokemon.getPontosDeVida());
                                if(move.isEffective(enemysPokemon.getFraquezas()))
                                    updateGameLog("It's super effective!");
                                else if(move.isLessEffective(enemysPokemon.getResistencias()))
                                    updateGameLog("It's not very effective...");
                            }
                            else
                                updateGameLog(playersPokemon.getNome() + "'s attack missed!");

                            checkPokemonStatus();
                            startNewRound();
                            break;
                    }

                }
            });

        }

        this.gameActivity.enableButtons();

    }

    private void prepareArena() {

        this.gameActivity.changeProgressBarColors();
        this.gameActivity.disableButtons();
        this.gameActivity.setPlayersProgressBarMax(this.playerMaxHP);
        this.gameActivity.setEnemysProgressBarMax(this.enemyMaxHP);
        this.gameActivity.updatePlayersProgressBar(this.playerMaxHP);
        this.gameActivity.updateEnemysProgressBar(this.enemyMaxHP);

        this.playerLevel.setText(":L"+String.valueOf(playersPokemon.getLevel()));
        this.enemyLevel.setText(":L"+String.valueOf(enemysPokemon.getLevel()));

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fireRef = storage.getReference();
        StorageReference pathReference = fireRef.child("game/back/"+ String.valueOf(playersPokemon.getPokedexNumber()) +".png");

        pathReference.getBytes(1024 * 1024 * 2).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imageView = (ImageView) gameActivity.findViewById(R.id.pokemon1);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        pathReference = fireRef.child("game/front/"+ String.valueOf(enemysPokemon.getPokedexNumber()) +".png");
        pathReference.getBytes(1024 * 1024 * 2).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView imageView = (ImageView) gameActivity.findViewById(R.id.pokemon2);
                imageView.setImageBitmap(bitmap);
                preparingArena = false;
            }
        });

    }

    private void startNewRound() {

        if(enemysPokemon.getPontosDeVida() <= 0) {
            endGame(true);
            return;
        }
        else if(playersPokemon.getPontosDeVida() <= 0) {
            endGame(false);
            return;
        }

        if(checkParalysis(this.enemysPokemon)) {
            this.gameActivity.enableButtons();
            return;
        }

        if(checkConfusion(this.enemysPokemon)){
            this.gameActivity.enableButtons();
            return;
        }

        Random random = new Random();
        int sortedMove = random.nextInt(4);

        PokemonMove move = enemysPokemon.getAtaques().get(sortedMove);
        moveEffectEnemy = new MoveEffect(Game.this, move, enemysPokemon, playersPokemon);
        updateGameLog(true, enemysPokemon.getNome() + " used " + move.getNome() + "!");
        moveEffectEnemy.makeMove();
        if(moveEffectEnemy.wasMoveSuccesful()){
            if(move.getNome().compareTo("dream eater") == 0) {
                if(!this.playersPokemon.hasStatus(PokemonStatus.Asleep)){
                    updateGameLog(true, this.playersPokemon.getNome() + " is not asleep!");
                }
            }
            if(move.isEffective(playersPokemon.getFraquezas()))
                updateGameLog(true, "It's super effective!");
            else if(move.isLessEffective(playersPokemon.getResistencias()))
                updateGameLog(true, "It's not very effective...");
        }
        else {
            updateGameLog(enemysPokemon.getNome() + " used " + move.getNome() + "!");
            updateGameLog(true, enemysPokemon.getNome() + "'s attack missed!");
        }

        if(playersPokemon.getPontosDeVida() <= 0){
            this.gameActivity.updatePlayersProgressBar(0);
            endGame(false);
            return;
        }

        checkEnemyStatus();
        this.gameActivity.updatePlayersProgressBar(playersPokemon.getPontosDeVida());

    }

    private boolean checkParalysis(Pokemon pokemon){
        if(pokemon.hasStatus(PokemonStatus.Paralyzed)){
            Random numGenerator = new Random();
            int gen = (numGenerator.nextInt(10) + 1);
            return (gen <= 4);
        }
        return false;
    }

    private boolean checkConfusion(Pokemon pokemon){
        if(this.enemysPokemon.getPontosDeVida() > 0 && this.playersPokemon.getPontosDeVida() > 0) {
            if(pokemon.hasStatus(PokemonStatus.Confused)) {
                updateGameLog(pokemon.getNome() + " is confused!");
                Random numGenerator = new Random();
                int gen = numGenerator.nextInt(10) + 1;
                if(gen <= 5){
                    pokemon.inflingirDano(calculateConfusionDamage(pokemon));
                    updateGameLog(pokemon.getNome() + " hurts itself in it's confusion!");
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    private int calculateConfusionDamage(Pokemon target) {

        int damage = (((((2 * 10) / 5) + 2) * 40 * (40/target.getDefesa())) / 50) + 2;

        return damage;
    }

    private void checkPokemonStatus(){
        if(this.enemysPokemon.getPontosDeVida() > 0) {
            if(this.playersPokemon.hasStatus(PokemonStatus.Poisoned)){
                this.playersPokemon.inflingirDano(this.playersPokemon.getMaxHP()/16);
                this.updateGameLog(this.playersPokemon.getNome() + " is hurt by poison!");
            }
            if(this.playersPokemon.hasStatus(PokemonStatus.Burned)){
                this.playersPokemon.inflingirDano(this.playersPokemon.getMaxHP()/16);
                this.updateGameLog(this.playersPokemon.getNome() + " is hurt by it's burn!");
            }
        }
    }

    private void checkEnemyStatus(){
        if(this.playersPokemon.getPontosDeVida() > 0) {
            if(this.enemysPokemon.hasStatus(PokemonStatus.Poisoned)){
                this.enemysPokemon.inflingirDano(this.enemysPokemon.getMaxHP()/16);
                this.updateGameLog(this.enemysPokemon.getNome() + " is hurt by poison!");
            }
            if(this.enemysPokemon.hasStatus(PokemonStatus.Burned)){
                this.enemysPokemon.inflingirDano(this.enemysPokemon.getMaxHP()/16);
                this.updateGameLog(this.enemysPokemon.getNome() + " is hurt by it's burn!");
            }
        }
    }

    private void endGame(boolean winCondition) {

        this.gameActivity.disableButtons();
        if(winCondition == true) {
            new EndGameTask((TextView) gameActivity.findViewById(R.id.gameText), 2000, gameActivity, winCondition).execute(enemysPokemon.getNome() + " fainted!");
        }
        else{
            new EndGameTask((TextView) gameActivity.findViewById(R.id.gameText), 2000, gameActivity, winCondition).execute(playersPokemon.getNome() + " fainted!");
        }

    }

    public void updateGameLog(String... args) {
        new WriteGameMessage(this.gameActivity, 2000)
                .execute(args);
    }

    public void updateGameLog(boolean enableButtons, String... args) {
        new WriteGameMessage(this.gameActivity, 2000, enableButtons)
                .execute(args);
    }

    public void updateGameLog(boolean enableButtons, int sleep, String... args) {
        new WriteGameMessage(this.gameActivity, sleep, enableButtons)
                .execute(args);
    }

}

class WriteGameMessage extends AsyncTask<String, String, Void> {

    private TextView textView;
    private GameActivity gameActivity;
    private int sleep;
    private boolean enableButtons;


    public WriteGameMessage(GameActivity gameActivity, int sleep, boolean enableButtons){
        this.gameActivity = gameActivity;
        this.textView = (TextView) gameActivity.findViewById(R.id.gameText);
        this.sleep = sleep;
        this.enableButtons = enableButtons;
    }

    public WriteGameMessage(GameActivity gameActivity, int sleep){
        this.gameActivity = gameActivity;
        this.textView = (TextView) gameActivity.findViewById(R.id.gameText);
        this.sleep = sleep;
        this.enableButtons = false;
    }

    @Override
    protected void onPreExecute() {
//        this.gameActivity.disableButtons();
    }

    @Override
    protected Void doInBackground(String... args) {

        try {
            for(String s : args) {
                publishProgress(s);
                Thread.sleep(sleep);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... args) {
        this.textView.setText(args[0]);
    }

    @Override
    protected void onPostExecute(Void v) {
        if(this.enableButtons)
            this.gameActivity.enableButtons();
    }

}

class EndGameTask extends AsyncTask<String, String, Void> {

    TextView textView;
    int sleep;
    GameActivity activityToFinish;
    boolean winCondition;

    public EndGameTask(TextView textView, int sleep, GameActivity activityToFinish, boolean winCondition){
        this.winCondition = winCondition;
        this.textView = textView;
        this.sleep = sleep;
        this.activityToFinish = activityToFinish;
    }

    @Override
    protected void onPreExecute(){
        this.activityToFinish.disableButtons();
    }

    @Override
    protected Void doInBackground(String... args) {

        try {
            for(String s : args) {
                publishProgress(s);
                Thread.sleep(sleep);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(String... args) {
        this.textView.setText(args[0]);
    }

    @Override
    protected void onPostExecute(Void v){
        if(winCondition){
            this.activityToFinish.startActivity(new Intent(this.activityToFinish, VictoryActivity.class));
            this.activityToFinish.stopMusic();
            this.activityToFinish.finish();
        }
        else{
            this.activityToFinish.startActivity(new Intent(this.activityToFinish, LossActivity.class));
            this.activityToFinish.stopMusic();
            this.activityToFinish.finish();
        }
    }

}