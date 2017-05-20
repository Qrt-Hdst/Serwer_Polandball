package server_pliki;

import java.io.BufferedReader;
import java.io.FileReader;
import static server_pliki.ParseConfigFile.*;
/*import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;*/

/**
 * klasa obslugujaca zlecenia od klienta otrzymane przez serwer
 */
public class ServerAction {

    /**
     * Napis, zalezny od odpowiedzi serwera
     */

    public static String serverflag_;

    /**
     * funkcja oblsugujaca zadania od klienta
     *
     * @param command zadanie klienta
     * @return odpowiedz serwera
     */
    public static String ServerResponse(String command) {

        String servercommand = command;//przypisanie otrzymanego zadanie do zmiennej
        String servermessage;//wiadomosc zwracana przez metode
        //obsluga zadan na podstawie otrzymanej wiadomosci od klienta
        switch (servercommand) {
            /*case "LOGIN":
                servermessage=login();//wywolanie metody zwracajacaj potwierdzenie o zalogowaniu
                break;*/
            case "GET_HIGHSCORES":
                servermessage = GetHighscores();//wywolanie metody GetHighscores, pobranie listy najlepszych wynikow
                serverflag_ = "HIGHSCORES: ";
                break;
            case "GET_CONFIGFILE":
                servermessage = GetConfigfile();//wywolanie metody ladujacej sparsowane zmienne do napisu-budowanie wiadomosci
                serverflag_ = "CONFIGFILE: ";
                break;
            default:
                servermessage = "INVALID_COMMAND";//w przypadku nieznanego zadania zostanie wyswietlona informacja o
                break;                          //nieznanej komendzie
        }
        return servermessage;//zwrocenie wiadomosci serwera przez metode ServerResponse
    }

    /*
     * funkcja obslugujaca logowanie klientow
     * @return odpowiedz serwera
     */
   /* private static String login(){
        String servermessage;//wiadomsoc serwera
        if(serveron) {//sprawdzenie czy serwer moze przyjac klienta
            servermessage="LOGGED_IN "+clientid+"\n";//w przypadku zaakceptoawnia klienta serwer wysyla odpowiedz
            //LOGGED_IN i informacje o przydzielonym id klienta
            clientid++;//zwiekszenie id kolejnych zalogowanych klientow
        }
        else{
            servermessage="CONNECTION_REJECTED";//odrzucenie polaczenia, w przypadku wylaczenia mozliwosci przyjmowania
        }                                       //klientow
        return servermessage;//zwrocenie wiadomosci serwera przez metode login
    }*/


    /**
     * funkcja wczytujaca liste najlepszych wynikow
     * @return zwraca wiadomosc (tekst) zawierajaca liste najlepszych wynikow
     */

    private static String GetHighscores() {
        //plik tekstowy zawierajacy liste najlepszych wynikow jest pakowany do stringa, aby mozna bylo go wyslac jako
        //wiadomosc tekstowa
        StringBuilder stringbuilder = new StringBuilder();
        String currentline;
        try (
                //wczytywanie kolejnych linijek pliku i dodawanie ich do zmiennej stringbuilder
                //co do pliku tekstowego
                //znak " / " oddziela kazdy wynik rogrywki i nick od reszty
                //znak " _ " oddziela dany wynik od gracza, ktory go uzyskal
                BufferedReader buildreader = new BufferedReader(new FileReader("src\\server_pliki\\highscores.txt"))) {
            while ((currentline = buildreader.readLine()) != null) {
                stringbuilder.append(currentline);
            }
        } catch (Exception e) {
            System.err.println("Nastapil niespodziewany blad");
            System.err.println(e);
        }
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }

    /**
     * funkcja budujaca napis ze zmiennych sparsowanych w klasie ParseConfigfile
     * @return zwraca wiadomosc (tekst) zawierajaca podstawowe parametry aplikacji
     */
    private static String GetConfigfile(){
        //wywolanie metody z klasy parsujacej plik=parsowanie pliku, za kazdym razem, gdy klient zazada danych
        ParseConfigFile.ParseConfig();
        //zmienna, na ktorej bedziemy budowac wiadomsosc tekstowa
        StringBuilder stringbuilder = new StringBuilder();
        //dodanie do bufora napisow wszystkich sparsowanych zmiennych
        for(int i=0;i<configbufor.length;i++){
            stringbuilder.append(configbufor[i]+" ");//kazda liczbe(napis) oddziela spacja
        }
        //usuwamy ostatni " ", zeby latwiej sie obrabialo
        stringbuilder.delete(stringbuilder.length()-1,stringbuilder.length());
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }
}
