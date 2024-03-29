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
package PokEsiremon.Pokemon;

import PokEsiremon.Capacite.Capacite;
import PokEsiremon.Capacite.EffetCapacite.Effet;
import PokEsiremon.Capacite.EffetCapacite.EffetSpecial;
import PokEsiremon.Capacite.EffetCapacite.EffetStatistique;
import PokEsiremon.Capacite.EffetCapacite.EffetStatut;
import static PokEsiremon.Capacite.EffetCapacite.Enum_EffetSpeciaux.*;
import PokEsiremon.Capacite.Enum_Cible;
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
public class Pokedex{
    public static final int NB_POKEMON = 151;
    public static final int NB_CAPACITE = 162;
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
        Scanner scan;
        int i = 0;
        Enum_TypePokemon types[] = Enum_TypePokemon.values();
        try {
            scan = new Scanner(Pokedex.class.getResourceAsStream("pokemon_base_stats.csv"));
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
            Document xml = builder.parse(Pokedex.class.getResourceAsStream("pokemon_moves.xml"));
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
                    boolean boostCrit = false,
                            tourAvant = false,
                            tourApres = false;
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
                            case "tourAvant": tourAvant = true; break;
                            case "tourApres": tourApres = true; break;
                            case "nbFrappe": 
                                nbFrappeMin = Integer.parseInt(n.getAttributes().item(1).getNodeValue()); 
                                nbFrappeMax = Integer.parseInt(n.getAttributes().item(0).getNodeValue());
                                break;
                            case "pokemons": //Dans ce cas, on récupère les id des pokémons pouvant utiliser la capacité
                                String content = n.getTextContent();
                                if(content.isBlank()) break;
                                if(content.toLowerCase().equals("all")){
                                    for (int k = 0; k < NB_POKEMON; k++) {
                                        _pkmnCapacite[k].add(id);
                                    }
                                    break;
                                }
                                //Si on trouve une virgule, il y a plusieurs pokémons donc on split la chaîne, sinon on lit directement l'id
                                if (content.contains(",")) {
                                    String[] pkmns = content.split(",");
                                    for (String s : pkmns) { this._pkmnCapacite[Integer.parseInt(s)-1].add(id); }
                                } else _pkmnCapacite[Integer.parseInt(content)-1].add(id);
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
                                                    case "bueenoire": effetsCapa.add(new EffetSpecial(cible, chance, BUEENOIRE, 0)); break;
                                                    case "copie_type": effetsCapa.add(new EffetSpecial(cible, chance, COPIE_TYPE, 0)); break;
                                                    case "copie_capacite": effetsCapa.add(new EffetSpecial(cible, chance, COPIE_CAPACITE, 0)); break;
                                                    case "contrecoup": effetsCapa.add(new EffetSpecial(cible, chance, CONTRECOUP, Double.parseDouble(info.getAttributes().item(0).getTextContent()))); break;
                                                    case "entrave": effetsCapa.add(new EffetSpecial(cible, chance, ENTRAVE, 0)); break;
                                                    case "degats2vie": effetsCapa.add(new EffetSpecial(cible, chance, DEGATS2VIE, Double.parseDouble(info.getAttributes().item(0).getTextContent()))); break;
                                                    case "metronome": effetsCapa.add(new EffetSpecial(cible, chance, METRONOME, 0)); break;
                                                    case "mimique": effetsCapa.add(new EffetSpecial(cible, chance, MIMIQUE, 0)); break;
                                                    case "renvoi_degat": effetsCapa.add(new EffetSpecial(cible, chance, RENVOI_DEGAT, Double.parseDouble(info.getAttributes().item(0).getTextContent()))); break;
                                                    case "soin": effetsCapa.add(new EffetSpecial(cible, chance, SOIN, 0)); break;
                                                    case "devoreve": effetsCapa.add(new EffetSpecial(cible, chance, DEVOREVE, 0)); break;
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
                    _capacite[id] = new Capacite(nomCapa, type, puissance, precision, pp, boostCrit, nbFrappeMin, nbFrappeMax, tourAvant, tourApres);
                    for(Effet effet : effetsCapa)
                        _capacite[id].addEffet(effet);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
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
        return _pkmnCapacite[id - 1];
    }
    
    /**
     * permet de récupérer la capacité grâce à son id
     * @param id l'id de la capacité à récupérer
     * @return la capacité
     */
    public Capacite getCapacite(int id){
        return _capacite[id];
    }
    
    public int[] getPokemonId(String search){
        ArrayList<Integer> id = new ArrayList<>();
        search = normalize(search);
        for(int i = 0; i < _pkmnNom.length; i++){
            if(normalize(_pkmnNom[i]).contains(search)) id.add(i);
        }
        return convertToInt(id);
    }
    
    public int[] getCapaciteId(String search){
        ArrayList<Integer> id = new ArrayList<>();
        search = normalize(search);
        for(int i = 1; i < NB_CAPACITE; i++){
            if(_capacite[i] == null) continue;
            if(normalize(_capacite[i].getNom()).contains(search)) id.add(i);
        }
        return convertToInt(id);
    }
    
    public ArrayList<Integer> getCapaciteId(int pokemonId, String search){
        ArrayList<Integer> res = new ArrayList<>();
        search = normalize(search);
        ArrayList<Integer> listeCapa = _pkmnCapacite[pokemonId - 1];
        for(int i = 0; i < listeCapa.size(); i++){
            int id = listeCapa.get(i);
            if(_capacite[id] == null) continue;
            if(normalize(_capacite[id].getNom()).contains(search)) res.add(id);
        }
        return res;
    }
    
    public String[] getListePokemon(){
        String res[] = new String[NB_POKEMON];
        for(int i = 0; i < NB_POKEMON; i++){
            res[i] = _pkmnNom[i] + " (" + _pkmnType[i][0].toString() + (_pkmnType[i][1]!=null?"/"+_pkmnType[i][1].toString():"") + ")";
        }
        return res;
    }
    
    public ArrayList<String> getListeCapacite(Pokemon pk){
        ArrayList<Integer> idCapa = getCapaciteId(pk.getId(), "");
        ArrayList<String> res = new ArrayList<>();
        for(int i : idCapa){
            res.add(_capacite[i].getNom() + " (" + _capacite[i].getTypePkmn().toString() 
                             + "/" + _capacite[i].getTypeAtq().toString() + ") " + _capacite[i].getPPMax() + " PP");
        }
        return res;
    }
    
    private static String normalize(String s){
        s = s.toLowerCase();
        String convert[][] = {
            {"é", "e"},
            {"è", "e"},
            {"ê", "e"},
            {"ë", "e"},
            {"à", "a"},
            {"â", "a"},
            {"î", "i"},
            {"ï", "i"},
            {"ç", "c"},
            {"ô", "o"},
            {"ö", "o"},
            {"'-_,?;.:/!§ ", ""}
        };
        
        for(String[] conv : convert) s = s.replaceAll("[" + conv[0] + "]", conv[1]);
        return s;
    }
    
    private static int[] convertToInt(ArrayList<Integer> liste){
        int[] res = new int[liste.size()];
        for(int i = 0; i < liste.size(); i++){
            res[i] = (int) liste.get(i);
        }
        return res;
    }
}
