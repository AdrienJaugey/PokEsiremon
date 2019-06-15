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
    private String _nom;
    private Pokemon _pkmnActuel = null;
    private final Pokemon[] _equipe = new Pokemon[6];

    public Dresseur(String _nom) {
        this._nom = _nom;
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
    
    public Pokemon getPokemon(int emplacement) throws Exception{
        checkEmplacement(emplacement);
        return _equipe[emplacement];
    }
    
    public void setPokemon(int idPokemon, int emplacement) throws Exception{
        checkEmplacement(emplacement);
        _equipe[emplacement] = new Pokemon(idPokemon);
    }
    
    public void setCapacitePokemon(int emplacementEquipe, int idCapacite, int emplacementCapa) throws Exception{
        checkEmplacement(emplacementEquipe);
        _equipe[emplacementEquipe].setCapacite(idCapacite, emplacementCapa);
    }
    
    public boolean isOut(){
        int i = 0;
        while(i < 6){
            if(!_equipe[i].isKO()) return false;
            i++;
        }
        return true;
    }
    
    public boolean canStart(){
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
    
    public String debuterCombat(){
        String res = "";
        try {
            return changerPokemon(0);
        } catch (Exception ex) {
            System.err.println("Ceci ne doit jamais s'afficher [debuterCombat]");
        }
        return res;
    }
    
    public String changerPokemon(int emplacement) throws Exception{
        String res = "";
        if(_pkmnActuel != null){
                if(_pkmnActuel.attaqueEnCours()) throw new Exception("Le pokémon est en train d'attaquer");
                _pkmnActuel.retourSac();
                res = _nom + " retire " + _pkmnActuel.getNom() + " du combat.\n";
        }
        if(getPokemon(emplacement).isKO()) throw new Exception("Le pokémon est KO !");
        _pkmnActuel = getPokemon(emplacement);
        res += _nom + " envoie " + _pkmnActuel.getNom() + " au combat.";
        return res;
    }

    @Override
    public String toString() {
        String res = _nom;
        for(Pokemon p : _equipe){
            if(p == null) continue;
            res += "\n\t" + p.getNom() + " [" + p.getType()[0].toString() 
                + (p.getType()[1] != null ? "/" + p.getType()[1].toString():"") + "]"
                + p.getVie() + "/" + p.getVieMax() + " PV";
        }
        return res;
    }
    
    
    
}
