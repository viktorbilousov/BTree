package com.company.tree;

import java.util.Collection;

public interface iTree<T> {

    int size();         // anzahl  Knoten
    int depth();        // max Höhe des Baums
    boolean isEmpty();  // size == 0

    void add(T o);   // fügt neues Objekt 'o'.
    // gibt 'false' aus, wenn der Baum schon Obj. 'o' enthaltet, anderenfalls 'true'

    void addAll(Collection<T> c); // fügt die Obj. aus der Liste 'c',
    //gibt 'false' aus, wenn eins oder mehr Obj. aus der Liste im Baum nicht enthaltet werden, anderenfalls 'true'

    boolean remove(T o); //löscht des obj.'o' aus dem Baum.
    // gibt 'false' aus, wenn  der Baum kein Obj. enthaltet, anderenfalls 'true'

    boolean removeAll(Collection<T> c);// löscht alle Obj, die in die Liste 'c' gibt, aus dem Baum.
    //gibt 'true' aus, wenn der Baum alle Obj. aus der Liste 'c' enthaltet, anderenfalls 'false'

    boolean isContain(T o); // gibt 'true' aus, wenn der Baum das Obj. 'o' enthaltet, anderfallse 'false'

    iTree copy(); // Kopierkonstruktor

    void clean(); // löscht alle Objekte aus dem Baum

};