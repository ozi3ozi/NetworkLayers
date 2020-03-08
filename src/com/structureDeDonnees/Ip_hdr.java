package com.structureDeDonnees;

import java.util.ArrayList;

public class Ip_hdr extends Ether_hdr {
    final String ETHER_IP = "1000";  // {0x0,0x8,0x0,0x0}
                                // 4 bits length. 0x veut dire que les nombre qui suivent sont en hexadecimal.
                                // 0b veut dire que les nombre qui suivent sont en binaire.
    final String version = "0100"; // {0x0,0x0,0x0,0x4}
                                //ip version. 4 bits length. Puisuq'on va utiliser IPv4 seulement, version sera tjrs egal a 4(0100)
    final String ihl = "0101"; //{0x0,0x0,0x0,0x5}
                //Internet header length. 4bits length. minimum 5 (0101) maximum 15 (1111)
                // puisqu'on n'utilisera que 13 champs, le 14 champ etant optionel, ihl sera tjrs egal a 5
                // version et ihl constituent le 1er byte de Ip_hdr
    final String tos = "00000000"; //Type of service. 8 bits length. constituent le 2eme byte de Ip_hdr
            // Se repartir un bloc de 6 bits et un autre de 2 bits.
            // selon la priorite d'envoie, la valeur du 1er bloc aura un chiffre pair entre 0 et 56.
            //le 2eme bloc est toujours egal a 0.
            //Dans notre cas, la priorite est initialise a "Best Effort"
    String tot_len; //total length. Constituent les 3eme et 4eme bytes. valeur min 20 max 65 535
                // cette valeur exprime la longueur du paquet en byte.
    String id; // id for IP packet. Constituent les 5eme et 6eme bytes.
            // sert a identifier le groupe de fragment lors de la fragmentation du paquet
    final String flag = "010"; // 3 bits length. bit 0: reserve. toujours 0.
                                //bit 1: ne pas fragmenter(DF)
                                //bit 2: fragmenter(MF)
                                 //Dans notre exemple, le paquet ne sera jamis fragmente. donc flag =
    final String frag_offset = "0000000000000" ; // fragment offset. 13 bits length
                    // sera toujours 0 puisqu'on ne fragmente pas les paquets
                    // flag et frag_offset constituent les 7eme et 8eme bytes
    final String time_to_live = "11111111"; // 8 bits length. la valeur exprime le nombre de secondes maximum
                        // pour acheminer un paquet. sinon il est rejete.
                        // chaque fois que le paquet arrive a un routeur. time_to_live est decremente de 1.
                        // Dans notre cas on prendra le maximum qui est de 255 secondes soit 4 minutes.
    final String protocol = "00000110"; // 8 bits length. definit le protocol utilise dans la couche Transport.
                    // Dans notre cas, on utilise TCP. donc la valuer doit etre 6.
    ArrayList<String> saddr; // source IP address. 32 bits length.
    ArrayList<String> daddr; // destination IP address. 32 bits length
    final int ipLength = 4;
    CheckSumAlgorithm Checksum = new CheckSumAlgorithm();
    String ip_hdr_checksum; // 16 bits length. En 1er, on calcul le total de chaque 16 bits de l'entete
                // sauf les 16 bits du checksum.
                // Ensuite on calcul le total de ces totaux tout en gardant la longueur du checksum a 16 bits.
                //s'il le total est rendu a 17 bits. on l'enleve de la fin et on l'ajoute au 1er bits
                //ex: 1 1001 0001 1010 0000 ==> 1001 0001 1010 0001
                //le checksum est egal au complement du total des totaux.
    // on va reunir chaque 16 bits(2 bytes) de l'entete en commencant par "version" puisque ETHER_IP ne fait
    //pas partie de l'entete ip. On aura donc 10 champs de 16 bits chaque.

