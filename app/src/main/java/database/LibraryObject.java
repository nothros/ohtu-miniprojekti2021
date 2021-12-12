package database;

public class LibraryObject {

    private int id;
    private String type;
    private String title;
    private String author;
    private String isbn;
    private String url;
    private String course;
    private String comment;

    public LibraryObject(String type, String title, String author, String isbn, String url, String course, String comment) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.url = url;
        this.course = course;
        this.comment = comment;
    }

    public LibraryObject(int id, String type, String title, String author, String isbn, String url, String course, String comment) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.url = url;
        this.course = course;
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "type: " + getType()
                + ", title: " + getTitle()
                + ", author: " + getAuthor()
                + ", ISBN: " + getISBN()
                + ", URL: " + getURL()
                + ", course: " + getCourse()
        		+ ", comment: " + getComment();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
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
        return this.isbn;
    }

    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public String getURL() {
        return this.url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public String getCourse() {
        return this.course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
    
    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
