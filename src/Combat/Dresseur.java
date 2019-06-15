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

import Pokemon.Pokemon;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Dresseur {
    protected String _nom;
    protected Pokemon _pkmnActuel = null;
    protected final Pokemon[] _equipe = new Pokemon[6];
    protected Enum_Action _actionVoulue;
    protected int _infoAction;

    public Dresseur(String _nom) {
        this._nom = _nom;
        _actionVoulue = null;
        _infoAction = -1;
    }

    public String getNom() {
        return _nom;
    }

    public Pokemon[] getEquipe() {
        return _equipe;
    }
    
    private int checkEmplacement(int emplacement) throws Exception{
        if(emplacement < 0 || emplacement > 5) throw new Exception("L'emplacement d'un pokémon est compris entre 0 et 5");
        return emplacement;
    }
    
    public final Pokemon getPokemon(int emplacement) throws Exception{
        checkEmplacement(emplacement);
        return _equipe[emplacement];
    }
    
    public final Pokemon getPokemonActuel(){
        return _pkmnActuel;
    }
    
    public final void setPokemon(int idPokemon, int emplacement) throws Exception{
        checkEmplacement(emplacement);
        _equipe[emplacement] = new Pokemon(idPokemon);
    }
    
    public final void setCapacitePokemon(int emplacementEquipe, int idCapacite, int emplacementCapa) throws Exception{
        checkEmplacement(emplacementEquipe);
        _equipe[emplacementEquipe].setCapacite(idCapacite, emplacementCapa);
    }
    
    public final Enum_Action getAction(){
        return _actionVoulue;
    }
    
    public final int getInfoAction(){
        return _infoAction;
    }
    
    public final boolean isOut(){
        int i = 0;
        while(i < 6){
            if(!_equipe[i].isKO()) return false;
            i++;
        }
        return true;
    }
    
    public final boolean canStart(){
        boolean res = true;
        int i = 0, j;
        while(i < 6 && res){
            res &= _equipe[i] != null;
            j = 0;
            while(j < 4 && res){
                try {
                    _equipe[i].getCapacite(j);
                    j++;
                } catch (Exception ex) {
                    res = false;
                }
            }
            i++;
        }
        return res;
    }
    
    public final String debuterCombat(){
        String res = "";
        try {
            return changerPokemon(0);
        } catch (Exception ex) {
            System.err.println("Ceci ne doit jamais s'afficher [debuterCombat]");
        }
        return res;
    }
    
    public final String changerPokemon(int emplacement) throws Exception{
        String res = _nom;
        if(_pkmnActuel != null){
                if(_pkmnActuel.attaqueEnCours()) throw new Exception("Le pokémon est en train d'attaquer");
                _pkmnActuel.retourSac();
                res = _nom + " retire " + _pkmnActuel.getNom() + " et";
        }
        if(getPokemon(emplacement).isKO()) throw new Exception("Le pokémon est KO !");
        _pkmnActuel = getPokemon(emplacement);
        res += " envoie " + _pkmnActuel.getNom() + " au combat.";
        return res;
    }

    @Override
    public final String toString() {
        String res = _nom;
        for(Pokemon p : _equipe){
            if(p == null) continue;
            String type = "[" + p.getType()[0].toString() + (p.getType()[1] != null ? "/" + p.getType()[1].toString():"") + "]";
            res += "\n\t" + p.getNom() + "\t" + (p.getNom().length() < 8 ? "\t":"") + type + "\t"
                + (type.length() < 8 ? "\t":"") + p.getVie() + "/" + p.getVieMax() + " PV";
        }
        return res;
    }
    
    public int getNbPokemonRestant(){
        int res = 0;
        for(int i = 0; i < 6; i++){
            if(_equipe[i] != null && !_equipe[i].isKO()) res++;
        }
        return res;
    }
    
}
