/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacfize.gatewrapper;

import static spark.Spark.*;

// Default Port : 4567 http://sparkjava.com/documentation.html#embedded-web-server
/**
 *
 * @author jacquesfize
 */
public class Server {

   public static void main(String[] args) throws Exception {
        AnnieNER ner=new AnnieNER();
        int port_=4035;
        
        port(port_);
        post("/hello", (req, res) -> ner.getAnnotation(req.body()));
        System.out.println("AnnieNER Server has been associated to localhost:"+port_);
   }
    
}
