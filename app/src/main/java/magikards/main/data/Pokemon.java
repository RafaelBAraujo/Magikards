package magikards.main.data;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Pokemon {

    private int pokedexNumber;
    private String nome;
    private ArrayList<TipoPokemon> tipos;
    private int level;
    private int pontosDeVida;
    private int maxHP;
    private ArrayList<PokemonMove> ataques;
    private int ataque;
    private int defesa;
    private int agilidade;
    private int especial;
    private Set<TipoPokemon> fraquezas;
    private Set<TipoPokemon> resistencias;
    private Set<PokemonStatus> status;
    private PokemonMove lastMove;

    public Pokemon(int pokedexNumber, String nome, int pontosDeVida, ArrayList<TipoPokemon> tipos, int level, ArrayList<PokemonMove> ataques, int ataque, int defesa) {

        this.pokedexNumber = pokedexNumber;
        this.nome = nome;
        this.tipos = tipos;
        this.ataques = ataques;
        this.level = level;
        this.pontosDeVida = pontosDeVida;
        this.maxHP = pontosDeVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.agilidade = 0;
        this.especial = 0;
        this.definirFraquezas();
        this.definirResistencias();
        this.status = new HashSet<>();

    }

    public boolean callMove(Pokemon target, int moveIndex) {

        this.lastMove = this.ataques.get(moveIndex);

        PokemonMove move = this.getAtaques().get(moveIndex);
        int dealtDamage = (((((2 * this.level) / 5) + 2) * move.getForca() * (this.getAtaque()/target.getDefesa())) / 50) + 2;

        if(move.isEffective(target.getFraquezas()))
            dealtDamage *= 2;
        else if(move.isLessEffective(target.getResistencias()))
            dealtDamage /= 2;

        Random numberGenarator = new Random();
        boolean moveWasSuccesful;
        if(move.getAcuracia() > 0.0) {
           int gen = (numberGenarator.nextInt(10) + 1);
           moveWasSuccesful = gen <= move.getAcuracia() * 10;
            if(moveWasSuccesful) {
                target.inflingirDano(dealtDamage);
                Log.e("DAMAGE", "DANO: " + String.valueOf(dealtDamage));
            }
        }
        else{
            moveWasSuccesful = true;
        }

        return moveWasSuccesful;

    }

    public Set<TipoPokemon> getFraquezas() {
        return fraquezas;
    }

    public Set<TipoPokemon> getResistencias() {
        return resistencias;
    }

    private void definirResistencias() {

        this.resistencias = new HashSet<>();

        for(TipoPokemon tipo : this.tipos) {

            switch (tipo) {

                case Grass :
                    this.resistencias.add(TipoPokemon.Ground);
                    this.resistencias.add(TipoPokemon.Water);
                    this.resistencias.add(TipoPokemon.Grass);
                    this.resistencias.add(TipoPokemon.Electric);
                    break;

                case Dragon:
                    this.resistencias.add(TipoPokemon.Fire);
                    this.resistencias.add(TipoPokemon.Water);
                    this.resistencias.add(TipoPokemon.Grass);
                    this.resistencias.add(TipoPokemon.Electric);
                    break;

                case Ice:
                    this.resistencias.add(TipoPokemon.Ice);
                    break;

                case Fighting:
                    this.resistencias.add(TipoPokemon.Rock);
                    this.resistencias.add(TipoPokemon.Bug);
                    break;

                case Fire:
                    this.resistencias.add(TipoPokemon.Bug);
                    this.resistencias.add(TipoPokemon.Fire);
                    this.resistencias.add(TipoPokemon.Grass);
                    break;

                case Flying:
                    this.resistencias.add(TipoPokemon.Fighting);
                    this.resistencias.add(TipoPokemon.Bug);
                    this.resistencias.add(TipoPokemon.Grass);
                    break;

                case Ghost:
                    this.resistencias.add(TipoPokemon.Poison);
                    this.resistencias.add(TipoPokemon.Bug);
                    break;

                case Ground:
                    this.resistencias.add(TipoPokemon.Poison);
                    this.resistencias.add(TipoPokemon.Rock);
                    break;

                case Electric:
                    this.resistencias.add(TipoPokemon.Flying);
                    this.resistencias.add(TipoPokemon.Electric);
                    break;

                case Normal:
                    break;

                case Poison:
                    this.resistencias.add(TipoPokemon.Fighting);
                    this.resistencias.add(TipoPokemon.Poison);
                    this.resistencias.add(TipoPokemon.Grass);
                    break;

                case Psychic:
                    this.resistencias.add(TipoPokemon.Fighting);
                    break;

                case Rock:
                    this.resistencias.add(TipoPokemon.Normal);
                    this.resistencias.add(TipoPokemon.Flying);
                    this.resistencias.add(TipoPokemon.Poison);
                    this.resistencias.add(TipoPokemon.Fire);
                    break;

                case Water:
                    this.resistencias.add(TipoPokemon.Fire);
                    this.resistencias.add(TipoPokemon.Water);
                    this.resistencias.add(TipoPokemon.Ice);
                    break;

                default:
                    break;

            }

        }

    }

    private void definirFraquezas() {

        this.fraquezas = new HashSet<>();

        for(TipoPokemon tipo : this.tipos) {

            switch (tipo) {

                case Grass :
                    this.fraquezas.add(TipoPokemon.Flying);
                    this.fraquezas.add(TipoPokemon.Poison);
                    this.fraquezas.add(TipoPokemon.Bug);
                    this.fraquezas.add(TipoPokemon.Fire);
                    this.fraquezas.add(TipoPokemon.Grass);
                    this.fraquezas.add(TipoPokemon.Dragon);
                    break;

                case Dragon:
                    break;

                case Ice:
                    this.fraquezas.add(TipoPokemon.Fire);
                    this.fraquezas.add(TipoPokemon.Water);
                    break;

                case Fighting:
                    this.fraquezas.add(TipoPokemon.Flying);
                    this.fraquezas.add(TipoPokemon.Poison);
                    this.fraquezas.add(TipoPokemon.Psychic);
                    this.fraquezas.add(TipoPokemon.Bug);
                    this.fraquezas.add(TipoPokemon.Ghost);
                    break;

                case Fire:
                    this.fraquezas.add(TipoPokemon.Water);
                    this.fraquezas.add(TipoPokemon.Rock);
                    this.fraquezas.add(TipoPokemon.Dragon);
                    break;

                case Flying:
                    this.fraquezas.add(TipoPokemon.Rock);
                    this.fraquezas.add(TipoPokemon.Electric);
                    break;

                case Ghost:
                    break;

                case Ground:
                    this.fraquezas.add(TipoPokemon.Flying);
                    this.fraquezas.add(TipoPokemon.Bug);
                    this.fraquezas.add(TipoPokemon.Grass);
                    break;

                case Electric:
                    this.fraquezas.add(TipoPokemon.Grass);
                    this.fraquezas.add(TipoPokemon.Ground);
                    this.fraquezas.add(TipoPokemon.Dragon);
                    break;

                case Normal:
                    this.fraquezas.add(TipoPokemon.Rock);
                    this.fraquezas.add(TipoPokemon.Ghost);
                    break;

                case Poison:
                    this.fraquezas.add(TipoPokemon.Ground);
                    this.fraquezas.add(TipoPokemon.Rock);
                    this.fraquezas.add(TipoPokemon.Ghost);
                    break;

                case Psychic:
                    break;

                case Rock:
                    this.fraquezas.add(TipoPokemon.Water);
                    this.fraquezas.add(TipoPokemon.Fighting);
                    this.fraquezas.add(TipoPokemon.Ground);
                    this.fraquezas.add(TipoPokemon.Grass);
                    break;

                case Water:
                    this.fraquezas.add(TipoPokemon.Electric);
                    this.fraquezas.add(TipoPokemon.Grass);
                    break;

                default:
                    break;

            }

        }



    }

    public String getNome() {
        return nome;
    }

    public int getLevel() {
        return level;
    }

    public int getPontosDeVida() {
        return pontosDeVida;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public ArrayList<TipoPokemon> getTipo() {
        return tipos;
    }

    public ArrayList<PokemonMove> getAtaques() {
        return ataques;
    }

    public void setTipos(ArrayList<TipoPokemon> tipos) {
        this.tipos = tipos;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getAgilidade() { return agilidade; }

    public int getEspecial() { return especial; }

    public void inflingirDano(int dano) {
        this.pontosDeVida -= dano;
    }

    public void recuperar(int pontos){
        this.pontosDeVida += pontos;
    }

    public int getPokedexNumber() {
        return pokedexNumber;
    }

    public void addStatus(PokemonStatus status) {
        this.status.add(status);
    }

    public void removeStatus(PokemonStatus status) {
        this.status.remove(status);
    }

    public boolean hasStatus(PokemonStatus status){
        if(this.status.contains(status))
            return true;
        else
            return false;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void setAgilidade(int agilidade) {
        this.agilidade = agilidade;
    }

    public void setEspecial(int especial) {
        this.especial = especial;
    }

    public PokemonMove getLastMove() {
        return lastMove;
    }

    public void setLastMove(PokemonMove lastMove) {
        this.lastMove = lastMove;
    }
}
