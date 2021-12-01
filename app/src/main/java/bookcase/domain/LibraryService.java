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
        if (!libraryDao.isUnique(ISBN,"libraryObjects")) {
        	System.out.println("Ei ole uniikki ISBN.");
    		return false;
    	}
        if (!libraryDao.isValidISBN(ISBN)) {
    		System.out.println("Ei ole validi ISBN.");
    		return false;
    	}
        LibraryObject libraryObject = new LibraryObject(laji, otsikko, kirjoittaja, ISBN, URL);
        return libraryDao.insert(libraryObject);
    }

    public List<LibraryObject> getAllObjects(){
        return libraryDao.getAll();
    }
    
    public List<LibraryObject> getAllObjects(String s){
        return libraryDao.getAll(s);
    }

}