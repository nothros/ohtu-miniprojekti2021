package tietokantaDemo;

public class libraryObject {
    private int laji;
    private String otsikko;
    private String kirjoittaja;
    private String ISBN;
    private String URL;

    public libraryObject(int laji, String otsikko, String kirjoittaja, String ISBN, String URL) {
        this.laji = laji;
        this.otsikko = otsikko;
        this. kirjoittaja = kirjoittaja;
        this.ISBN = ISBN;
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "{" +
            " laji='" + getLaji() + "'" +
            ", otsikko='" + getOtsikko() + "'" +
            ", kirjoittaja='" + getKirjoittaja() + "'" +
            ", ISBN='" + getISBN() + "'" +
            ", URL='" + getURL() + "'" +
            "}";
    }
    /*
        1 = Kirja
        2 = Blogi
        3 = Podi
    */
    public int getLaji(){
        return this.laji;
    }

    public void setLaji(int laji) {
        this.laji = laji;
    }

    public String getOtsikko() {
        return this.otsikko;
    }

    public void setOtsikko(String otsikko) {
        this.otsikko = otsikko;
    }

    public String getKirjoittaja() {
        return this.kirjoittaja;
    }

    public void setKirjoittaja(String kirjoittaja) {
        this.kirjoittaja = kirjoittaja;
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


}
