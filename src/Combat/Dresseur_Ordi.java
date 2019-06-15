/*
 * Copyright (C) 2019 AdrienJaugey <a.jaugey@gmail.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package Combat;

import static Combat.Enum_Action.*;
import Pokemon.Capacite.Capacite;
import static Pokemon.Enum_Statut.*;
import Pokemon.Enum_TypePokemon;
import static Pokemon.Enum_TypePokemon.*;
import Pokemon.Pokedex;
import Pokemon.Pokemon;
import Pokemon.Utils;
import java.util.ArrayList;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Dresseur_Ordi extends Dresseur {
    private final String nomPossibles[] = {"Topdresseur", "Topdresseuse", "Scout", "Scientifique", "Montagnard"};
    private Dresseur _j1;
    private final int CHANCE_SWITCH_VAMPIGRAINE = 50;
    private final int CHANCE_SWITCH_BASE = 5;
    
    private final int CHANCE_CAPA_FEU_GEL = 50;
    private final int CHANCE_CAPA_SOIN60 = 30;
    private final int CHANCE_CAPA_SOIN30 = 60;
    private final int CHANCE_CAPA_EFFICACE = 20;
   
    public Dresseur_Ordi() {
        super("");
        _nom = nomPossibles[(int)Math.floor(Math.random() * (nomPossibles.length))];
        Pokedex pkdx = Pokedex.get();
        for(int pkmn = 0; pkmn < 6; pkmn++){
            int idPkmn = 1 + (int)Math.floor(Math.random() * Pokedex.NB_POKEMON);
            _equipe[pkmn] = new Pokemon(idPkmn);
            ArrayList<Integer> listeCapa = (ArrayList<Integer>) pkdx.getCapacitePokemon(idPkmn).clone();
            for(int capa = 0; capa < 4; capa++){
                int idCapa = listeCapa.get((int)Math.floor(Math.random() * listeCapa.size()));
                try {
                    _equipe[pkmn].setCapacite(idCapa, capa);
                    listeCapa.remove(Integer.valueOf(idCapa));
                } catch (Exception ex) { }
            }
        }
    }
    
    public void setAdversaire(Dresseur d){
        _j1 = d;
    }
    
    public void choixActionSuivante(){
        boolean switchPossible = switchPossible();
        _actionVoulue = ATTAQUER;
        if(switchPossible) {
            if(_pkmnActuel.isKO()){
                _actionVoulue = SWITCHER;
                _infoAction = choixSwitch();
            }
            if (_pkmnActuel.getStatut() == VAMPIGRAINE && Utils.chance(CHANCE_SWITCH_VAMPIGRAINE)) {
                _actionVoulue = SWITCHER;
            } else if(Utils.chance(CHANCE_SWITCH_BASE)) _actionVoulue = SWITCHER;
        }
        if(_actionVoulue == ATTAQUER) _infoAction = choixCapacite();
        else _infoAction = choixSwitch();
    }
    
    private int choixCapacite(){
        //Si le pokémon est gelé et que l'on veut utiliser de préférence une
        //capacité de type feu, on parcourt les capacités non bloquées
        if(_pkmnActuel.getStatut() == GEL && Utils.chance(CHANCE_CAPA_FEU_GEL)){
            ArrayList<Integer> aChoisir = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                try {
                    if(_pkmnActuel.capaciteBloquee(i)) continue;
                    Capacite capa = _pkmnActuel.getCapacite(i);
                    if(capa.getTypePkmn() == FEU){
                        aChoisir.add(i);
                    }
                } catch (Exception ex) { }
            }
            //Si on en a trouvé au moins une, on en tire une au hasard parmi toutes
            if(!aChoisir.isEmpty()){
                return aChoisir.get((int) Math.floor(Math.random() * aChoisir.size()));
            }
        }
        
        //Si l'on n'a pas trouvé de capacité et que l'on en veut une efficace
        if(Utils.chance(CHANCE_CAPA_EFFICACE)){
            ArrayList<Integer> aChoisir1 = new ArrayList<>();
            ArrayList<Integer> aChoisir2 = new ArrayList<>();
            Enum_TypePokemon typePkmnJ1[] = _j1.getPokemonActuel().getType();
            for(int i = 0; i < 4; i++){
                try {
                    if(_pkmnActuel.capaciteBloquee(i)) continue;
                    Capacite capa = _pkmnActuel.getCapacite(i);
                    double modifier = getModifier(typePkmnJ1, capa.getTypePkmn());
                    if(modifier >= 1){ //Si la capacité est efficace
                        aChoisir2.add(i);
                        if(modifier >= 2)aChoisir1.add(i); //Si la capacité est super ou très efficace
                    }
                } catch (Exception ex) { }
            }
            if(!aChoisir2.isEmpty()){ //On choisit en priorité dans les attaques super/très efficaces
                return aChoisir2.get((int) Math.floor(Math.random() * aChoisir2.size()));
            } else if(!aChoisir1.isEmpty()){ //Si toujours pas de capacité, on choisit parmi les efficaces
                return aChoisir1.get((int) Math.floor(Math.random() * aChoisir1.size()));
            }
        }
        
        //Si la vie du pokémon est en dessous de 30%, on cherche une capacité de soin
        //Ou si la vie est en dessous de 60% mais avec moins de fréquence
        int pourcentageVie = (int) Math.round(_pkmnActuel.getVie()*100. / _pkmnActuel.getVieMax());
        if((pourcentageVie <= 30 && Utils.chance(CHANCE_CAPA_SOIN30)) || (pourcentageVie  <= 60 && Utils.chance(CHANCE_CAPA_SOIN60))){
            ArrayList<Integer> aChoisir = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                try {
                    if(!_pkmnActuel.capaciteBloquee(i) && _pkmnActuel.getCapacite(i).soigne()){
                        aChoisir.add(i);
                    }
                } catch (Exception ex) { }
            }
            if(!aChoisir.isEmpty()){
                return aChoisir.get((int) Math.floor(Math.random() * aChoisir.size()));
            }
        }
        
        //Si on n'a pas encore choisi, on prend une capacité au hasard parmi celles
        //qui ne sont pas bloquées
        ArrayList<Integer> aChoisir = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            try {
                if(!_pkmnActuel.capaciteBloquee(i)) aChoisir.add(i);
            } catch (Exception ex) { }
        }
        if(!aChoisir.isEmpty()){
            return aChoisir.get((int) Math.floor(Math.random() * aChoisir.size()));
        }
        return 0;
    }
    
    private double getModifier(Enum_TypePokemon[] adversaire, Enum_TypePokemon capa){
        double res = 1;
        res *= adversaire[0].dmgModifier(capa);
        if(adversaire[1] != null) res *= adversaire[1].dmgModifier(capa);
        return res;
    }
    
    /**
     * Permet de choisir le pokémon avec lequel l'ordi va changer
     * @return l'emplacement du pokémon a envoyer au combat
     */
    private int choixSwitch(){
        ArrayList<Integer> possible = new ArrayList<>();
        int i = 0;
        while(i < 6){
            if(_equipe[i] != _pkmnActuel && _equipe[i] != null && !_equipe[i].isKO()) possible.add(i);
            i++;
        }
        return possible.get((int)Math.floor(Math.random() * possible.size()));
    }

    /**
     * Permet de savoir si le joueur peut changer de pokémon
     * @return true s'il peut, false sinon
     */
    private boolean switchPossible() {
        boolean res = false;
        int i = 0;
        while(i < 6 && !res){
            if(_equipe[i] != null && !_equipe[i].isKO()) res |= true;
            i++;
        }
        return res;
    }
    
}
