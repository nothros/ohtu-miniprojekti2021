package bookcase.domain;

import java.util.List;
import database.LibraryObject;
import database.LibraryObjectDAO;

public class LibraryService {
    private LibraryObjectDAO libraryDao;

    public LibraryService(LibraryObjectDAO libraryDao){
        this.libraryDao = libraryDao;
    }

    public boolean createLibraryObject(int laji, String otsikko, String kirjoittaja, String ISBN, String URL){
        if (otsikko.isEmpty() || kirjoittaja.isEmpty() || ISBN.isEmpty()) {
            return false;
        }
        if (!libraryDao.isUnique(ISBN)) {
        	System.out.println("Not a unique ISBN.");
    		return false;
    	}
        if (!libraryDao.isValidISBN(ISBN)) {
    		System.out.println("Not a valid ISBN.");
    		return false;
    	}
        LibraryObject libraryObject = new LibraryObject(laji, otsikko, kirjoittaja, ISBN, URL);
        return libraryDao.insertLibrary(libraryObject);
    }

    public List<LibraryObject> getAllObjects(){
        return libraryDao.getAll();
    }
    
    public List<LibraryObject> getAllObjects(String s){
        return libraryDao.getAll(s);
    }

    public void createNewTablesIfNotExists(){
        libraryDao.createNewTable();
    }

}