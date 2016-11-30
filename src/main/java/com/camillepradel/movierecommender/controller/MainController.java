package com.camillepradel.movierecommender.controller;

import java.util.ArrayList;

import com.camillepradel.movierecommender.databases.MongoDB;

import com.camillepradel.movierecommender.databases.MySQL;
import com.camillepradel.movierecommender.databases.Neo4J;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.camillepradel.movierecommender.model.Movie;

@Controller
public class MainController {
	String message = "Welcome to Spring MVC!";
	private MongoDB mongodb;
	private MySQL mysql;
    private Neo4J neo4j;
 
	@RequestMapping("/hello")
	public ModelAndView showMessage(
			@RequestParam(value = "name", required = false, defaultValue = "World") String name) {
		System.out.println("in controller");
 
		ModelAndView mv = new ModelAndView("helloworld");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}

	@RequestMapping("/movies")
	public ModelAndView showMovies(
			@RequestParam(value = "user_id", required = false) Integer userId) {
		ArrayList<Movie> moviesMongoDB;
		ArrayList<Movie> moviesMySQL;
        ArrayList<Movie> moviesNeo4J;


		mysql = new MySQL();
		mongodb = new MongoDB();
        neo4j = new Neo4J();
		if(userId == null){
			moviesMongoDB = mongodb.getAllMovies();
			moviesMySQL = mysql.getAllMovies();
            moviesNeo4J = neo4j.getAllMovies();
            System.out.println("show all Movies");
		}
		else {
			moviesMongoDB = mongodb.getAllMoviesByUserId(userId);
			moviesMySQL = mysql.getMoviesByUserId(userId);
            moviesNeo4J = neo4j.getMoviesByUserId(userId);
            System.out.println("show Movies of user " + userId);
		}





		ModelAndView mv = new ModelAndView("movies");
		mv.addObject("userId", userId);
		mv.addObject("moviesMongoDB", moviesMongoDB);
		mv.addObject("moviesMySQL", moviesMySQL);
        mv.addObject("moviesNeo4J", moviesNeo4J);

        mv.addObject("moviesMongoDBSize", moviesMongoDB.size());
        mv.addObject("moviesMySQLSize", moviesMySQL.size());
        mv.addObject("moviesNeo4JSize", moviesNeo4J.size());


        // Closing connections
        neo4j.close();
		mysql.close();
		return mv;
	}
}
