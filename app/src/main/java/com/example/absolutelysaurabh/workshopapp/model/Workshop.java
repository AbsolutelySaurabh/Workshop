package com.example.absolutelysaurabh.workshopapp.model;

public class Workshop {

    public String title;
    public String description;
    public int is_applied;
    public int url_to_image;
    public String workshop_id;

    public Workshop(String title, String description, int url_to_image, int is_applied, String id){

        this.title = title;
        this.description = description;
        this.url_to_image = url_to_image;
        this.is_applied = is_applied;
        this.workshop_id = id;
    }


    public Workshop(String title, String description, int url_to_image, String id){

        this.title = title;
        this.description = description;
        this.url_to_image = url_to_image;
        this.is_applied = is_applied;
        this.workshop_id = id;
    }

    public String getTitle(){

        return title;
    }
    public String getDescription(){

        return description;
    }
    public int getUrl_to_image(){

        return url_to_image;
    }

    public int getIs_applied(){

        return is_applied;
    }
    public String getWorkshop_id(){
        return workshop_id;
    }

}
