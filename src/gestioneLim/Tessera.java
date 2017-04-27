/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gestioneLim;

/**
 *
 * @author Marco
 */
public class Tessera {
    
    private final boolean SX;
    private final boolean DX;
    private final boolean GIU;
    private final boolean SU;

    public Tessera(boolean SX, boolean DX, boolean GIU, boolean SU) {
        this.SX = SX;
        this.DX = DX;
        this.GIU = GIU;
        this.SU = SU;
    }

    public boolean getSX() {
        return SX;
    }

    public boolean getDX() {
        return DX;
    }

    public boolean getGIU() {
        return GIU;
    }

    public boolean getSU() {
        return SU;
    }
    
    
    
    
}
