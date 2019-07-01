package magikards.main;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import magikards.main.data.Pokemon;
import magikards.main.data.PokemonMove;
import magikards.main.data.TipoPokemon;

public class PokemonPicker {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static Pokemon pickPokemon(Context appContext) {

        Pokemon pokemon = null;

        InputStream pokemonInputStream = appContext.getResources().openRawResource(R.raw.species);

        try(BufferedReader br = new BufferedReader(new InputStreamReader(pokemonInputStream))) {

            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

            String everything = sb.toString();

            JSONArray pokeJson = new JSONArray(everything);

            Random random = new Random();
            Integer randPokeIndex = random.nextInt(pokeJson.length());

            JSONObject randPoke = pokeJson.getJSONObject(randPokeIndex);

            int pokedexNumber = randPoke.getInt("national_pokedex_number");
            String randPokeName = randPoke.getString("name");
            int ataque = randPoke.getJSONObject("baseStats").getInt("attack");
            int hp = randPoke.getJSONObject("baseStats").getInt("hp");
            int defesa = randPoke.getJSONObject("baseStats").getInt("defense");
            JSONArray types = randPoke.getJSONArray("types");

            ArrayList<TipoPokemon> enumTipos = new ArrayList<>();
            for(int i = 0; i < types.length(); i ++) {
                enumTipos.add(pokemonTypeToEnum(types.getString(i)));
            }

            InputStream movesInputStream = appContext.getResources().openRawResource(R.raw.moves);
            BufferedReader brMoves = new BufferedReader(new InputStreamReader(movesInputStream));
            StringBuilder sbMoves = new StringBuilder();

            String lineMoves = brMoves.readLine();

            while(lineMoves != null) {
                sbMoves.append(lineMoves);
                sbMoves.append(System.lineSeparator());
                lineMoves = brMoves.readLine();
            }

            String wholeMovesJsonString = sbMoves.toString();

            JSONArray movesJson = new JSONArray(wholeMovesJsonString);

            // sorting 4 moves for the pokemon
            ArrayList<PokemonMove> pokemonMoves = new ArrayList<>();
            for(int i = 0; i < 4; i ++ ) {

                int randMoveIndex = random.nextInt(movesJson.length());
                JSONObject moveJson = movesJson.getJSONObject(randMoveIndex);
                TipoPokemon moveType = pokemonTypeToEnum(moveJson.getString("type"));

                boolean typeIsCorrect = false;
                for(TipoPokemon t : enumTipos) {
                    if(moveType == t)
                        typeIsCorrect = true;
                }

                if(typeIsCorrect) {
                    String moveName = moveJson.getString("name");
                    int movePower = moveJson.getInt("power");
                    double moveAccuracy = moveJson.getDouble("accuracy");
                    PokemonMove newMove = new PokemonMove(moveName, movePower, moveAccuracy, moveType);
                    pokemonMoves.add(newMove);
                }
                else{
                    i--;
                }

            }

            int level = 10;
            String capitalPokeName = randPokeName.substring(0, 1).toUpperCase() + randPokeName.substring(1);
            pokemon = new Pokemon(pokedexNumber, capitalPokeName, hp, enumTipos, level, pokemonMoves, ataque, defesa);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return pokemon;

    }

    private static TipoPokemon pokemonTypeToEnum(String type) {

        switch (type) {

            case "grass":
                return TipoPokemon.Grass;

            case "dragon":
                return TipoPokemon.Dragon;

            case "ice":
                return TipoPokemon.Ice;

            case "fighting":
                return TipoPokemon.Fighting;

            case "fire":
                return TipoPokemon.Fire;

            case "flying":
                return TipoPokemon.Flying;

            case "ghost":
                return TipoPokemon.Ghost;

            case "ground":
                return TipoPokemon.Ground;

            case "electric":
                return TipoPokemon.Electric;

            case "normal":
                return TipoPokemon.Normal;

            case "poison":
                return TipoPokemon.Poison;

            case "psychic":
                return TipoPokemon.Psychic;

            case "rock":
                return TipoPokemon.Rock;

            case "water":
                return TipoPokemon.Water;

            default:
                return TipoPokemon.Normal;

        }

    }

}
