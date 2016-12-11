package com.camillepradel.movierecommender.databases;



import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MySQL {
    private String url = "jdbc:mysql://localhost:3306/movielens";
    public String getUrl(){
        return url;
    }
    public void setUrl(String pUrl){
        url = pUrl;
    }
    private String login = "root";
    public String getLogin(){
        return login;
    }
    public void setLogin(String pLogin){
        login = pLogin;
    }
    private String password = "";
    public String getPassword(){
        return password;
    }
    public void setPassword(String pPassword){
        password = pPassword;
    }
    private Connection cn = null;
    public Connection getCn(){
        return cn;
    }
    public void setCn(Connection pCn){
        cn = pCn;
    }
    private Statement st = null;
    public Statement getSt(){
        return st;
    }
    public void setSt(Statement pSt){
        st = pSt;
    }
    private ResultSet rs = null;


    public MySQL(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            cn = DriverManager.getConnection(url,login,password);
            st = cn.createStatement();
        }catch(SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Movie> getAllMovies(){
        long startTime = System.nanoTime();
        List<Genre> genres = new ArrayList<Genre>();
        ArrayList<Movie> list = new ArrayList<Movie>();
        String query = "SELECT * FROM movie";
        try{
            rs = st.executeQuery(query);
            while(rs.next()){
                list.add(new Movie(rs.getShort("id"),rs.getString("title"),genres));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        long endTime = System.nanoTime();
        //System.out.println("getAllMovies MySQL : "+((double)(endTime-startTime)/1000000000.0));
        return list;
    }

    public ArrayList<Movie> getMoviesByUserId(int userId){
                List<Genre> genres = new ArrayList<Genre>();
                ArrayList<Movie> list = new ArrayList<Movie>();

                        String query = "SELECT movie.title, movie.id FROM movie , ratings WHERE ratings.id_user="+userId+" AND ratings.id_movie=movie.id";
                try{
                        rs = st.executeQuery(query);
                        while(rs.next()){
                                list.add(new Movie(rs.getShort("id"),rs.getString("title"),genres));
                            }
                   }catch(SQLException e){
                        e.printStackTrace();
                    }


                               return list;
            }

    public void close(){
        try{
            cn.close();
            st.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
