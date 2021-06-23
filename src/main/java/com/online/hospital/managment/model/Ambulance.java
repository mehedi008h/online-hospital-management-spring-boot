package com.online.hospital.managment.model;

import javax.persistence.*;

@Entity
@Table(name = "AMBULANCE")
public class Ambulance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phone;
    private String zilla;
    private String thana;

    public Ambulance() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getZilla() {
        return zilla;
    }

    public void setZilla(String zilla) {
        this.zilla = zilla;
    }

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    @Override
    public String toString() {
        return "Ambulance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", zilla='" + zilla + '\'' +
                ", thana='" + thana + '\'' +
                '}';
    }
}
