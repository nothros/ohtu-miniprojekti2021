package database;

public class LibraryObject {
	private int id;
    private int type;
    private String title;
    private String author;
    private String ISBN;
    private String URL;
    private String course;

    public LibraryObject(int type, String title, String author, String ISBN, String URL, String course) {
        this.type = type;
        this.title = title;
        this. author = author;
        this.ISBN = ISBN;
        this.URL = URL;
        this.course = course;
    }
    
    public LibraryObject(int id, int type, String title, String author, String ISBN, String URL, String course) {
    	this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        this.URL = URL;
        this.course = course;
    }

    @Override
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", title='" + getTitle() + "'" +
            ", author='" + getAuthor() + "'" +
            ", ISBN='" + getISBN() + "'" +
            ", URL='" + getURL() + "'" +
            "}";
    }
    
    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getType(){
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return this.ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getURL() {
        return this.URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
    
    public String getCourse() {
    	return this.course;
    }
    
    public void setCourse(String course) {
    	this.course = course;
    }
    
}