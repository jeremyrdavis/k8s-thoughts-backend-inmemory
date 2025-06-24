package com.redhat.k8sthoughts;

import java.time.LocalDate;

public class ThoughtOfTheDay {
    public String thought;

    public String author;

    public LocalDate day;

    public ThoughtOfTheDay() {
    }

    public ThoughtOfTheDay(String thought, String author, String day) {
        this.thought = thought;
        this.author = author;
        this.day = LocalDate.parse(day);
    }
    
    public ThoughtOfTheDay(String thought, String author, LocalDate day) {
        this.thought = thought;
        this.author = author;
        this.day = day;
    }

    public String getThought() {
        return thought;
    }

    public String getAuthor() {
        return author;
    }

    public LocalDate getDay() {
        return day;
    }

    @Override
    public String toString() {
        return "ThoughtOfTheDay{" +
                "thought='" + thought + '\'' +
                ", author='" + author + '\'' +
                ", day=" + day +
                '}';
    }
}
 
