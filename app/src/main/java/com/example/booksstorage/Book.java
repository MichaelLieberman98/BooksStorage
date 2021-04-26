package com.example.booksstorage;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Arrays;


import java.util.HashMap; //languages

public class Book {
    private String id;
    private Bitmap cover;
    private String coverURL;
    private String title;
    private String description;
    private ArrayList<String> authors;

    private String sPublishDate; //consider making a nested object which represents "dates" more abstractly / vividly
    private Date publishDate;

    private String publisher;
    private String purchaseLink;

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", cover=" + cover +
                ", coverURL='" + coverURL + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", authors=" + authors +
                ", sPublishDate='" + sPublishDate + '\'' +
                ", publishDate=" + publishDate +
                ", publisher='" + publisher + '\'' +
                ", purchaseLink='" + purchaseLink + '\'' +
                ", status=" + status +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public String getCoverURL() {
        return coverURL;
    }

    public void setCoverURL(String coverURL) {
        this.coverURL = coverURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<String> authors) {
        this.authors = authors;
    }

    public String getsPublishDate() {
        return sPublishDate;
    }

    public void setsPublishDate(String sPublishDate) {
        this.sPublishDate = sPublishDate;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPurchaseLink() {
        return purchaseLink;
    }

    public void setPurchaseLink(String purchaseLink) {
        this.purchaseLink = purchaseLink;
    }

    public Data.BookReadStatus getStatus() {
        return status;
    }

    public void setStatus(Data.BookReadStatus status) {
        this.status = status;
    }

    //LANGUAGE STUFF
    private HashMap<String, String> LANGtitle;
    private HashMap<String, String> LANGdescription;
    private HashMap<String, ArrayList<String>> LANGauthors;
    private HashMap<String, String> LANGpublisher;
    ////////////////

    private Data.BookReadStatus status;

    public Book(){
        this.id = "";
        this.cover = null;
        this.coverURL = "";
        this.title = "";
        this.description = "";
        this.authors = new ArrayList<String>();
        this.publishDate = new Date(2000, 1, 1);
        this.publisher = "";
        this.purchaseLink = "";

        this.status = Data.BookReadStatus.READ;
    }

    public Book(String id, Bitmap cover, String coverURL, String title, String description, ArrayList<String> authors, String sPublishDate, Date publishDate, String publisher, String purchaseLink, Data.BookReadStatus status){
        this.id = id;
        this.cover = cover;
        this.coverURL = coverURL;
        this.title = title;
        this.description = description;
        this.authors = authors;

        this.sPublishDate = sPublishDate;
        this.publishDate = publishDate;

        this.publisher = publisher;
        this.purchaseLink = purchaseLink;

        this.status = status;
    }
}