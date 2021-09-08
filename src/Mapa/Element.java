package Mapa;

import Infrastruktura.Pruga;
import Infrastruktura.PruzniPrelaz;
import Infrastruktura.Put;
import Infrastruktura.Stanica;
import Lokomotive.Lokomotiva;
import Lokomotive.NaponLokomotiva;
import Lokomotive.PutnickaLokomotiva;
import Vagoni.Vagon;
import Vozila.Automobil;
import Vozila.Kamion;
import Vozila.Vozilo;
import sample.Controller;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Element {

    private int x;
    private int y;
    private ArrayList<Object> objektiNaPolju;
    private int brojObjekata;
    private boolean zakljucanoPolje;

    public Element(int x,int y){
        this.x = x;
        this.y = y;
        objektiNaPolju = new ArrayList<>();
    }
    public void dodajObjekatUListu(Object obj){
        objektiNaPolju.add(obj);
    }
    public Object skiniObjekatSaListe(int index){
        return objektiNaPolju.get(index);
    }

    public void setZakljucanoPolje(boolean zakljucanoPolje) {
        this.zakljucanoPolje = zakljucanoPolje;
    }

    public boolean isZakljucanoPolje() {
        return zakljucanoPolje;
    }

    public void ukloniObjekatSaListe(Object obj){
        objektiNaPolju.remove(obj);
    }
    public ArrayList<Object> getObjektiNaPolju() {
        return objektiNaPolju;
    }

    public boolean elementImaPrugu(){
        for(Object o : objektiNaPolju){
            if(o instanceof Pruga){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaPruzniPrelaz(){
        for(Object o : objektiNaPolju){
            if(o instanceof PruzniPrelaz){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaPut(){
        for(Object o : objektiNaPolju){
            if(o instanceof Put){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaStanicu(){
        for(Object o : objektiNaPolju){
            if(o instanceof Stanica){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaLokomotivu(){
        for(Object o : objektiNaPolju){
            if(o instanceof Lokomotiva){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaVagon(){
        for(Object o : objektiNaPolju){
            if(o instanceof Vagon){
                return true;
            }
        }
        return false;
    }
    public void skiniLokomotivuSaListe(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Lokomotiva){
                iterator.remove();
            }
        }
    }
    public void skiniVagonSaListe(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Vagon){
                iterator.remove();
            }
        }
    }
    public boolean elementImaNaponLokomotivu(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof NaponLokomotiva){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaVozilo(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Vozilo){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaAutomobil(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Automobil){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaKamion(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Kamion){
                return true;
            }
        }
        return false;
    }
    public boolean elementImaElektricnuLokomotivu(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Lokomotiva){
                if(((Lokomotiva) next).getPogonLokomotive().equals(Controller.ELEKTRICNI_POGON))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean elementImaDizelLokomotivu(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Lokomotiva){
                if(((Lokomotiva) next).getPogonLokomotive().equals(Controller.DIZELSKI_POGON))
                {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean elementImaParnuLokomotivu(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Lokomotiva){
                if(((Lokomotiva) next).getPogonLokomotive().equals(Controller.PARNI_POGON))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public PruzniPrelaz getPruzniPrelaz(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        PruzniPrelaz rezultat = new PruzniPrelaz(0,0);
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof PruzniPrelaz){
               rezultat =(PruzniPrelaz) next;
               return rezultat;
            }
        }
        return null;
    }

    public Put getPut(){
        Iterator<Object> iterator = objektiNaPolju.iterator();
        Put rezultat = new Put(0,0,"result");
        while(iterator.hasNext()){
            Object next = iterator.next();
            if(next instanceof Put){
                rezultat =(Put) next;
            }
        }
        return rezultat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return x == element.x &&
                y == element.y;
    }

    @Override
    public String toString() {
        return "Element{" +
                "x=" + x +
                ", y=" + y ;
    }
}
