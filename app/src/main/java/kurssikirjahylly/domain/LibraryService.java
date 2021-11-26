package kurssikirjahylly.domain;

import java.util.List;
import tietokantaDemo.LibraryObject;
import tietokantaDemo.LibraryObjectDAO;

public class LibraryService {
    private LibraryObjectDAO libraryDao;

    public LibraryService(LibraryObjectDAO libraryDao){
        this.libraryDao = libraryDao;
    }

    public boolean createLibraryObject(int laji, String otsikko, String kirjoittaja, String ISBN, String URL){
        if (otsikko.isEmpty() || kirjoittaja.isEmpty() || ISBN.isEmpty()) {
            return false;
        }
        LibraryObject libraryObject = new LibraryObject(laji, otsikko, kirjoittaja, ISBN, URL);
        libraryDao.insert(libraryObject);
        return true;
    }

    public List<LibraryObject> getAllObjects(){
        return libraryDao.getAll();
    }
}