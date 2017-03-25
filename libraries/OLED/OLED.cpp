#include "OLED.h"

#include <Wire.h>

OLED::OLED()
{
	oled = U8GLIB_SSD1306_128X64(U8G_I2C_OPT_NONE);
}
void OLED::scrivi(int n, int pos)
{
if (pos >= OLED_ALTO && pos <= OLED_CENTRO)
  {
    n_oled[pos] = n;
    if (pos != OLED_CENTRO && pos != OLED_CENTRO_GRANDE) n_oled[OLED_CENTRO] = n_oled[OLED_CENTRO_GRANDE] = 0;
  }
}
void OLED::display()
{
	oled.firstPage();  
  do {
     oled.setFont(u8g_font_fub20n);//u8g_font_unifont);
   if (n_oled[OLED_CENTRO])
   {
      oled.setFont(u8g_font_fub49n);//u8g_font_unifont);
      oled.drawStr( 0, 64,  String(n_oled[OLED_CENTRO]).c_str());
      continue;
   }
   if (n_oled[OLED_CENTRO_GRANDE])
   {
		oled.drawStr( 0, 64,  String(n_oled[OLED_CENTRO_GRANDE]).c_str());
		continue;
   }
   if (n_oled[OLED_ALTO])
    oled.drawStr(44, 20,  String(n_oled[OLED_ALTO]).c_str());
   if (n_oled[OLED_SINISTRA])
    oled.drawStr( 0, 44,  String(n_oled[OLED_SINISTRA]).c_str());
   if (n_oled[OLED_DESTRA])
      oled.drawStr( 70, 44, String(n_oled[OLED_DESTRA]).c_str());
   if (n_oled[OLED_BASSO])
      oled.drawStr( 44, 64,  String(n_oled[OLED_BASSO]).c_str());
    
  } while( oled.nextPage() );
}