package magikards.main;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import magikards.main.data.Pokemon;
import magikards.main.data.PokemonMove;
import magikards.main.data.PokemonStatus;
import magikards.main.data.TipoPokemon;

import static android.content.ContentValues.TAG;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class MoveEffect {

    private PokemonMove move;
    private Pokemon pokemon;
    private Pokemon target;
    private Game game;
    private boolean moveWasSuccesful;


    public MoveEffect(Game game, PokemonMove move, Pokemon pokemon, Pokemon target) {
        this.move = move;
        this.pokemon = pokemon;
        this.target = target;
        this.game = game;
        this.moveWasSuccesful = false;
    }

    public void makeMove() {

        switch (move.getNome()) {
            case "absorb":
                absorb();
                break;

            case "explosion":
                explosion();
                break;

            case "acid armor":
                acidArmor();
                break;

            case "agility":
                agility();
                break;

            case "amnesia":
                amnesia();
                break;

            case "barrier":
                barrier();
                break;

            case "confuse ray":
                confuseRay();
                break;

            case "confusion":
                confusion();
                break;

            case "constrict":
                constrict();
                break;

            case "conversion":
                conversion();
                break;

            case "defense curl":
                defenseCurl();
                break;

            case "disable":
                disable();
                break;

            case "double-edge":
                doubleEdge();
                break;

            case "dragon rage":
                dragonRage();
                break;

            case "dream eater":
                dreamEater();
                break;

            case "ember":
                ember();
                break;

            case "fire blast":
                fireBlast();
                break;

            case "fire punch":
                firePunch();
                break;

            case "flamethrower":
                flamethrower();
                break;

            case "glare":
                glare();
                break;

            case "growl":
                growl();
                break;

            case "harden":
                harden();
                break;

            case "hypnosis":
                hypnosis();
                break;

            case "ice beam":
                iceBeam();
                break;

            case "ice punch":
                icePunch();
                break;

            case "jump kick":
                jumpKick();
                break;

            case "leech life":
                leechLife();
                break;

            case "leer":
                leer();
                break;

            case "lick":
                lick();
                break;

            case "lovely kiss":
                lovelyKiss();
                break;

            case "meditate":
                meditate();
                break;

            case "mega drain":
                megaDrain();
                break;

            case "metronome":
                metronome();
                break;

            case "mimic":
                mimic();
                break;

            case "mirror move":
                mirrorMove();
                break;

            case "night shade":
                nightShade();
                break;

            case "poison gas":
                poisonGas();
                break;

            case "poison powder":
                poisonPowder();
                break;

            case "poison string":
                poisonSting();
                break;

            case "psybeam":
                psybeam();
                break;

            case "psywave":
                psywave();
                break;

            case "recover":
                recover();
                break;

            case "screech":
                screech();
                break;

            case "seismic toss":
                seismicToss();
                break;

            case "self destruct":
                selfDestruct();
                break;

            case "sharpen":
                sharpen();
                break;

            case "sing":
                sing();
                break;

            case "sleep powder":
                sleepPowder();
                break;

            case "sludge":
                sludge();
                break;

            case "smog":
                smog();
                break;

            case "soft-boiled":
                softBoiled();
                break;

            case "sonic boom":
                sonicBoom();
                break;

            case "spore":
                spore();
                break;

            case "stun spore":
                stunSpore();
                break;

            case "submission":
                submission();
                break;

            case "super fang":
                superFang();
                break;

            case "supersonic":
                supersonic();
                break;

            case "swords dance":
                swordsDance();
                break;

            case "tail whip":
                tailWhip();
                break;

            case "take down":
                takeDown();
                break;

            case "thunder":
                thunder();
                break;

            case "thunder punch":
                thunderPunch();
                break;

            case "thunder shock":
                thunderShock();
                break;

            case "thunder wave":
                thunderwave();
                break;

            case "thunderbolt":
                thunderbolt();
                break;

            case "toxic":
                toxic();
                break;

            case "transform":
                transform();
                break;

            case "vine whip":
                vineWhip();
                break;

            case "vice grip":
                viceGrip();
                break;

            case "twineedle":
                twineedle();
                break;

            case "withdraw":
                withdraw();
                break;

            default:
                defaultEffect();
                break;
        }

    }

    private void withdraw() {
        if(moveWasSuccesful()){
            this.pokemon.setDefesa(this.pokemon.getDefesa() + 1);
            this.game.updateGameLog(this.pokemon.getNome() + "'s DEFENSE rose!");
        }
    }

    private void vineWhip() {
        if (moveWasSuccesful()) {
            this.target.inflingirDano(calculateDamage());
        }
    }

    private void viceGrip() {
        if (moveWasSuccesful()) {
            this.target.inflingirDano(calculateDamage());
        }
    }

    private void twineedle() {
        if(moveWasSuccesful()) {
            this.target.inflingirDano(calculateDamage() * 2);
            if (moveWasSuccesful(2)) {
                if (!this.target.hasStatus(PokemonStatus.Confused)) {
                    this.target.addStatus(PokemonStatus.Confused);
                    game.updateGameLog(this.target.getNome() + " is confused!");
                }
            }
        }
    }


    private void transform() {
        // removido do json
    }

    private void toxic() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                this.target.addStatus(PokemonStatus.Poisoned);
                game.updateGameLog(this.target.getNome() + " is poisoned!");
            }

        }
    }

    private void thunderbolt() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                    this.target.addStatus(PokemonStatus.Paralyzed);
                    game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
                }
            }
        }
    }

    private void thunderwave() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                this.target.addStatus(PokemonStatus.Paralyzed);
                game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
            }

        }
    }

    private void thunderShock() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                    this.target.addStatus(PokemonStatus.Paralyzed);
                    game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
                }
            }
        }
    }

    private void thunderPunch() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                    this.target.addStatus(PokemonStatus.Paralyzed);
                    game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
                }
            }
        }
    }

    private void thunder() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                    this.target.addStatus(PokemonStatus.Paralyzed);
                    game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
                }
            }
        }
    }

    private void takeDown() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano(damage/4);
            this.game.updateGameLog(this.pokemon.getNome() + "'s hit with recoil!");
        }
    }

    private void tailWhip() {
        if(moveWasSuccesful()){
            this.target.setDefesa(this.target.getDefesa() - 1);
            this.game.updateGameLog(this.target.getNome() + "'s DEFENSE fell!");

        }
    }

    private void swordsDance() {
        if(moveWasSuccesful()){
            this.pokemon.setAtaque(this.pokemon.getAtaque() + 2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s ATTACK greatly rose!");

        }
    }

    private void supersonic() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Confused)){
                this.target.addStatus(PokemonStatus.Confused);
                game.updateGameLog(this.target.getNome() + " is confused!");
            }
        }
    }

    private void superFang() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(this.target.getPontosDeVida()/2);
        }
    }

    private void submission() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano(damage/2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s hit with recoil!");
        }
    }

    private void stunSpore() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                this.target.addStatus(PokemonStatus.Paralyzed);
                game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
            }
        }
    }

    private void spore() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Asleep)){
                this.target.addStatus(PokemonStatus.Asleep);
                game.updateGameLog(this.target.getNome() + " is fast asleep.");
            }

        }
    }

    private void sonicBoom() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(20);
        }
    }

    private void softBoiled() {
        if(moveWasSuccesful()){
            Random generator = new Random();
            int recover = generator.nextInt(50) + 25;
            this.pokemon.inflingirDano(recover * (-1));
            this.game.updateGameLog(this.pokemon.getNome() + " regained health!");

        }
    }

    private void smog() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(3)){
                if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                    this.target.addStatus(PokemonStatus.Poisoned);
                    game.updateGameLog(this.target.getNome() + " is hurt by poison!");
                }
            }
        }
    }

    private void sludge() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(3)){
                if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                    this.target.addStatus(PokemonStatus.Poisoned);
                    game.updateGameLog(this.target.getNome() + " is hurt by poison!");
                }
            }

        }
    }

    private void sleepPowder() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Asleep)){
                this.target.addStatus(PokemonStatus.Asleep);
                game.updateGameLog(this.target.getNome() + " is fast asleep.");
            }
        }
    }

    private void sing() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Asleep)){
                this.target.addStatus(PokemonStatus.Asleep);
                game.updateGameLog(this.target.getNome() + " is fast asleep.");
            }
        }
    }

    private void sharpen() {
        if(moveWasSuccesful()){
            this.pokemon.setAtaque(this.pokemon.getAtaque() + 1);
            this.game.updateGameLog(this.pokemon.getNome() + "'s ATTACK rose!");

        }
    }

    private void selfDestruct() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            this.pokemon.inflingirDano(this.pokemon.getPontosDeVida());

        }
    }

    private void seismicToss() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(this.target.getLevel());
        }
    }

    private void screech() {
        if(moveWasSuccesful()){
            this.target.setDefesa(this.target.getDefesa() - 2);
            this.game.updateGameLog(this.target.getNome() + "'s DEFENSE fell!");
        }
    }

    private void recover() {
        if(moveWasSuccesful()){
            Random generator = new Random();
            int recover = generator.nextInt(50) + 25;
            this.pokemon.inflingirDano(recover * (-1));
            this.game.updateGameLog(this.pokemon.getNome() + " regained health!");

        }
    }

    private void psywave() {
        if(moveWasSuccesful()){
            Random generator = new Random();
            int damage = generator.nextInt(150) + 50;
            this.target.inflingirDano(damage);
        }
    }

    private void psybeam() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Confused)){
                    this.target.addStatus(PokemonStatus.Confused);
                    game.updateGameLog(this.target.getNome() + " is confused!");
                }
            }
        }
    }

    private void poisonSting() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(3)){
                if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                    this.target.addStatus(PokemonStatus.Poisoned);
                    game.updateGameLog(this.target.getNome() + " is hurt by poison!");
                }
            }
        }
    }

    private void poisonPowder() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                this.target.addStatus(PokemonStatus.Poisoned);
                game.updateGameLog(this.target.getNome() + " is hurt by poison!");
            }
        }
    }

    private void poisonGas() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Poisoned)) {
                this.target.addStatus(PokemonStatus.Poisoned);
                game.updateGameLog(this.target.getNome() + " is hurt by poison!");
            }
        }
    }

    private void nightShade() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(this.target.getLevel());

        }
    }

    private void mirrorMove() {
        // removido do json
    }

    private void mimic() {
        // removido do json
    }

    private void metronome() {
        // removido do json
    }

    private void megaDrain() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano((damage/2) * (-1));
            this.game.updateGameLog("Sucked health from " + this.target.getNome() + "!");
        }
    }

    private void meditate() {
        if(moveWasSuccesful()){
            this.pokemon.setAtaque(this.pokemon.getAtaque() + 1);
            this.game.updateGameLog(this.pokemon.getNome() + "'s ATTACK rose!");

        }
    }

    private void lovelyKiss() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Asleep)){
                this.target.addStatus(PokemonStatus.Asleep);
                game.updateGameLog(this.target.getNome() + " is fast asleep.");
            }
        }
    }

    private void lick() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(3)){
                if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                    this.target.addStatus(PokemonStatus.Paralyzed);
                    game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
                }
            }
        }
    }

    private void leer() {
        if(moveWasSuccesful()){
            this.target.setDefesa(this.target.getDefesa() - 1);
            this.game.updateGameLog(this.target.getNome() + "'s DEFENSE fell!");
        }
    }

    private void leechLife() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano((damage/2) * (-1));
            this.game.updateGameLog("Sucked health from " + this.target.getNome() + "!");
        }
    }

    private void jumpKick() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
        }
        else {
            this.pokemon.inflingirDano(this.pokemon.getMaxHP()/2);
        }
    }

    private void icePunch() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
        }
    }

    private void iceBeam() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
        }
    }

    private void hypnosis() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Asleep)){
                this.target.addStatus(PokemonStatus.Asleep);
                game.updateGameLog(this.target.getNome() + " is fast asleep.");
            }
        }
    }

    private void harden() {
        if(moveWasSuccesful()){
            this.pokemon.setDefesa(this.pokemon.getDefesa() + 1);
            this.game.updateGameLog(this.pokemon.getNome() + "'s DEFENSE rose!");

        }
    }

    private void growl() {
        if(moveWasSuccesful()){
            this.target.setAtaque(this.target.getAtaque() - 1);
            this.game.updateGameLog(this.target.getNome() + "'s ATTACK fell!");
        }
    }

    private void glare() {
        if(moveWasSuccesful()){
            if(!this.target.hasStatus(PokemonStatus.Paralyzed)) {
                this.target.addStatus(PokemonStatus.Paralyzed);
                game.updateGameLog(this.target.getNome() + " is paralyzed!\nIt can't move!");
            }
        }
    }

    private void flamethrower() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Burned)) {
                    this.target.addStatus(PokemonStatus.Burned);
                    this.target.inflingirDano(this.target.getMaxHP()/16);
                    game.updateGameLog(this.target.getNome() + " is hurt by it's burn!");
                }
            }
        }
    }

    private void firePunch() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Burned)) {
                    this.target.addStatus(PokemonStatus.Burned);
                    this.target.inflingirDano(this.target.getMaxHP()/16);
                    game.updateGameLog(this.target.getNome() + " is hurt by it's burn!");
                }
            }
        }
    }

    private void fireBlast() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Burned)) {
                    this.target.addStatus(PokemonStatus.Burned);
                    this.target.inflingirDano(this.target.getMaxHP()/16);
                    game.updateGameLog(this.target.getNome() + " is hurt by it's burn!");
                }
            }
        }
    }

    private void ember() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            if(moveWasSuccesful(1)){
                if(!this.target.hasStatus(PokemonStatus.Burned)) {
                    this.target.addStatus(PokemonStatus.Burned);
                    this.target.inflingirDano(this.target.getMaxHP()/16);
                    game.updateGameLog(this.target.getNome() + " is hurt by it's burn!");
                }
            }
        }
    }

    private void dreamEater() {
        if(moveWasSuccesful()){
            if(this.target.hasStatus(PokemonStatus.Asleep)){
                int damage = calculateDamage();
                this.target.inflingirDano(damage);
                this.pokemon.inflingirDano((damage/2) * (-1));
                this.game.updateGameLog(this.target.getNome() + "'s dream was eaten!");
            }
        }
    }

    private void dragonRage() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(40);
        }
    }

    private void doubleEdge() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano(damage/3);
            this.game.updateGameLog(this.pokemon.getNome() + "'s hit with recoil!");
        }
    }

    private void disable() {
        if(moveWasSuccesful()){
            this.target.getLastMove().setDisabled(true);
        }
    }

    private void defenseCurl() {
        if(moveWasSuccesful()){
            this.pokemon.setDefesa(this.pokemon.getDefesa() + 1);
            this.game.updateGameLog(this.pokemon.getNome() + "'s DEFENSE rose!");

        }
    }

    private void conversion() {
        if(moveWasSuccesful()){
            ArrayList<TipoPokemon> novoTipo = new ArrayList<>();
            novoTipo.add(this.target.getTipo().get(0));
            this.pokemon.setTipos(novoTipo);

        }
    }

    private void constrict() {
        if(moveWasSuccesful(1)){
            this.target.setAgilidade(this.target.getAgilidade() - 1);
            this.game.updateGameLog(this.target.getNome() + "'s SPEED fell!");
        }
    }

    private void confusion() {
        if(moveWasSuccesful(1)) {
            this.target.addStatus(PokemonStatus.Confused);
            game.updateGameLog(this.target.getNome() + " is confused!");
        }
    }

    private void confuseRay() {
        if(moveWasSuccesful()){
            this.target.addStatus(PokemonStatus.Confused);
            game.updateGameLog(this.target.getNome() + " is confused!");

        }
    }

    private void barrier() {
        if(moveWasSuccesful()){
            this.pokemon.setDefesa(this.pokemon.getDefesa() + 2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s DEFENSE greatly rose!");

        }
    }

    private void amnesia() {
        if(moveWasSuccesful()){
            this.pokemon.setEspecial(this.pokemon.getEspecial() + 2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s SPECIAL greatly rose!");
        }
    }

    private void agility() {
        if(moveWasSuccesful()){
            this.pokemon.setAgilidade(this.pokemon.getAgilidade() + 2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s SPEED greatly rose!");

        }
    }

    private void acidArmor() {
        if(moveWasSuccesful()){
            this.pokemon.setDefesa(this.pokemon.getDefesa() + 2);
            this.game.updateGameLog(this.pokemon.getNome() + "'s DEFENSE greatly rose!");
        }
    }

    private void explosion() {
        if(moveWasSuccesful()){
            this.target.inflingirDano(calculateDamage());
            this.pokemon.inflingirDano(this.pokemon.getPontosDeVida());
        }
    }

    private void absorb() {
        if(moveWasSuccesful()){
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
            this.pokemon.inflingirDano((damage/2) * (-1));
            this.game.updateGameLog("Sucked health from " + this.target.getNome() + "!");
        }
    }

    private void defaultEffect() {

        if(moveWasSuccesful()) {
            int damage = calculateDamage();
            this.target.inflingirDano(damage);
        }

    }

    private int calculateDamage() {

        int pokemonAttack = this.pokemon.getAtaque();
        if(this.pokemon.hasStatus(PokemonStatus.Burned))
            pokemonAttack = pokemonAttack / 2;

        int damage = (((((2 * this.pokemon.getLevel()) / 5) + 2) * this.move.getForca() * (pokemonAttack/this.target.getDefesa())) / 50) + 2;

        if(move.isEffective(target.getFraquezas()))
            damage *= 2;
        else if(move.isLessEffective(target.getResistencias()))
            damage /= 2;

        return damage;
    }

    public boolean moveWasSuccesful() {

        boolean moveWasSuccesful = false;
        Random numberGenerator = new Random();
        if(move.getAcuracia() > 0.0) {
            int gen = (numberGenerator.nextInt(10) + 1);
            moveWasSuccesful = gen <= move.getAcuracia() * 10;
            this.moveWasSuccesful = moveWasSuccesful;
            if(moveWasSuccesful) {
                return true;
            }
        }
        else{
            this.moveWasSuccesful = true;
            return true;
        }

        return false;

    }

    private boolean moveWasSuccesful(int accuracy) {

        boolean moveWasSuccesful;
        Random numberGenerator = new Random();
        if(move.getAcuracia() > 0.0) {
            int gen = (numberGenerator.nextInt(10 - (10 -accuracy)) + 1);
            moveWasSuccesful = gen <= move.getAcuracia() * 10;
            if(moveWasSuccesful) {
                return true;
            }
        }
        else{
            return true;
        }

        return false;

    }

    public boolean wasMoveSuccesful() {
        return this.moveWasSuccesful;
    }
}
