package com.camillepradel.movierecommender.databases;


import com.camillepradel.movierecommender.model.Genre;
import com.camillepradel.movierecommender.model.Movie;
import org.neo4j.driver.v1.*;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.util.ArrayList;
import java.util.List;

public class Neo4J {
    private Driver driver;
    private Session session;

    public Neo4J(){

         driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "04111992" ) );
         session = driver.session();
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
                while ( result.hasNext() )
                    {
                                Record record = result.next();
                    list.add(new Movie(record.get( "id" ).asInt(),record.get( "title" ).asString(),genres));
                }
                return list;
            }

    public void close(){
        session.close();
        driver.close();
    }
}
