package com.structureDeDonnees;

import jdk.jfr.Registered;

public enum Regles_Addr_Port {
    Reserved,           // si le numero port est egal a 0
    Well_known,         // si le numero de port est entre 1 et 1024
    Registered,         // si le numero de port est entre 1025 et 49151
    Dynamic_Private,     // si le numero de port est entre 49152 et 65535

    //Dans notre exemple, la regle est que le numero de port doit etre "Well-known" sinon les informations
    // du paquet seront enregistrees dans le fichier logERROR.txt
}
