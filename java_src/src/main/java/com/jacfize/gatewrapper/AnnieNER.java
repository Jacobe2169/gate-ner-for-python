/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacfize.gatewrapper;

import gate.*;
import gate.creole.ANNIEConstants;
import gate.creole.ExecutionException;
import gate.util.persistence.PersistenceManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
/**
 *
 * @author jacquesfize
 */
public class AnnieNER {

    private final LanguageAnalyser controller;
    
    private HashMap<String,String> unified_ner_rep= new HashMap<>();
            
    public AnnieNER() throws Exception {
        /*
            _unified_tag = {
        "place": "LOC",
        "org": "ORG",
        "pers": "PERS"
    }
        */
        this.unified_ner_rep.put("pers", "PERS");
        this.unified_ner_rep.put("org", "ORG");
        this.unified_ner_rep.put("place", "LOC");
        
        Gate.setGateHome(new File("/Applications/GATE_Developer_8.4.1"));
        Gate.init();
        
        controller = (LanguageAnalyser) PersistenceManager
                .loadObjectFromFile(new File(new File(Gate.getPluginsHome(),
                        ANNIEConstants.PLUGIN_DIR), ANNIEConstants.DEFAULT_FILE));
    }
    
    public String getAnnotation(String text) throws Exception{
        String output = "";
        
        Corpus corpus = Factory.newCorpus("corpus");
        Document document = Factory.newDocument(text);
        corpus.add(document); controller.setCorpus(corpus); 
        controller.execute();

        AnnotationSet ann_data = document.getAnnotations().get(new HashSet<>(Arrays.asList("Person", "Organization", "Location")));
        for ( Annotation a:  ann_data){
             output+=""+Utils.stringFor(document, a)+""
                     + "\t"+ this.translateTag(a.getType())+"\t"+a.getStartNode().getOffset().toString()+""
                     + "\t"+a.getEndNode().getOffset().toString()+"\n";
             //System.out.println(a.getStartNode().getOffset()+" "+ a.getEndNode().getOffset());
        }
               
        Factory.deleteResource(document); Factory.deleteResource(corpus);
        return output.trim();
    }
    
    private String translateTag(String tag){
        switch (tag){
            case "Person":
                return this.unified_ner_rep.get("pers");
            case "Location":
                return this.unified_ner_rep.get("place");
            case "Organization":
                return this.unified_ner_rep.get("org");
        }
        return null;
    }
    
    
    
}
