package com.structureDeDonnees;

import java.util.ArrayList;

public class Reseau {
    public static ArrayList<Paquet> listePaquet = new ArrayList<>();

    public static void ajoutPaquetAuReseau(Paquet P){
        listePaquet.add(P);
    }


}
