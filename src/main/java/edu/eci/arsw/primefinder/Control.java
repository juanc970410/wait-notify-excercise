/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.primefinder;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 */
public class Control extends Thread {
    
    private final static int NTHREADS = 3;
    private final static int MAXVALUE = 30000000;
    private final static int TMILISECONDS = 5000;
    private long stopTime = 0;

    private final int NDATA = MAXVALUE / NTHREADS;
    
    public Object lock = new Object();

    private PrimeFinderThread pft[];
    
    private Control(long time) {
        super();
        this.pft = new  PrimeFinderThread[NTHREADS];
        this.stopTime = time;
        
        int i;
        for(i = 0;i < NTHREADS - 1; i++) {
            PrimeFinderThread elem = new PrimeFinderThread(i*NDATA, (i+1)*NDATA);
            pft[i] = elem;
        }
        pft[i] = new PrimeFinderThread(i*NDATA, MAXVALUE + 1);
    }
    
    public static Control newControl(long tiempo) {
        return new Control(tiempo);
    }

    @Override
    public void run() {
        for(int i = 0;i < NTHREADS;i++ ) {
            pft[i].setLock(lock);
            pft[i].start();
        }
        try {
            sleep(1000);
            for (int i = 0; i < pft.length; i++) {
                pft[i].setDuerma(true);   
                
            }
            for (int i = 0; i < pft.length; i++) {
                System.out.println(pft[i].getPrimes().size());
            }
            synchronized(lock){
                lock.notifyAll();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
        int finals = 0;
        for (int i = 0; i < pft.length; i++) {
            finals+=pft[i].getPrimes().size();
        }
        System.out.println("tamano final: "+finals);
    }
    
}
