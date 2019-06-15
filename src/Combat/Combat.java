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

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Combat {
    private final Dresseur _joueurs[] = new Dresseur[2];
    private int _premier;
    private int _second;
    
    public Combat() {
        _joueurs[1] = new Dresseur_Ordi();
    }
    
    public void ajouterHumain(Dresseur d){
        _joueurs[0] = d;
        ((Dresseur_Ordi)_joueurs[1]).setAdversaire(d);
    }
    
    public Dresseur getDresseur(int id){
        return _joueurs[id];
    }
    
    public String lancerCombat() throws Exception{
        if(!_joueurs[0].canStart() || !_joueurs[1].canStart()) throw new Exception("Au moins un joueur n'est pas près");
        String res = _joueurs[0].debuterCombat();
        res += "\n" + _joueurs[1].debuterCombat();
        return res;
    }
    
    public String debutNouveauTour(){
        Enum_Action a1 = _joueurs[0].getAction(),
                    a2 = _joueurs[1].getAction();
        int info1 = _joueurs[0].getInfoAction(),
            info2 = _joueurs[1].getInfoAction();
        
        if(a1 == SWITCHER && _joueurs[0].getPokemonActuel().attaqueEnCours()) a1 = ATTAQUER;
        if(a2 == SWITCHER && _joueurs[1].getPokemonActuel().attaqueEnCours()) a2 = ATTAQUER;
        
        if(a1 != a2){
            _premier = (a1 == SWITCHER ? 0 : 1);      
            _second = (a1 == SWITCHER ? 1 : 0);
        } else {
            if(a1 == SWITCHER){
                _premier = 0;
                _second = 1;
            } else {
                String nomCapaJ1 = "", nomCapaJ2 = "";
                try {
                    nomCapaJ1 = _joueurs[0].getPokemonActuel().getCapacite(info1).getNom();
                    nomCapaJ2 = _joueurs[1].getPokemonActuel().getCapacite(info2).getNom();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
                //Soit la même capacité ou toutes les deux différentes de Vive Attaque
                if(nomCapaJ1.equals(nomCapaJ2) || (!nomCapaJ1.equals("Vive Attaque") && !nomCapaJ2.equals("Vive Attaque"))){
                    int vit1 = _joueurs[0].getPokemonActuel().getVitesse(),
                        vit2 = _joueurs[1].getPokemonActuel().getVitesse();
                    if(vit1 > vit2){
                        _premier = 0;
                        _second = 1;
                    } else if (vit2 > vit1){
                        _premier = 1;
                        _second = 0; 
                    } else {
                        _premier = (int)Math.round(Math.random());
                        _second = (_premier == 0 ? 1 : 0);
                    }
                }
            }
        }
        String res = _joueurs[0].getPokemonActuel().debutTour();
        return res + "\n" + _joueurs[1].getPokemonActuel().debutTour();
    }
    
    public String actionPremierJoueur(){
        return actionJoueur(_premier);
    }
    
    public String actionSecondJoueur(){
        return actionJoueur(_second);
    }
    
    private String actionJoueur(int joueur){
        Dresseur dresseur = _joueurs[joueur];
        String res = "";
        if(dresseur.getAction() == SWITCHER){
            try {
                res = dresseur.changerPokemon(dresseur.getInfoAction());
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        } else {
            res = dresseur.getPokemonActuel().utiliserCapacite(dresseur.getInfoAction(), _joueurs[(joueur == 0 ? 1 : 0)].getPokemonActuel()); 
        }
        return res;
    }
    
    public String changerKO(){
        String res = "";
        for(int i = 0; i < 2; i++){
            if(_joueurs[i] instanceof Dresseur_Ordi && _joueurs[i].getPokemonActuel().isKO()){
                ((Dresseur_Ordi)_joueurs[i]).choixActionSuivante();
                try {
                    res += _joueurs[i].changerPokemon(_joueurs[i].getInfoAction());
                } catch (Exception ex) { }
            }
        }
        return res;
    }
    
    public boolean pokemonKO(){
        return (_joueurs[0].getPokemonActuel().isKO() || _joueurs[1].getPokemonActuel().isKO());
    }
    
    public String terminerTour(){
        String res = _joueurs[_premier].getPokemonActuel().finTour();
        res += "\n" + _joueurs[_second].getPokemonActuel().finTour();
        res += "\n" + checkFin();
        return res;
    }
    
    private String checkFin(){
        if(_joueurs[0].isOut() != _joueurs[1].isOut()){
            if(_joueurs[0].isOut()){ //Le joueur a perdu
                return _joueurs[1].getNom() + " remporte le combat.";
            } else { //L'ordi a perdu
                return _joueurs[0].getNom() + " remporte le combat.";
            }
        } else if(_joueurs[0].isOut()){ //Les deux joueurs ont perdus
            return "Match nul";
        } else return "";
    }
    
    public boolean isDone(){
        return (_joueurs[0].isOut() || _joueurs[1].isOut());
    }
}
