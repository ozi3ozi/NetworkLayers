package com.structureDeDonnees;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import static java.nio.file.StandardOpenOption.*;

public class Firewall {
    public Paquet P_Recus;
    public static ArrayList<Regles_Addr_Port> reglesAddrPort = new ArrayList<>();
    public final Src_Ou_Dst srcOuDst = Src_Ou_Dst.destinataire;
    CheckSumAlgorithm Checksum = new CheckSumAlgorithm();
    public String msgErreur;
    public String filePath = new File("").getAbsolutePath();
    public String cheminFichicher = filePath.concat("\\logError.txt");



    public Firewall(Paquet P_Recus) {
        this.P_Recus = P_Recus;
        reglesAddrPort.add(Regles_Addr_Port.Reserved);
        reglesAddrPort.add(Regles_Addr_Port.Well_known);
        reglesAddrPort.add(Regles_Addr_Port.Registered);
        reglesAddrPort.add(Regles_Addr_Port.Dynamic_Private);
        filtrePaquet(this.P_Recus);
    }
    //Le numero de port est verifie avant de verifie le checksum. SI le numero de port est invalide. il n'y aura
    //pas de calcul de verification du checksum
    public void filtrePaquet(Paquet P_A_Filtrer){
        if (P_A_Filtrer.Entete instanceof Tcp_hdr){//Cette methode ne fonctionnera que si le paquet a une entete TCP
            int source_Port_nbr = Ether_hdr.binaire_A_Deciaml(((Tcp_hdr)P_A_Filtrer.Entete).tcp_SPort);
            int dest_Port_nbr = Ether_hdr.binaire_A_Deciaml(((Tcp_hdr)P_A_Filtrer.Entete).tcp_DPort);
            // on convertit l'adresse du port source et de destination pour le comparer selon les regles etablies
            if (source_Port_nbr==0 || dest_Port_nbr == 0){
                msgErreur = "\n***************************************" +
                        "\nErreur! Vous avez utilise un port non permis de type 'Reserved' " +
                        "\nInfo Paquet: \n" + P_A_Filtrer.toString()+
                        "\nDate et heure: \n" + LocalDateTime.now()+"\n";
                writeTofile(msgErreur,cheminFichicher);
                System.out.println("Erreur de transmission! Les informations sur le paquet on " +
                        "ete enregistre sur le fichier TP_1/logERROR.txt");

            } else if (source_Port_nbr<= 1024 && dest_Port_nbr <= 1024){
                verificationChecksum(P_A_Filtrer);

            } else if ((source_Port_nbr >= 1024 && source_Port_nbr<= 49151)||( dest_Port_nbr >= 1024 && dest_Port_nbr <= 49151)){
                msgErreur = "\n***************************************" +
                        "\nErreur! Vous avez utilise un port non permis de type 'Registered' " +
                        "\nInfo Paquet: \n" + P_A_Filtrer.toString()+
                        "\nDate et heure: \n" + LocalDateTime.now()+"\n";
                writeTofile(msgErreur,cheminFichicher);
                System.out.println("Erreur de transmission! Les informations sur le paquet on " +
                        "ete enregistre sur le fichier TP_1/logERROR.txt");
            } else if (source_Port_nbr<= 65535 || dest_Port_nbr <= 65535){
                msgErreur = "\n***************************************" +
                        "\nErreur! Vous avez utilise un port non permis de type 'Dynamic/Private' " +
                        "\nInfo Paquet: \n" + P_A_Filtrer.toString()+
                        "\nDate et heure: \n" + LocalDateTime.now()+"\n";
                writeTofile(msgErreur,cheminFichicher);
                System.out.println("Erreur de transmission! Les informations sur le paquet on " +
                        "ete enregistre sur le fichier TP_1/logERROR.txt");
            }
        } else if (P_A_Filtrer.Entete instanceof Ip_hdr){
            verificationChecksum(P_A_Filtrer);
        } else {
            Reseau.ajoutPaquetAuReseau(P_A_Filtrer);
            System.out.println("L'envoie a ete effectue avec succes. Le paquet a ete ajoute au reseau ");
        }
    }

    public void verificationChecksum(Paquet P_to_verify){
        String checksumPaquet = Checksum.verifChecksumPaquet(P_to_verify);
        String checksumIP = Checksum.verifChecksumIpHdr(P_to_verify);

        if (P_to_verify.Entete instanceof Tcp_hdr) {

            if (Integer.parseInt(checksumPaquet) == 0 && Integer.parseInt(checksumIP) == 0) {
                Reseau.ajoutPaquetAuReseau(P_to_verify);
                System.out.println("L'envoie a ete effectue avec succes. Le paquet a ete ajoute au reseau " +
                        "Les deux checksum de verification sont egal a 0. \n" +
                        "ChecksumPaquet: " + checksumPaquet + "\n" +
                        " ChecksumIP: " + checksumIP);
            } else {
                msgErreur = "\n***************************************" +
                        "\nErreur! Le message recu est different de celui envoye " +
                        "\nInfo Paquet: " + P_to_verify.toString() +
                        "\nDate et heure: " + LocalDateTime.now() +
                        "\nChecksumPaquet: " + checksumPaquet +
                        " \nChecksumIP: " + checksumIP + "\n";

                writeTofile(msgErreur, cheminFichicher);
                System.out.println("Erreur de transmission! Les informations sur le paquet on " +
                        "ete enregistre sur le fichier TP_1/logERROR.txt");
            }
        } else {

            if (Integer.parseInt(checksumIP) == 0 || checksumIP == "pas calcule") {
                Reseau.ajoutPaquetAuReseau(P_to_verify);
                System.out.println("L'envoie a ete effectue avec succes. Le paquet a ete ajoute au reseau " +
                        "Les deux checksum de verification sont egal a 0. \n" +
                        " ChecksumIP: " + checksumIP);
            } else {
                msgErreur = "\n***************************************" +
                        "\nErreur! Le message recu est different de celui envoye " +
                        "\nInfo Paquet: " + P_to_verify.toString() +
                        "\nDate et heure: " + LocalDateTime.now() +
                        " \nChecksumIP: " + checksumIP + "\n";

                writeTofile(msgErreur, cheminFichicher);
                System.out.println("Erreur de transmission! Les informations sur le paquet on " +
                        "ete enregistre sur le fichier TP_1/logERROR.txt");
            }
        }
    }

    public static void writeTofile(String msg, String chemin){
        Path fichier = Paths.get(chemin);
        byte[] data = msg.getBytes();
        OutputStream output;
        try {
            output = new BufferedOutputStream(Files.newOutputStream(fichier,CREATE,APPEND));
            output.write(data);
            output.flush();
            output.close();
        }
        catch (Exception e){
            System.out.println("Message: "+e);
        }
    }

}
