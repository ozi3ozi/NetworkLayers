package com.structureDeDonnees;

import java.util.ArrayList;

public class CheckSumAlgorithm {
    Ip_hdr EntetIp;
    Paquet Donnees;
    Src_Ou_Dst choix;


    //calcul du checksum de l'entete IP
    public String calculcChecksum(ArrayList<String> entete, Src_Ou_Dst choix){
        int checksumDecimalIncomplet = 0;
        String checksumBinaireComplet= "";
        for (int i = 0; i < entete.size(); ++i){
            if (entete.size() == 10 && choix == Src_Ou_Dst.expediteur && i == 5) {
                // pour le calcul du checksum de l'entete IP cote expediteur,
                // le champs checksum dans l'entete ip n'est pas pris en compte.
                // il n'y aura jamais confusion avec le calcul du checksum du paquet
                // car le paquet a deja une longueur de 10 sans prendre en consideration
                // les addresses TCP source et destination et la longueur du message
                ++i;
            }
            checksumDecimalIncomplet += Ether_hdr.binaire_A_Deciaml(entete.get(i));
        }
        checksumBinaireComplet = Ether_hdr.decimal_A_Binaire(checksumDecimalIncomplet);
        if (checksumBinaireComplet.length() > 16){ // voir notes classe "Ip_hdr", variable "ip_hdr_checksum"
            int nbr_de_char_A_Oter = checksumBinaireComplet.length() - 16;
            char[] checksumChar = checksumBinaireComplet.toCharArray();
            int i = 0;
            String bitARajouter = "";
            while (i < nbr_de_char_A_Oter){
                bitARajouter = checksumChar[i] + bitARajouter;
                checksumBinaireComplet = removeCharAt(checksumBinaireComplet,0);
                ++i;
            }
            checksumDecimalIncomplet = Ether_hdr.binaire_A_Deciaml(checksumBinaireComplet)
                                        + Ether_hdr.binaire_A_Deciaml(bitARajouter);
            checksumBinaireComplet = Ether_hdr.ajouterNbreDe_0_manquant(Ether_hdr.decimal_A_Binaire(checksumDecimalIncomplet),16);
            checksumChar = checksumBinaireComplet.toCharArray();
            for (i = 0; i < checksumChar.length; ++i){ // Derniere etape du calcul du checksum: calculer le complement
                if (checksumChar[i] == '0'){
                    checksumChar[i] = '1';
                } else checksumChar[i] = '0';
            }
            checksumBinaireComplet = "";
            for (char c:checksumChar) {
                checksumBinaireComplet += c;
            }
        }

        return checksumBinaireComplet;
    }


    public String verifChecksumPaquet(Paquet P){
        return calculcChecksum(P.encapsArray,Src_Ou_Dst.destinataire);
    }

    public String verifChecksumIpHdr(Paquet P){
        if (P.Entete instanceof Tcp_hdr){
            return calculcChecksum(((Tcp_hdr)P.Entete).entete_IP_par16bits,Src_Ou_Dst.destinataire);
        }else {
            return calculcChecksum(((Ip_hdr)P.Entete).entete_IP_par16bits,Src_Ou_Dst.destinataire);
        }

    }

    public static String removeCharAt(String s, int pos) {
        return s.substring(0, pos) + s.substring(pos + 1);
    }


}
