/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Formatter;
import java.util.Locale;

/**
 *
 * @author Terminal 1
 */
public class clsConvertirNumeroAletra {

    public static void main(String Arg[]) {
        //convertNumberToLetter("20.62");
        // System.out.println(convertNumberToLetter("1070,8", "NUEVOS SOLES") + "\n");
    }
    private static final String[] UNIDADES = {"", "UN ", "DOS ", "TRES ",
        "CUATRO ", "CINCO ", "SEIS ", "SIETE ", "OCHO ", "NUEVE ", "DIEZ ",
        "ONCE ", "DOCE ", "TRECE ", "CATORCE ", "QUINCE ", "DIECISEIS",
        "DIECISIETE", "DIECIOCHO", "DIECINUEVE", "VEINTE"};
    private static final String[] DECENAS = {"VENTI", "TREINTA ", "CUARENTA ",
        "CINCUENTA ", "SESENTA ", "SETENTA ", "OCHENTA ", "NOVENTA ",
        "CIEN "};
    private static final String[] CENTENAS = {"CIENTO ", "DOSCIENTOS ",
        "TRESCIENTOS ", "CUATROCIENTOS ", "QUINIENTOS ", "SEISCIENTOS ",
        "SETECIENTOS ", "OCHOCIENTOS ", "NOVECIENTOS "};

    public static String convertNumberToLetter(String number) {
        return number;
    }

    public static String convertNumberToLetter(double number, String letra)
            throws NumberFormatException {
        String converted = new String();
        String uno;
        String dos;
        // Validamos que sea un numero legal

        double doubleNumber = number;
        if (doubleNumber > 999999999) {
            throw new NumberFormatException(
                    "El numero es mayor de 999'999.999, "
                    + "no es posible convertirlo");
        }
        DecimalFormatSymbols simbolos = DecimalFormatSymbols.getInstance(Locale.ENGLISH);
        DecimalFormat formato = new DecimalFormat("######0.00", simbolos);

//        DecimalFormat formato = new DecimalFormat("######0.00");
        String num = formato.format(doubleNumber);

        String splitNumber[] = num.replace('.', '#').split("#");

        // Descompone el trio de millones - 
        int millon = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0], 8))
                + String.valueOf(getDigitAt(splitNumber[0], 7))
                + String.valueOf(getDigitAt(splitNumber[0], 6)));
        if (millon == 1) {
            converted = "UN MILLON ";
        }
        if (millon > 1) {
            converted = convertNumber(String.valueOf(millon)) + "MILLONES ";
        }

        // Descompone el trio de miles 
        int miles = Integer.parseInt(String.valueOf(getDigitAt(splitNumber[0],
                5))
                + String.valueOf(getDigitAt(splitNumber[0], 4))
                + String.valueOf(getDigitAt(splitNumber[0], 3)));
        if (miles == 1) {
            converted += "MIL ";
        }
        if (miles > 1) {
            converted += convertNumber(String.valueOf(miles)) + "MIL ";
        }

        // Descompone el ultimo trio de unidades 
        int cientos = Integer.parseInt(String.valueOf(getDigitAt(
                splitNumber[0], 2))
                + String.valueOf(getDigitAt(splitNumber[0], 1))
                + String.valueOf(getDigitAt(splitNumber[0], 0)));
        if (cientos == 1) {
            converted += "UN";
        }

        if (millon + miles + cientos == 0) {
            converted += "CERO ";
        }
        if (cientos > 1) {
            converted += convertNumber(String.valueOf(cientos));
        }

        converted += "CON ";
        // Descompone los centavos

        if (getDigitAt(splitNumber[1], 1) == 0) {
            uno = "0";
        } else {
            uno = String.valueOf(getDigitAt(splitNumber[1], 1));
        }
        if (getDigitAt(splitNumber[1], 0) == 0) {
            dos = "0";
        } else {
            dos = String.valueOf(getDigitAt(splitNumber[1], 0));
        }
        converted += uno + dos;
        converted += "/100 ";
        converted += letra;

        return converted;
    }

    /**
     * Convierte los trios de numeros que componen las unidades, las decenas y
     * las centenas del numero.
     * <p>
     * Creation date 3/05/2006 - 05:33:40 PM
     *
     * @param number Numero a convetir en digitos
     * @return Numero convertido en letras
     * @since 1.0
     */
    private static String convertNumber(String number) {
        if (number.length() > 3) {
            throw new NumberFormatException(
                    "La longitud maxima debe ser 3 digitos");
        }
        String output = new String();
        if (getDigitAt(number, 2) != 0) {
            output = CENTENAS[getDigitAt(number, 2) - 1];
        }

        int k = Integer.parseInt(String.valueOf(getDigitAt(number, 1))
                + String.valueOf(getDigitAt(number, 0)));

        if (k <= 20) {
            output += UNIDADES[k];
        } else if (k > 30 && getDigitAt(number, 0) != 0) {
            output += DECENAS[getDigitAt(number, 1) - 2] + "Y "
                    + UNIDADES[getDigitAt(number, 0)];
        } else {
            output += DECENAS[getDigitAt(number, 1) - 2]
                    + UNIDADES[getDigitAt(number, 0)];
        }

        // Caso especial con el 100
        if (getDigitAt(number, 2) == 1 && k == 0) {
            output = "CIEN";
        }

        return output;
    }

    /**
     * Retorna el digito numerico en la posicion indicada de derecha a izquierda
     * <p>
     * Creation date 3/05/2006 - 05:26:03 PM
     *
     * @param origin Cadena en la cual se busca el digito
     * @param position Posicion de derecha a izquierda a retornar
     * @return Digito ubicado en la posicion indicada
     * @since 1.0
     */
    private static int getDigitAt(String origin, int position) {
        if (origin.length() > position && position >= 0) {
            return origin.charAt(origin.length() - position - 1) - 48;
        }
        return 0;
    }
}
