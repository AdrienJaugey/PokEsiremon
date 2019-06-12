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
import Pokemon.Capacite.EffetCapacite.EffetSpecial;
import Pokemon.Capacite.EffetCapacite.EffetStatistique;
import Pokemon.Capacite.EffetCapacite.EffetStatut;
import static Pokemon.Capacite.EffetCapacite.Enum_EffetSpeciaux.*;
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
    public static final int NB_CAPACITE = 100;
    private static Pokedex _instance;
    private final String[] _pkmnNom;
    private final Enum_TypePokemon[][] _pkmnType;
    private final int[][] _pkmnBaseStat;
    private final ArrayList<Integer>[] _pkmnCapacite;
    private final Capacite[] _capacite;
    
    /**
     * Permet d'instancier et remplir le Pokédex
     */
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
            Scanner scan = new Scanner(getClass().getResourceAsStream("../Ressources/Donnees/pokemon_base_stats.csv"));
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
            Document xml = builder.parse(getClass().getResourceAsStream("../Ressources/Donnees/pokemon_moves.xml"));
            NodeList capacites = xml.getDocumentElement().getChildNodes();
            Node capacite;
            for(i = 0; i < capacites.getLength() - 1; i++){
                capacite = capacites.item(i);
                if (capacite.getNodeName().equals("capacite")) {
                    
                    int id = Integer.parseInt(capacite.getAttributes().item(0).getNodeValue());
                    String nomCapa = "";
                    Enum_TypePokemon type = null;
                    int puissance = 0, 
                        precision = 100, 
                        pp = 0,
                        nbFrappeMin = 1,
                        nbFrappeMax = 1;
                    boolean boostCrit = false;
                    ArrayList<Effet> effetsCapa = new ArrayList<>();
                    
                    NodeList infos = capacite.getChildNodes();
                    for (int j = 1; j < infos.getLength(); j++) {
                        Node n = infos.item(j);
                        String nom = n.getNodeName();
                        switch (nom) {//On parcours les noeuds enfants, suivant leur nom, on met à jour la donnée correspondante
                            case "nom": nomCapa = n.getTextContent(); break;
                            case "type": type = Enum_TypePokemon.get(n.getTextContent()); break;
                            case "puissance": puissance = Integer.parseInt(n.getTextContent()); break;
                            case "precision": precision = Integer.parseInt(n.getTextContent()); break;
                            case "pp": pp = Integer.parseInt(n.getTextContent()); break;
                            case "boostCrit": boostCrit = true; break;
                            case "nbFrappe": 
                                nbFrappeMin = Integer.parseInt(n.getAttributes().item(0).getNodeValue()); 
                                nbFrappeMax = Integer.parseInt(n.getAttributes().item(1).getNodeValue());
                                break;
                            case "pokemons": //Dans ce cas, on récupère les id des pokémons pouvant utiliser la capacité
                                String content = n.getTextContent();
                                //Si on trouve une virgule, il y a plusieurs pokémons donc on split la chaîne, sinon on lit directement l'id
                                if (content.contains(",")) {
                                    String[] pkmns = content.split(",");
                                    for (String s : pkmns) { this._pkmnCapacite[Integer.parseInt(s)].add(id); }
                                } else _pkmnCapacite[Integer.parseInt(content)].add(id);
                                break;
                            case "effets": //Dans ce cas, on va ajouter des effets à la capacité
                                NodeList effets = n.getChildNodes();
                                for(int e = 0; e < effets.getLength(); e++){
                                    Node effet = effets.item(e);
                                    //On parcours les neuds "effet"
                                    if(effet.getNodeName().equals("effet")){
                                        Enum_Cible cible = null;
                                        int chance = 100;
                                        //Si on trouve un attribut, c'est la chance de réussite
                                        if(effet.getAttributes().item(0) != null) chance = Integer.parseInt(effet.getAttributes().item(0).getNodeValue());  
                                        
                                        //On parcours les noeuds enfants qui contiennent la cible et le statut OU la statistique à modifier
                                        NodeList effetInfos = effet.getChildNodes();
                                        for(int ei = 0; ei < effetInfos.getLength(); ei++){
                                            Node info = effetInfos.item(ei);
                                            //Si c'est le noeud "cible" on enregistre la cible (ce noeud apparaît forcément en premier)
                                            if(info.getNodeName().equals("cible")) cible = Enum_Cible.get(info.getTextContent());
                                            else if (info.getNodeName().equals("statut")){
                                                //Si on trouve le noeud "statut", on récupère le statut à affecter
                                                //On crée l'effet et on l'ajoute à la capacité
                                                Enum_Statut statut = Enum_Statut.get(info.getTextContent());
                                                effetsCapa.add(new EffetStatut(cible, chance, statut));
                                            }
                                            else if (info.getNodeName().equals("statistique")){
                                                //Sinon, si on tombe sur le noeud "statistique", on récupère la stat à modifier et de combien
                                                //On crée également l'effet et on l'ajoute à la capacité
                                                Enum_Statistique stat = Enum_Statistique.get(info.getTextContent());
                                                String modifierString = info.getAttributes().item(0).getTextContent();
                                                int delta = 0;
                                                double modifier = 0;
                                                if(modifierString.contains(".")) modifier = Double.parseDouble(modifierString);
                                                else delta = Integer.parseInt(modifierString);
                                                EffetStatistique effetStat = new EffetStatistique(cible, chance, stat, delta, modifier);
                                                effetsCapa.add(effetStat);
                                            } else if(info.getNodeName().equals("special")){
                                                switch (info.getTextContent().toLowerCase()) {
                                                    case "peur": effetsCapa.add(new EffetSpecial(cible, chance, PEUR, 0)); break;
                                                    case "clonage": effetsCapa.add(new EffetSpecial(cible, chance, CLONAGE, 0)); break;
                                                    case "no_status_change": effetsCapa.add(new EffetSpecial(cible, chance, NO_STATUS_CHANGE, 0)); break;
                                                    case "copie_type": effetsCapa.add(new EffetSpecial(cible, chance, COPIE_TYPE, 0)); break;
                                                    case "copie_capacite": effetsCapa.add(new EffetSpecial(cible, chance, COPIE_CAPACITE, 0)); break;
                                                    case "contrecoup": effetsCapa.add(new EffetSpecial(cible, chance, CONTRECOUP, Double.parseDouble(info.getAttributes().item(0).getTextContent()))); break;
                                                    default: System.out.println("[" + id + "] A implémenter : " + info.getTextContent());
                                                }
                                            }
                                        }
                                    }
                                }
                                break;
                            default: break;
                        }                        
                    }
                    _capacite[id] = new Capacite(nomCapa, type, puissance, precision, pp, boostCrit, nbFrappeMin, nbFrappeMax);
                    for(Effet effet : effetsCapa)
                        _capacite[id].addEffet(effet);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        } finally {
            
        }
    }
    
    /**
     * Permet de récupérer l'instance du Pokedex
     * @return l'instance
     */
    public static Pokedex get(){
        if(_instance == null) _instance = new Pokedex();
        return _instance;
    }
    
    /**
     * Permet de récupérer le nom du pokémon suivant son id
     * @param id l'id du pokémon en question
     * @return le nom du pokémon
     */
    public String getNomPkmn(int id){
        return _pkmnNom[id - 1];
    }
    
    /**
     * Permet de récupérer toutes les statistiques de base d'un pokémon selon 
     * son id
     * @param id l'id du pokémon en question
     * @return un tableau contenant les statistiques (Vie, Atq, Def, Atq Spe, DefSpe et Vit)
     */
    public int[] getBaseStatsPkmn(int id){
        return _pkmnBaseStat[id - 1];
    }
    
    /**
     * Permet de récupérer le(s) type(s) d'un pokémon grâce à son id
     * @param id l'id du pokémon en question
     * @return un tableau contenant le(s) type(s) du pokémon (le deuxième peut être null)
     */
    public Enum_TypePokemon[] getTypesPkmn(int id){
        return _pkmnType[id - 1];
    }
    
    /**
     * Permet de récupérer la liste des capacités que peut utiliser un pokémon
     * grâce à son id
     * @param id l'id du pokémon en question
     * @return une ArrayList contenant les id des capacités
     */
    public ArrayList<Integer> getCapacitePokemon(int id){
        return _pkmnCapacite[id];
    }
    
    /**
     * permet de récupérer la capacité grâce à son id
     * @param id l'id de la capacité à récupérer
     * @return la capacité
     */
    public Capacite getCapacite(int id){
        return _capacite[id];
    }
}
