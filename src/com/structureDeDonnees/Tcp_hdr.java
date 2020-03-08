package com.structureDeDonnees;

import java.util.ArrayList;
import java.util.Scanner;

public class Tcp_hdr extends Ip_hdr {
    final String IP_TCP = "0110";  // {0x0,0x0,0x0,0x6}
    String tcp_SPort; // port source. 16 bits length.
    String tcp_DPort; // port de destination. 16 bits length
    final int tcpLength = 1;
//    String zeroValue = "00000000"; // pour ajouter par byte au tcp_seq et au tcp_ackseq
    final String tcp_seq = "00000000000000000000000000000000"; // sequence number. 32 bits length. sera toujours egal a 0.(nombre relatif)
                                // normalement, c'est un chiffre au hasard entre 0 et 4,294,967,295 inclusivement
    final String tcp_ackseq = "00000000000000000000000000000000"; // acknowledge number. 32 bits length. sera toujours egal au tcp_seq.

    public Tcp_hdr(String donnees) {
        super(donnees);
        System.out.println("Vous devez Entrez deux adresses TCP qui traduisent le numero de port." +
                " la 1ere est le numero de port source et la 2eme est le numero de port de destination");
        this.tcp_SPort = verifyAddress();
        this.tcp_DPort = verifyAddress();
        setEntete_par16bits();
    }


    public String verifyAddress() {
        String binaryString;
        int addr;
        Scanner input = new Scanner(System.in);

        System.out.println(" Attention, les numeros de port peuvent aller de 0 et 65535. " +
                "\nLes ports acceptes sont ceux entre 1 et 1024. Si le numero de port n'est pas " +
                "\ncompris entre ces deux valeurs, le paquet ne sera pas transmit et les informations " +
                "\nsur ce dernier ainsi que sur le type d'erreur seront enregistrees sur logERROR.txt ");

        addr = input.nextInt();
        while (addr>65535){
            System.out.println("Numero de port inexistant! Le numero de port ne peut pas etre superieur a 65535. Entrez un numero valide!");
            addr = input.nextInt();
        }

        binaryString = ajouterNbreDe_0_manquant(decimal_A_Binaire(addr),16);

        return binaryString;
    }

    @Override
    public void setEntete_par16bits() {
        super.setEntete_par16bits();
        this.entete_par16bits.add(tcp_SPort);
        this.entete_par16bits.add(tcp_DPort);
    }

    @Override
    public String toString() {
        return super.toString()+
                "\nTcp_hdr{" +
                "\nIP_TCP='" + binaire_A_Deciaml(IP_TCP)+" ==>TCP" + '\'' +
                "\n, tcp_SPort=" + binaire_A_Deciaml(tcp_SPort)+
                "\n, tcp_DPort=" + binaire_A_Deciaml(tcp_DPort)+
                "\n, tcpLength=" + tcpLength +
                "\n, tcp_seq='" + binaire_A_Deciaml(tcp_seq )+ '\'' +
                "\n, tcp_ackseq='" + binaire_A_Deciaml(tcp_ackseq )+ '\'' +
                '}';
    }
}
