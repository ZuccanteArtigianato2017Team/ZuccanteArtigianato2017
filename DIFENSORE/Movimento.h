
#ifndef _MOVIMENTO
#define _MOVIMENTO

#include "Definitions.h"
#include "MisuraUsAffidabile.h"
#include "Motori.h"       // libreria con tutti i movimenti dei motori possibili
#include "Sensori.h"

// qui presenti le funzioni di movimento




class Movimento
{
public:
    Movimento(); // gli servono i sensori per funzionare

    bool cambia;
    void _delay(int ms);         //
    void controlla_fuori_campo();//
	
    void aggiusta_orientamento(int cmps = 1000,int vel = VelR); //
    void orientamentoGiusto_assolutamente(); //
    
    void orienta_PID();
    
    bool gestisciFuoriCampo(int volta = 0);
    void CercaPalla();
    bool dribbler(); // se non è affidabile la lunghezza, e ussu da poca distanza

    void InPortaAngolando();  // calcola l'angolo migliore in base all'us_davanti
    void SegnaInPorta(); // va semplicemente avanti
    void giraAdAngolo(int angolo); // da 0 a 179 in senso orario, da -1 a -180 in sento antiorario
    void giraAdAngoloPerSegnare(); // capisce se girare a dx o sinistra per tirare in porta   
    bool aggiusta_pos_per_segnare();
    

    bool vai_a_posizione(int x, int y,bool leggi = true); // funzinona che è una bellezza

    
    bool difendi_porta();   // DA FARE!!  
    
    
    int compass;
private:
    

    sensori s;
   // sensori s;
    
    bool aggiusta_orientamento_PID(int orientamento);
    
    
    /////// PROVA PID CON ROTAZIONE
    float P; //Proporzionale
    float I; //Integrativa
    float D; //Derivativa
    int previousError;// = 0;
    float previousTime;// = time;
    ///////
    
};
#endif
