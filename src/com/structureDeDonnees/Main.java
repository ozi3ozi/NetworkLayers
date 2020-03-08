package com.structureDeDonnees;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.Checksum;

public class Main {

    public static void main(String[] args) {

        String dataETH = "message a envoyer ETHERNET";
        String dataIP = "message a envoyer IP";
        String dataTCP = "message a envoyer TCP";

        Ether_hdr Entete;


//        Entete = new Ether_hdr(dataETH);
//        Paquet P_Eth = new Paquet(Entete, Entete.donneesATransf);
//        System.out.println("\n********************************");
//        System.out.println(Entete.toString());
//        System.out.println("********************************\n");
//        Firewall F1 = new Firewall(P_Eth);
//        System.out.println("********************************\n");
//
//        Entete = new Ip_hdr(dataIP);
//        Paquet P_Ip = new Paquet(Entete,((Ip_hdr) Entete).donneesATransf);
//        System.out.println("\n********************************");
//        System.out.println(Entete.toString());
//        System.out.println("********************************\n");
//        Firewall F2 = new Firewall(P_Ip);
//        System.out.println("********************************\n");

        Entete = new Tcp_hdr(dataTCP);
        Paquet P_Tcp = new Paquet(Entete,((Tcp_hdr) Entete).donneesATransf);
        System.out.println("\n********************************");
        System.out.println(Entete.toString());
        System.out.println("********************************\n");
        Firewall F3 = new Firewall(P_Tcp);
        System.out.println("********************************\n");

        System.out.println(Reseau.listePaquet);


    }
}
