public class Book {
    private String Autore,Titolo,ISBN,Prestito;

    public String getAutore() {
        return Autore;
    }

    public void setAutore(String autore) {
        Autore = autore;
    }

    public String getTitolo() {
        return Titolo;
    }

    public void setTitolo(String titolo) {
        Titolo = titolo;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setPrestito(String Prestito) {
        this.Prestito = Prestito;
    }
    public String getPrestito() {
        return Prestito;
    }
}
