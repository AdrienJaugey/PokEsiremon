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
package Pokemon;

import Pokemon.Capacite.Capacite;
import Pokemon.Capacite.EffetCapacite.Effet;
import Pokemon.Capacite.EffetCapacite.EffetStatistique;
import Pokemon.Capacite.EffetCapacite.EffetStatut;
import Pokemon.Capacite.Enum_Cible;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author AdrienJaugey <a.jaugey@gmail.com>
 */
public class Pokedex {
    public static final int NB_POKEMON = 151;
    public static final int NB_CAPACITE = 2;
    private static Pokedex _instance;
    private final String[] _pkmnNom;
    private final Enum_TypePokemon[][] _pkmnType;
    private final int[][] _pkmnBaseStat;
    private final ArrayList<Integer>[] _pkmnCapacite;
    private final Capacite[] _capacite;
    
    private Pokedex(){
        _pkmnNom = new String[NB_POKEMON];
        _pkmnBaseStat = new int[NB_POKEMON][6];
        _pkmnType = new Enum_TypePokemon[NB_POKEMON][2];
        _pkmnCapacite = new ArrayList[NB_POKEMON];
        _capacite = new Capacite[NB_CAPACITE];
        
        /*******************************
        *   Importation des pokémons   *
        *******************************/
        String line = "";
        int i = 0;
        Enum_TypePokemon types[] = Enum_TypePokemon.values();
        try {
            Scanner scan = new Scanner(getClass().getResourceAsStream("pokemon_base_stats.csv"));
            while (scan.hasNextLine()) {
                String[] pkmn = scan.nextLine().split(";");
                _pkmnNom[i] = pkmn[1];
                _pkmnBaseStat[i][0] = Integer.parseInt(pkmn[2]);
                _pkmnBaseStat[i][1] = Integer.parseInt(pkmn[3]);
                _pkmnBaseStat[i][2] = Integer.parseInt(pkmn[4]);
                _pkmnBaseStat[i][3] = Integer.parseInt(pkmn[5]);
                _pkmnBaseStat[i][4] = Integer.parseInt(pkmn[6]);
                _pkmnBaseStat[i][5] = Integer.parseInt(pkmn[7]);
                _pkmnType[i][0] = types[Integer.parseInt(pkmn[8])];
                _pkmnType[i][1] = (Integer.parseInt(pkmn[9]) != -1 ? types[Integer.parseInt(pkmn[9])] : null);
                _pkmnCapacite[i] = new ArrayList<>();
                i++;
            }
            scan.close();
            
            /********************************
            *   Importation des Capacités   *
            ********************************/
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.parse(getClass().getResourceAsStream("Capacite/pokemon_moves.xml"));
            NodeList capacites = xml.getDocumentElement().getChildNodes();
            Node capacite;
            for(i = 0; i < capacites.getLength() - 1; i++){
                capacite = capacites.item(i);
                if (capacite.getNodeName().equals("capacite")) {
                    int id = Integer.parseInt(capacite.getAttributes().item(0).getNodeValue());
                    String nomCapa = "";
                    Enum_TypePokemon type = null;
                    int puissance = 0, precision = 0;
                    ArrayList<Effet> effetsCapa = new ArrayList<>();
                    NodeList infos = capacite.getChildNodes();
                    for (int j = 1; j < infos.getLength(); j++) {
                        Node n = infos.item(j);
                        String nom = n.getNodeName();
                        switch (nom) {
                            case "nom": nomCapa = n.getTextContent(); break;
                            case "type": type = Enum_TypePokemon.get(n.getTextContent()); break;
                            case "puissance": puissance = Integer.parseInt(n.getTextContent()); break;
                            case "precision": precision = Integer.parseInt(n.getTextContent()); break;
                            case "pokemons":
                                String content = n.getTextContent();
                                if (content.contains(",")) {
                                    String[] pkmns = content.split(",");
                                    for (String s : pkmns) { this._pkmnCapacite[Integer.parseInt(s)].add(id); }
                                } else _pkmnCapacite[Integer.parseInt(content)].add(id);
                                break;
                            case "effets":
                                NodeList effets = n.getChildNodes();
                                for(int e = 0; e < effets.getLength(); e++){
                                    Node effet = effets.item(e);
                                    if(effet.getNodeName().equals("effet")){
                                        NodeList effetInfos = effet.getChildNodes();
                                        Enum_Cible cible = null;
                                        for(int ei = 0; ei < effetInfos.getLength(); ei++){
                                            Node info = effetInfos.item(ei);
                                            if(info.getNodeName().equals("cible")) cible = Enum_Cible.get(info.getTextContent());
                                            else if (info.getNodeName().equals("statut")) effetsCapa.add(new EffetStatut(cible, Enum_Statut.get(info.getTextContent())));
                                            else if (info.getNodeName().equals("statistique")){
                                                Enum_Statistique stat = Enum_Statistique.get(info.getChildNodes().item(1).getTextContent());
                                                String modifierString = info.getChildNodes().item(3).getTextContent();
                                                int delta = 0;
                                                double modifier = 0;
                                                if(modifierString.contains(".")) modifier = Double.parseDouble(modifierString);
                                                else delta = Integer.parseInt(modifierString);
                                                EffetStatistique effetStat = new EffetStatistique(cible, stat, delta, modifier);
                                                effetsCapa.add(effetStat);
                                            }
                                        }
                                    }
                                }
                                break;
                            default: break;
                        }                        
                    }
                    _capacite[id] = new Capacite(nomCapa, type, puissance, precision);
                    for(Effet effet : effetsCapa)
                        _capacite[id].addEffet(effet);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            
        }
    }
    
    public static Pokedex get(){
        if(_instance == null) _instance = new Pokedex();
        return _instance;
    }
    
    public String getNomPkmn(int id){
        return _pkmnNom[id - 1];
    }
    
    public int[] getBaseStatsPkmn(int id){
        return _pkmnBaseStat[id - 1];
    }
    
    public Enum_TypePokemon[] getTypesPkmn(int id){
        return _pkmnType[id - 1];
    }
    
    public ArrayList<Integer> getCapacitePokemon(int id){
        return _pkmnCapacite[id];
    }
    
    public Capacite getCapacite(int id){
        return _capacite[id];
    }
}
