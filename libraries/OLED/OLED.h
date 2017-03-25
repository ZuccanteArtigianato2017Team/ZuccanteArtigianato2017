
#ifndef _OLED_LIB_H
#define _OLED_LIB_H

#if defined(ARDUINO) && ARDUINO >= 100
#include "Arduino.h"
#else
#include "WProgram.h"
#endif

#include "U8glib.h"

#define OLED_ALTO 0
#define OLED_SINISTRA 1
#define OLED_DESTRA 2
#define OLED_BASSO 3
#define OLED_CENTRO_GRANDE 4
#define OLED_CENTRO 5

class OLED
{
public:
	OLED();
	OLED(int address); 
	void scrivi(int n, int pos);
	void display();
private:
    int n_oled[5]; 
  	U8GLIB_SSD1306_128X64 oled;
};

#endif
