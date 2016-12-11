package com.camillepradel.movierecommender.databases;


import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Movie;
import com.camillepradel.movierecommender.model.Rating;
import org.neo4j.driver.v1.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Neo4J {
    private Driver driver;
    private Session session;

    public Neo4J(){

         driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "04111992" ) );
         session = driver.session();
    }


    public Boolean isRated(int userId, long movieId){

        ArrayList<Rating> list = new ArrayList<Rating>();
        StatementResult result = session.run( "MATCH (n:User {id:"+userId+"})-[r]->(m:Movie {id:"+movieId+"}) RETURN m.id AS idMovie, m.title AS title, m.id AS id,r.note AS note" );

        while ( result.hasNext() )
        {
            Record record = result.next();
            list.add(new Rating(null,userId,record.get("note").asInt()));
        }
        if(list.size()==0) return false;
        else return true;
    }

    public void editRate(int userId, long movieId, int nouvelleNote){
        session.run( "MATCH (n:User {id:"+userId+"})-[r]->(m:Movie {id:"+movieId+"}) SET r.note="+nouvelleNote);
    }
    public void createRate(int userId, long movieId, int nouvelleNote){
        int timestamp = (int) ((new Date()).getTime()/1000);
        session.run("MATCH (u:User{id:"+userId+"})-[r:RATED]->(m:Movie{id:"+movieId+"})"
                +" SET r.note="+nouvelleNote+", r.timestamp="+timestamp);
    }



    public ArrayList<Movie> getAllMovies(){
        ArrayList<Movie> list = new ArrayList<Movie>();
        List<Genre> genres = new ArrayList<Genre>();


        StatementResult result = session.run( "MATCH (m:Movie) RETURN m.title AS title, m.id AS id" );
        while ( result.hasNext() )
        {
            Record record = result.next();
            list.add(new Movie(record.get( "id" ).asInt(),record.get( "title" ).asString(),genres));
        }
        return list;
    }

    public  ArrayList<Movie> getMoviesByUserId(int userId){
                ArrayList<Movie> list = new ArrayList<Movie>();
                List<Genre> genres = new ArrayList<Genre>();
                StatementResult result = session.run( "MATCH (n:User {id:"+userId+"})-[r]->(m:Movie) RETURN m.title AS title, m.id AS id" );
                while ( result.hasNext() ){
                    Record record = result.next();
                    list.add(new Movie(record.get( "id" ).asInt(),record.get( "title" ).asString(),genres));
                }
                return list;
    }


    public ArrayList<Rating> getRatingByIdUser(int idUser){
        ArrayList<Rating> list = new ArrayList<Rating>();
        List<Genre> genres = new ArrayList<Genre>();
        StatementResult result = session.run( "MATCH (n:User {id:"+idUser+"})-[r]->(m:Movie) RETURN m.id AS idMovie, m.title AS title, m.id AS id,r.note AS note" );
        while ( result.hasNext() )
        {
            Record record = result.next();
            list.add(new Rating(new Movie(record.get("idMovie").asInt(),record.get("title").asString(),genres),idUser,record.get("note").asInt()));
        }
        return list;
    }


    public void close(){
        session.close();
        driver.close();
    }

    public ArrayList<Rating> recommandations(int idUser){
        ArrayList<Rating> list = new ArrayList<Rating>();
        List<Genre> genres = new ArrayList<Genre>();

        StatementResult result = session.run( "MATCH (target_user:User {id : "+idUser+"})-[:RATED]->(m:Movie) <-[:RATED]-(other_user:User)\n" +
                "WITH other_user, count(distinct m.title) AS num_common_movies, target_user\n" +
                "ORDER BY num_common_movies DESC\n" +
                "LIMIT 1\n" +
                "MATCH (other_user:User)-[rat_other_user:RATED]->(m2:Movie)\n" +
                "WHERE NOT ((target_user:User {id : "+idUser+"})-[:RATED]->(m2:Movie))\n" +
                "RETURN m2.title AS rec_movie_title,m2.id AS idMovie, rat_other_user.note AS rating,\n" +
                "other_user.id AS watched_by\n" +
                "ORDER BY rat_other_user.note DESC" );
        while ( result.hasNext() ){
            Record record = result.next();
            list.add(new Rating(new Movie(record.get( "idMovie" ).asInt(),record.get( "rec_movie_title" ).asString(),genres),record.get( "watched_by" ).asInt()));
        }
        return list;
    }
}
