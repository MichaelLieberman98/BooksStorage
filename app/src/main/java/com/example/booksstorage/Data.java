package com.example.booksstorage;
import static java.util.Map.entry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Stack;

public class Data {
    private static Data instance;
    public static Data getInstance(){
        if (instance == null){
            instance = new Data();
        }
        return instance;
    }


    enum BookReadStatus {
        TOREAD,
        READ,
        UNKNOWN
    }

    enum Activity{
        MAIN,
        READ,
        TOREAD,
        APIRESULTS,
        DETAILS
    }



    private Stack<Activity> activityStack = new Stack<>();
    public Stack<Activity> getActivityStack() {
        return this.activityStack;
    }
    public void setActivityStack(Stack<Activity> activityStack) {
        this.activityStack = activityStack;
    }



    public String bookSearchURL = "https://www.googleapis.com/books/v1/volumes?q=";

    private String bookSearch = "";
    public String getBookSearch() {
        return bookSearch;
    }
    public void setBookSearch(String bookSearch) {
        this.bookSearch = bookSearch;
    }

    private ArrayList<Book> booksFromAPI = new ArrayList<>();
    private ArrayList<Book> booksToRead = new ArrayList<>();
    private ArrayList<Book> booksAlreadyRead = new ArrayList<>();

    public ArrayList<Book> getBooksFromAPI() {
        return booksFromAPI;
    }
    public void setBooksFromAPI(ArrayList<Book> booksFromAPI) {
        this.booksFromAPI = booksFromAPI;
    }
    public ArrayList<Book> getBooksToRead() {
        return booksToRead;
    }
    public void printAllBooks(){
        System.out.println();
        System.out.print("API BOOKS:   ");
        for (int i = 0; i < getBooksFromAPI().size(); i++){
            System.out.print(getBooksFromAPI().get(i).getTitle() + " , ");
        }
        System.out.println();
        System.out.print("TOREAD BOOKS:   ");
        for (int i = 0; i < getBooksToRead().size(); i++){
            System.out.print(getBooksToRead().get(i).getTitle() + " , ");
        }
        System.out.println();
        System.out.print("ALREADYREAD BOOKS:   ");
        for (int i = 0; i < getBooksAlreadyRead().size(); i++){
            System.out.print(getBooksAlreadyRead().get(i).getTitle() + " , ");
        }
        System.out.println();
    }




    public void setBooksToRead(ArrayList<Book> booksToRead) {
        this.booksToRead = booksToRead;
    }
    public ArrayList<Book> getBooksAlreadyRead() {
        return booksAlreadyRead;
    }
    public void setBooksAlreadyRead(ArrayList<Book> booksAlreadyRead) {
        this.booksAlreadyRead = booksAlreadyRead;
    }


    private Book clickedBook = new Book();

    public Book getClickedBook() {
        return clickedBook;
    }

    public void setClickedBook(Book clickedBook) {
        this.clickedBook = clickedBook;
    }



    public void sortBooksAlphabetically(ArrayList<Book> books){
        Collections.sort(books, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
    }
}