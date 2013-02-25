package unlock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ben Rhine
 */
public class Unlock {
    // Url to open
    private static final String testUrl   = "http://simple-snow-3171.herokuapp.com/?key=";
    
    // Known first character
    private static final String firstChar = "Q";
    private static boolean success = false;
    private static String successUrl;
    
    private static boolean testUrlUnlock(String strUrl) {
        URL url;
        HttpURLConnection conn;
        Integer statusCode = null;
        BufferedReader in;
        try {
            url = new URL(strUrl);
            System.out.println(url.toString());
            try {
                in = new BufferedReader(new InputStreamReader(url.openStream()));
                
                String inputLine;
                
                // Read response from URL
                while((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    
                    // Return and continue if denied
                    if (inputLine.contains("KEY DENIED")) {
                        System.out.println("Denied");
                        in.close();
                        return false;
                    // Return and exit once key found.
                    } else {
                        System.out.println("YAY" + url.toString());
                        in.close();
                        success = true;
                        return true;
                    }            
                }
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Unlock.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Unlock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private static void findKeyPermutation(char[] base, StringBuilder candidateKey, int strLength) {
        if (candidateKey.length() == strLength) {
            return;
        }

        for (int i = 0; i < base.length; i++) { 
               //i is the next char to add
               candidateKey.append(base[i]);               
               
               // Only attempt to unlock if key contains a 9
               if (candidateKey.toString().contains("9")) {
                   // Print candidate key.
                   System.out.println(candidateKey);
                   if (Unlock.testUrlUnlock(Unlock.testUrl + Unlock.firstChar + candidateKey)) {
                       successUrl = Unlock.testUrl + Unlock.firstChar + candidateKey;
                       
                       //break loop if successfull key is found.
                       break;
                   }                  
               }
               //recursively find permutations for the remianing elements
               findKeyPermutation(base, candidateKey, strLength);
               //clean up 
               candidateKey.deleteCharAt(candidateKey.length()-1);
        }

    }
    
    public static void findKeyPermutation(char[] base, int strLength) { 
        findKeyPermutation(base, new StringBuilder(), strLength); 
    }
    
    /**
     * @param args the command line arguments
     * 
     * Main method for testing/running simple unlock app.
     */
    public static void main(String[] args) {
        // Declare class object
        Unlock ul = new Unlock();
        
        // Local variables
        String strUrl;        
        Integer status = null;
        Integer trials = 0;
        
        // All characters to be used to generate random alpha-numeric key
        char[] base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        
        // While successfull key not found.
        while (success != true) {
            Unlock.findKeyPermutation(base, trials);
            trials++;
        }
        // Print full URL with successful key and the number of trials
        System.out.println("Success: " + successUrl + ". Attempts: " + trials);
    }
}
