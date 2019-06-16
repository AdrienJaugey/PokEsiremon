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
package PokEsiremon.Combat;

import PokEsiremon.Capacite.Capacite;
import static PokEsiremon.Combat.Enum_Action.*;
import static PokEsiremon.Pokemon.Enum_Statut.*;
import PokEsiremon.Pokemon.Enum_TypePokemon;
import static PokEsiremon.Pokemon.Enum_TypePokemon.*;
import PokEsiremon.Pokemon.Utils;
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
        genererEquipeAlea();
    }
    
    public void setAdversaire(Dresseur d){
        _j1 = d;
    }
    
    public void choixActionSuivante(){
        boolean switchPossible = switchPossible();
        _actionVoulue = ATTAQUER;
        if(!_pkmnActuel.attaqueEnCours() && switchPossible) {
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
                return choisir(aChoisir);
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
                return choisir(aChoisir2);
            } else if(!aChoisir1.isEmpty()){ //Si toujours pas de capacité, on choisit parmi les efficaces
                return choisir(aChoisir1);
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
                return choisir(aChoisir);
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
            return choisir(aChoisir);
        }
        return 0;
    }
    
    private double getModifier(Enum_TypePokemon[] adversaire, Enum_TypePokemon capa){
        double res = 1;
        res *= adversaire[0].dmgModifier(capa);
        if(adversaire[1] != null) res *= adversaire[1].dmgModifier(capa);
        return res;
    }
    
    private <T> T choisir(ArrayList<T> liste){
        return liste.get((int) Math.floor(Math.random() * liste.size()));
    }
    
    /**
     * Permet de choisir le pokémon avec lequel l'ordi va changer
     * @return l'emplacement du pokémon a envoyer au combat
     */
    private int choixSwitch(){
        return choisir(switchPossibles());
    }

    
    
}
