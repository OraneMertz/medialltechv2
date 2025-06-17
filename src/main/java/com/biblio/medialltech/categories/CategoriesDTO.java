package com.biblio.medialltech.categories;

public class CategoriesDTO {
    private Long id;
    private String name;

    public CategoriesDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoriesDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}