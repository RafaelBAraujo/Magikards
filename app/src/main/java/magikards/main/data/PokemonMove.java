package magikards.main.data;

import android.util.Log;

import java.util.Random;
import java.util.Set;

public final class PokemonMove {

    private String nome;
    private int forca;
    private double acuracia;
    private TipoPokemon tipo;
    private boolean disabled;

    public PokemonMove(String nome, int forca, double acuracia, TipoPokemon tipo) {
        this.nome = nome;
        this.forca = forca;
        this.acuracia = acuracia;
        this.tipo = tipo;
        this.disabled = false;
    }

//    public boolean callMove(Pokemon target) {
//
//        int dealtDamage = (this.forca / 2) - (target.getDefesa() / 3);
//
//        if(isEffective(target.getFraquezas()))
//            dealtDamage *= 2;
//        else if(isLessEffective(target.getResistencias()))
//            dealtDamage /= 2;
//
//        Random numberGenarator = new Random();
//        boolean moveWasSuccesful;
//        int result = (numberGenarator.nextInt(10) + 1) - (int) this.acuracia * 10;
//        if(result <= 0) {
//            moveWasSuccesful = true;
//            target.inflingirDano(dealtDamage);
//            Log.e("DAMAGE", "DANO: " + String.valueOf(dealtDamage));
//        }
//        else
//            moveWasSuccesful = false;
//
//        return moveWasSuccesful;
//
//    }

    public boolean isEffective(Set<TipoPokemon> fraquezas) {

        if(fraquezas.contains(this.tipo))
            return true;
        else
            return false;

    }

    public boolean isLessEffective(Set<TipoPokemon> resistencias) {

        if(resistencias.contains(this.tipo))
            return true;
        else
            return false;

    }

    public String getNome() {
        return nome;
    }

    public int getForca() {
        return forca;
    }

    public double getAcuracia() {
        return acuracia;
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}
