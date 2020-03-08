package com.structureDeDonnees;

import java.util.ArrayList;
import java.util.Scanner;

public class Ether_hdr { // Autre appelation de la classe: EnteteEthernet
    ArrayList<String> e_dst; // 6 bytes
    ArrayList<String> e_src; // 6 bytes
    String e_type; // tout les ether type commence avec 0x.
                // Comme 0x0800 pour signaler que la trame contient IPv4 datagramme.
                // Donc on prendra juste les 4 chiffres apres le 0x.
    final int macLength = 6;
    String donneesATransf;
    public ArrayList<String> entete_par16bits = new ArrayList<>();

    public void setEntete_par16bits() {
        this.entete_par16bits.add(this.e_src.get(0)+this.e_src.get(1));
        this.entete_par16bits.add(this.e_src.get(2)+this.e_src.get(3));
        this.entete_par16bits.add(this.e_src.get(4)+this.e_src.get(5));
        this.entete_par16bits.add(this.e_dst.get(0)+this.e_dst.get(1));
        this.entete_par16bits.add(this.e_dst.get(2)+this.e_dst.get(3));
        this.entete_par16bits.add(this.e_dst.get(4)+this.e_dst.get(5));
    }


    public Ether_hdr(String donnees) {
        int[] e_dst = new int[macLength];
        int[] e_src = new int[macLength];
        this.donneesATransf = donnees;
        System.out.println("Vous devez Entrez deux adresses MAC. la 1ere est l'addresse source et la 2eme est" +
                " l'adresse de destination");
        this.e_src = verifyAddress(e_src,macLength);
        this.e_dst = verifyAddress(e_dst,macLength);
    }

    public ArrayList<String> verifyAddress(int[] addr, int addrlength){
        ArrayList<String> binaryStringTable;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("l'addresse doit avoir " +addrlength+ " bytes(8bits)" +
                    " Entrez les bytes un a la fois!" +
                    " Attention, les valeurs doivent etre comprises entre 0 et 255");
            for (int i =0; i < addrlength; ++i){
                addr[i] = input.nextInt();
                while (addr[i] < 0 || addr[i] > 255){
                    System.out.println("Attention, les valeurs doivent etre comprises entre 0 et 255");
                    addr[i] = input.nextInt();
                }
            }
        }while (addr.length != addrlength);
        binaryStringTable = toBinaryStringTable(addr,8);

        return binaryStringTable;
    }

    public static String ajouterNbreDe_0_manquant(String champ, int longueurDuChamp){
        //longueurDuChamp indique de combien de bits est compose l'element du header.
        // ex: l'adresse MAC se compose de 6 bytes et chaque byte contient 8 bits.
        // Donc lors de la conversion, des chiffres decimaux en binaire, des 0 seront ajoutes au besoin.
        // 5 = 101 ==> la methode ajoutera cinq zero: 5 = 00000101
        // cette methode nous aidera pour l'addition des nombre binaires.
        while (champ.length() != longueurDuChamp){
            champ = "0"+champ;
        }
        return champ;
    }

    public static ArrayList<String> toBinaryStringTable(int[] byteTable, int longueurElementTableEnBinaire){
        //Convertit une table int de chiffres deciamaux en une table String de chiffres binaires correspondants
        ArrayList<String> stringTable = new ArrayList<>();
        for (int i =0; i < byteTable.length; ++i) {
            stringTable.add(Integer.toBinaryString(byteTable[i]));
            stringTable.set(i,ajouterNbreDe_0_manquant(stringTable.get(i),longueurElementTableEnBinaire));
        }
        return stringTable;
    }

    public static int binaire_A_Deciaml(String chiffreBinaire) {
        int chiffreDeciaml = 0;
        char[] arrayBinaire = chiffreBinaire.toCharArray();
        for (int i = 0, j=arrayBinaire.length-1; i<arrayBinaire.length; ++i,--j){
            if (arrayBinaire[i] == '1'){
                chiffreDeciaml += Math.pow(2,j);
            }
        }
        return chiffreDeciaml;
    }

    public static String decimal_A_Binaire(int chiffreDecimal){
        String chiffreBinaire = "";
        while(chiffreDecimal != 0){
            if (chiffreDecimal%2 == 0){
                chiffreBinaire = "0"+chiffreBinaire;
            }  else chiffreBinaire = "1"+chiffreBinaire;
            chiffreDecimal = (chiffreDecimal/2);
        }
        return chiffreBinaire;
    }

    public static ArrayList<String> toBinaryTable(String donnees){ // pour transformer le message a transfere en binaire
        byte[] byteTable = donnees.getBytes();
        ArrayList<String> stringTable = new ArrayList<>();
        for (int i = 0; i < byteTable.length; ++i) {
            stringTable.add(ajouterNbreDe_0_manquant(Integer.toBinaryString(byteTable[i]),8));
        }
        return stringTable;
    }

    public static ArrayList<String> donneesATransf_par_16_bits(ArrayList<String> donneesATrans_par_8_bits){
        ArrayList<String> array_donnees_A_Transf_par_16_bits = new ArrayList<>();
        if (donneesATrans_par_8_bits.size()%2 == 0){//si la longueur du message a envoyer est paire,
            // l'ajout dans l'entete se fait par 16 bits(par 2 lettres) sans probleme
            for (int i=0; i<donneesATrans_par_8_bits.size();i+=2){
                array_donnees_A_Transf_par_16_bits.add(donneesATrans_par_8_bits.get(i)+donneesATrans_par_8_bits.get(i+1));
            }
        } else if (donneesATrans_par_8_bits.size()%2 != 0){//si la longueur du message a envoyer est impaire,
            // l'ajout dans l'entete se fait par 16 bits sauf pour la derniere lettre
            // qui est ajoutee separement mais a laquelle on augmente la longueur a 16 bits au lieu de 8.
            for (int i=0; i<donneesATrans_par_8_bits.size();i+=2){
                if (i < donneesATrans_par_8_bits.size()-1){
                    array_donnees_A_Transf_par_16_bits.add(donneesATrans_par_8_bits.get(i)+donneesATrans_par_8_bits.get(i+1));
                } else if (i == donneesATrans_par_8_bits.size()-1){//Derniere lettre. 16 bits au lieu de 8.
                    array_donnees_A_Transf_par_16_bits.add(Ether_hdr.ajouterNbreDe_0_manquant(donneesATrans_par_8_bits.get(i),16));
                }
            }
        }
        return array_donnees_A_Transf_par_16_bits;
    }

    @Override
    public String toString() {
        return "\nEther_hdr{" +
                ", e_src=" + binaire_A_Deciaml(e_src.get(0))+"." +
                                binaire_A_Deciaml(e_src.get(1))+"." +
                                        binaire_A_Deciaml(e_src.get(2))+"." +
                                                binaire_A_Deciaml(e_src.get(3))+"." +
                                                        binaire_A_Deciaml(e_src.get(4))+"." +
                                                                binaire_A_Deciaml(e_src.get(5)) +
                "\n, e_dst=" + binaire_A_Deciaml(e_dst.get(0))+"." +
                                binaire_A_Deciaml(e_dst.get(1))+"." +
                                        binaire_A_Deciaml(e_dst.get(2))+"." +
                                                binaire_A_Deciaml(e_dst.get(3))+"." +
                                                        binaire_A_Deciaml(e_dst.get(4))+"." +
                                                                binaire_A_Deciaml(e_dst.get(5))+"." +
                '}';
    }
}
