package com.camillepradel.movierecommender.benchmarker;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by WWW on 11/12/2016.
 */
public class TestGetAllMoviesMongoDB {
    public static void main(String[] args) {

        String urlStart = "http://localhost:8080/getAllNeo4J";
        int nbIterations = 300;
        int userId= 0;
        long startTime = System.nanoTime();

        for (int i= 0; i < nbIterations; i++) {

            URL u;
            InputStream is = null;
            DataInputStream dis;

            try
            {
                //u = new URL(urlStart + userId);
                u = new URL(urlStart);
                is = u.openStream();
                dis = new DataInputStream(new BufferedInputStream(is));
                while ((dis.readLine()) != null)
                {
                }
                System.out.println(i + "/" + nbIterations);
            }
            catch (MalformedURLException mue)
            {
                System.err.println("Ouch - a MalformedURLException happened.");
                mue.printStackTrace();
                System.exit(2);
            }
            catch (IOException ioe)
            {
                System.err.println("Oops- an IOException happened.");
                ioe.printStackTrace();
                System.exit(3);
            }
            finally
            {
                try
                {
                    is.close();
                }
                catch (IOException ioe)
                {
                }
            }
        }

        long endTime = System.nanoTime();
        System.out.println("Time to get " + nbIterations + " times getAllMovies with MongoDB page: " + (endTime - startTime) + "ns");

    }
}
