/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dougl
 */
public class Server implements Runnable {

    private FXMLBasqueteController vi;
    private int i;

    public Server(FXMLBasqueteController view) {
        this.vi = view;
    }

    @Override
    public void run() {
        while (true) {
            vi.mudaMensagem("" + i++);
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
