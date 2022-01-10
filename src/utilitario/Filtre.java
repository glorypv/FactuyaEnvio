/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilitario;

import java.io.*;

/**
 *
 * @author Soledad Chuquipalla
 */
class Filtre implements FilenameFilter {

    String extension;

    public Filtre(String string) {
        this.extension = string;

    }

    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }

}
