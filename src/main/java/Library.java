import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*import java.text.SimpleDateFormat;  
import java.util.Date; 
import java.util.*;
import java.text.*;*/

@Path("/book")
public class Library {
    private final String error = "Server error, contact administrators";
    private boolean checkParams(String isbn,String autore, String titolo){
        return (isbn == null || isbn.trim().length() == 0) || (titolo == null || titolo.trim().length() == 0) || (autore == null || autore.trim().length() == 0);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(){
        final String QUERY = "SELECT * FROM Libri";
        final List<Book> books = new ArrayList<>();
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            ResultSet results =  pstmt.executeQuery();
            while (results.next()){
                Book book = new Book();
                book.setTitolo(results.getString("Titolo"));
                book.setAutore(results.getString("Autore"));
                book.setISBN(results.getString("ISBN"));
                books.add(book);

            }
        }catch (SQLException e){
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson(books);
        return Response.status(200).entity(obj).build();
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@FormParam("ISBN") String isbn,
                           @FormParam("Titolo")String titolo,
                           @FormParam("Autore") String autore){
        if(checkParams(isbn, titolo, autore)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "UPDATE Libri SET Titolo = ?, Autore = ? WHERE ISBN = ?";
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0]);//, data[1], data[2]);
                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            pstmt.setString(1,titolo);
            pstmt.setString(2,autore);
            pstmt.setString(3,isbn);
            pstmt.execute();
        }catch (SQLException e){
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " modificato con successo");
        return Response.ok(obj,MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response create(@FormParam("ISBN") String isbn,
                           @FormParam("Titolo")String titolo,
                           @FormParam("Autore") String autore){
        if(checkParams(isbn, titolo, autore)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "INSERT INTO Libri(ISBN,Titolo,Autore) VALUES(?,?,?)";
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            pstmt.setString(1,isbn);
            pstmt.setString(2,autore);
            pstmt.setString(3,titolo);
            pstmt.execute();
        }catch (SQLException e){
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " aggiunto con successo");
        return Response.ok(obj,MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response update(@FormParam("ISBN") String isbn){
        if(isbn == null || isbn.trim().length() == 0){
            String obj = new Gson().toJson("ISBN must be valid");
            return Response.serverError().entity(obj).build();
        }
        final String QUERY = "DELETE FROM Libri WHERE ISBN = ?";
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0]);
                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            pstmt.setString(1,isbn);
            pstmt.execute();
        }catch (SQLException e){
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Libro con ISBN:" + isbn + " eliminato con successo");
        return Response.ok(obj,MediaType.APPLICATION_JSON).build();
    }


    private boolean checkParams2(int ID,int IDLibro, int IDUtente,String DataInizio,String DataFine){
        return (ID == 0 || IDLibro == 0 || IDUtente == 0 || (DataInizio == null || DataInizio.trim().length() == 0) || (DataFine == null || DataFine.trim().length() == 0));
    }

    @POST
    @Path("/newLead")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addLead(@FormParam("ID") int ID,
                            @FormParam("IDLibro") int IDLibro,
                           @FormParam("IDUtenti") int IDUtenti,
                           @FormParam("DataInizio") String DataInizio,
                           @FormParam("DataFine") String DataFine){
        if(checkParams2(ID,IDLibro, IDUtenti, DataInizio,DataFine)) {
            String obj = new Gson().toJson("Parameters must be valid");
            return Response.serverError().entity(obj).build();
        }  
        /*Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(DataInizio); 
        Date DataF = date1.add(Calendar.MONTH, 1);*/
        final String QUERY = "INSERT INTO Libri(ID,IDLibro,IDUtenti,DataInizio,DataFine) VALUES(?,?,?,?,?)";
        final String[] data = Database.getData();
        try(

                Connection conn = DriverManager.getConnection(data[0]);

                PreparedStatement pstmt = conn.prepareStatement( QUERY )
        ) {
            pstmt.setString(1,Integer.toString(IDLibro));
            pstmt.setString(2,Integer.toString(IDUtenti));
            pstmt.setString(3,DataInizio);
            pstmt.setString(4,DataFine);
            pstmt.execute();
        }catch (SQLException e){
            e.printStackTrace();
            String obj = new Gson().toJson(error);
            return Response.serverError().entity(obj).build();
        }
        String obj = new Gson().toJson("Prestito del libro con id:" + IDLibro + " eseguito con successo");
        return Response.ok(obj,MediaType.APPLICATION_JSON).build();
    }
}
