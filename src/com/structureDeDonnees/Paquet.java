package com.structureDeDonnees;

import java.util.ArrayList;

public class Paquet {
    public static int id = 0;
    public Ether_hdr Entete;
    public String donneesATransferer;
    public CheckSumAlgorithm Checksum = new CheckSumAlgorithm();
    String checksum;
    public ArrayList<String> encapsArray = new ArrayList<>();
    final Src_Ou_Dst expdteur_ou_dstntaire = Src_Ou_Dst.expediteur;

    public Paquet(Ether_hdr entete, String donneesATransferer) {
        Entete = entete;
        this.donneesATransferer = donneesATransferer;

        if (!(entete instanceof Ip_hdr)){ //cela voudra dire que l'entete est Ethernet
            encapsArray.addAll(entete.entete_par16bits);
            encapsArray.addAll(Ether_hdr.donneesATransf_par_16_bits(Ether_hdr.toBinaryTable(entete.donneesATransf)));
            this.checksum = Checksum.calculcChecksum(encapsArray,
                                                    expdteur_ou_dstntaire);
            encapsArray.add(this.checksum);

        } else if (!(entete instanceof Tcp_hdr)){ //cela voudra dire que l'entete est IP
            encapsArray.addAll(((Ip_hdr)entete).entete_par16bits);
            encapsArray.addAll(Ether_hdr.donneesATransf_par_16_bits(Ether_hdr.toBinaryTable(entete.donneesATransf)));
            this.checksum = Checksum.calculcChecksum(encapsArray,
                                                    expdteur_ou_dstntaire);
            encapsArray.add(this.checksum);

        } else { //cela voudra dire que l'entete est TCP
            encapsArray.addAll(((Tcp_hdr)entete).entete_par16bits);
            encapsArray.addAll(Ether_hdr.donneesATransf_par_16_bits(Ether_hdr.toBinaryTable(entete.donneesATransf)));
            this.checksum = Checksum.calculcChecksum(encapsArray,
                                                    expdteur_ou_dstntaire);
            encapsArray.add(this.checksum);
        }
        ++id;
    }
    
    public String arrayListToString(ArrayList<String> addr){
        String stringAddr = "";
        for (String s:addr) {
            stringAddr += s;
        }
        return stringAddr;
    }

    public void setEncapsArray(ArrayList<String> entete1,
                               ArrayList<String> dataToSend, String checksumBinaire){
        this.encapsArray.addAll(entete1);
        this.encapsArray.addAll(dataToSend);
        this.encapsArray.add(checksumBinaire);

    }


    @Override
    public String toString() {
        return "\nPaquet{" +
                "\ndonneesATransferer='" + donneesATransferer + '\'' +
                "\n, checksum='" + Ether_hdr.binaire_A_Deciaml(checksum)+ '\'' +
                "\n, expdteur_ou_dstntaire=" + expdteur_ou_dstntaire +
                Entete.toString()+
                '}';
    }
}
