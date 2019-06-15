/*
 * Copyright (C) 2019 AdrienJaugey.
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
package Pokemon;

/**
 *
 * @author AdrienJaugey
 */
public enum Enum_TypePokemon {
    NORMAL,
    FEU,
    EAU,
    PLANTE,
    ELECTRIK,
    GLACE,
    COMBAT,
    POISON,
    SOL,
    VOL,
    PSY,
    INSECTE,
    ROCHE,
    SPECTRE,
    DRAGON;
    
    /**
     * Permet d'obtenir un type
     * @param type le type sous forme de chaîne de caractères
     * @return le type
     */
    public static Enum_TypePokemon get(String type){
        return Enum_TypePokemon.valueOf(type.toUpperCase());
    }
    
    private final double modifier[][] = {
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 0, 1},                        //Normal
        {1, 0.5, 0.5, 2, 1, 2, 1, 1, 1, 1, 1, 2, 0.5, 1, 0.5},                  //Feu
        {1, 2, 0.5, 0.5, 1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 0.5},                    //Eau
        {1, 0.5, 2, 0.5, 1, 1, 1, 0.5, 2, 0.5, 1, 0.5, 2, 1, 0.5},              //Plante
        {1, 1, 2, 0.5, 0.5, 1, 1, 1, 0, 2, 1, 1, 1, 1, 0.5},                    //Electrik
        {1, 1, 0.5, 2, 1, 0.5, 1, 1, 2, 2, 1, 1, 1, 1, 2},                      //Glace
        {2, 1, 1, 1, 1, 2, 1, 0.5, 1, 0.5, 0.5, 0.5, 1, 0, 1},                  //Combat
        {1, 1, 1, 2, 1, 1, 1, 0.5, 0.5, 1, 1, 2, 0.5, 0.5, 1},                  //Poison
        {1, 2, 1, 0.5, 2, 1, 1, 2, 1, 0, 1, 0.5, 2, 1, 1},                      //Sol
        {1, 1, 1, 2, 0.5, 1, 2, 1, 1, 1, 1, 2, 0.5, 1, 1},                      //Vol
        {1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 0.5, 1, 1, 1, 1},                        //Psy
        {1, 0.5, 1, 2, 1, 1, 0.5, 2, 1, 0.5, 2, 1, 1, 0.5, 1},                  //Insecte
        {1, 0.5, 1, 1, 1, 2, 0.5, 1, 0.5, 2, 1, 2, 1, 1, 1},                    //Roche
        {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 2, 1},                          //Spectre
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2}                           //Dragon
    };
    
    /**
     * Permet d'obtenir le coefficient multiplicateur de dégats face à un autre type
     * @param attackType le type qui attaque
     * @return le coefficient multiplicateur
     */
    public double dmgModifier(Enum_TypePokemon attackType){
        return modifier[attackType.ordinal()][this.ordinal()];
    }
    
    private final String displayColor[] = {"A8A878", "F08030", "6890F0", "78C850", "F8D030", "98D8D8", "C03028", "A040A0", "E0C068", "A890F0", "F85888", "A8B820", "B8A038", "705898", "7038F8"};
    
    /**
     * Permet d'obtenir le code couleur d'un type
     * @return le code couleur au format RGB/Hexadecimal (ex : FF0000 pour Rouge)
     */
    public String getDisplayColor(){
        return displayColor[this.ordinal()];
    }
    
    /**
     * Affiche une description des faiblesses et avantages d'un type
     */
    public void descType(){
        String inefficace = "";
        String peuEfficace = "";
        String tresEfficace = "";
        for(Enum_TypePokemon t : Enum_TypePokemon.values()){
            double value = t.dmgModifier(this);
            if(value == 0) {
                if(!inefficace.equals("")) inefficace += ", ";
                inefficace += t.toString();
            } else if(value == 0.5) {
                if(!peuEfficace.equals("")) peuEfficace += ", ";
                peuEfficace += t.toString();
            } else if(value == 2) {
                if(!tresEfficace.equals("")) tresEfficace += ", ";
                tresEfficace += t.toString();
            } else if (value != 1){
                System.err.println(this.toString() + " -> " + t.toString());
            }
        }
        System.out.println("Le type " + this.toString() + " est :");
        if(!inefficace.equals("")) System.out.println("\t- Inefficace contre " + inefficace);
        if(!peuEfficace.equals("")) System.out.println("\t- Peu efficace contre " + peuEfficace);
        if(!tresEfficace.equals("")) System.out.println("\t- Très efficace contre " + tresEfficace);
    }
}
