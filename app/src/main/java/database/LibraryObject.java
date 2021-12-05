package database;

public class LibraryObject {

    private int id;
    private int type;
    private String title;
    private String author;
    private String isbn;
    private String url;
    private String course;

    public LibraryObject(int type, String title, String author, String isbn, String url, String course) {
        this.type = type;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.url = url;
        this.course = course;
    }

    public LibraryObject(int id, int type, String title, String author, String isbn, String url, String course) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.url = url;
        this.course = course;
    }

    @Override
    public String toString() {
        String type = "";
        if (getType() == 1) {
            type = "Book";
        }
        if (getType() == 2) {
            type = "Blogpost";
        }
        if (getType() == 3) {
            type = "Podcast";
        }
        return " type: " + type
                + ", title: " + getTitle()
                + ", author: " + getAuthor()
                + ", ISBN: " + getISBN()
                + ", URL: " + getURL()
                + ", course: " + getCourse();
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
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

}