    public Ip_hdr(String donnees) {
        super(donnees);
        int[] src_addr = new int[ipLength];
        int[] dst_addr = new int[ipLength];
        this.e_type = ETHER_IP;
        System.out.println("Vous devez Entrez deux adresses IP. la 1ere est l'addresse source et la 2eme est" +
                " l'adresse de destination");
        this.saddr = verifyAddress(src_addr,ipLength);
        this.daddr = verifyAddress(dst_addr,ipLength);
        this.tot_len = ajouterNbreDe_0_manquant(decimal_A_Binaire(20+donnees.length()),16);
        this.id = ajouterNbreDe_0_manquant(decimal_A_Binaire(Paquet.id),16);
        deuxieme16bits = tot_len;
        troisieme16bits = id;
        septieme16bits = saddr.get(0)+saddr.get(1);
        huitieme16bits = saddr.get(2)+saddr.get(3);
        neuvieme16bits = daddr.get(0)+daddr.get(1);
        dixieme16bits = daddr.get(2)+daddr.get(3);
        setEntete_IP_par16bits();
        this.ip_hdr_checksum = Checksum.calculcChecksum(this.entete_IP_par16bits,Src_Ou_Dst.expediteur);
        entete_IP_par16bits.set(5,ip_hdr_checksum);

    }

    String premier16bits = version + ihl + tos;
    String deuxieme16bits;//definit dans le constructeur
    String troisieme16bits;//definit dans le constructeur
    String quatrieme16bits = flag + frag_offset;
    String cinquieme16bits = time_to_live + protocol;
    String sixieme16bits = ip_hdr_checksum;
    String septieme16bits;//definit dans le constructeur
    String huitieme16bits;//definit dans le constructeur
    String neuvieme16bits;
    String dixieme16bits;

    public ArrayList<String> entete_IP_par16bits = new ArrayList<>();

    public void setEntete_IP_par16bits() {
        this.entete_IP_par16bits.add(premier16bits);
        this.entete_IP_par16bits.add(deuxieme16bits);
        this.entete_IP_par16bits.add(troisieme16bits);
        this.entete_IP_par16bits.add(quatrieme16bits);
        this.entete_IP_par16bits.add(cinquieme16bits);
        this.entete_IP_par16bits.add(sixieme16bits);
        this.entete_IP_par16bits.add(septieme16bits);
        this.entete_IP_par16bits.add(huitieme16bits);
        this.entete_IP_par16bits.add(neuvieme16bits);
        this.entete_IP_par16bits.add(dixieme16bits);
    }

    @Override
    public void setEntete_par16bits() {
        super.setEntete_par16bits();
        this.entete_par16bits.add(septieme16bits);//correspond au saddr
        this.entete_par16bits.add(huitieme16bits);//correspond au saddr
        this.entete_par16bits.add(neuvieme16bits);//correspond au daddr
        this.entete_par16bits.add(dixieme16bits);//correspond au daddr
    }

    @Override
    public String toString() {
        return super.toString()+
                "\nIp_hdr{" +
                "\nETHER_IP='" + binaire_A_Deciaml(ETHER_IP )+ '\'' +
                "\n, version='" + binaire_A_Deciaml(version )+ '\'' +
                "\n, ihl='" + binaire_A_Deciaml(ihl )+ '\'' +
                "\n, tos='" + binaire_A_Deciaml(tos )+ '\'' +
                "\n, tot_len='" + binaire_A_Deciaml(tot_len )+" bytes"+ '\'' +
                "\n, id='" + binaire_A_Deciaml(id) + '\'' +
                "\n, flag='" + binaire_A_Deciaml(flag )+" ==>Ne pas fragmenter"+ '\'' +
                "\n, frag_offset='" + binaire_A_Deciaml(frag_offset )+ '\'' +
                "\n, time_to_live='" + binaire_A_Deciaml(time_to_live )+" secondes"+ '\'' +
                "\n, protocol='" + binaire_A_Deciaml(protocol )+" ==>TCP"+ '\'' +
                "\n, saddr=" + binaire_A_Deciaml(saddr.get(0))+"."+
                                binaire_A_Deciaml(saddr.get(1))+"."+
                                binaire_A_Deciaml(saddr.get(2))+"."+
                                binaire_A_Deciaml(saddr.get(3))+
                "\n, daddr=" + binaire_A_Deciaml(daddr.get(0))+"."+
                                binaire_A_Deciaml(daddr.get(1))+"."+
                                binaire_A_Deciaml(daddr.get(2))+"."+
                                binaire_A_Deciaml(daddr.get(3)) +
                "\n, ipLength=" + ipLength +
                "\n, ip_hdr_checksum='" + binaire_A_Deciaml(ip_hdr_checksum )+ '\'' +
                '}';
    }
}
