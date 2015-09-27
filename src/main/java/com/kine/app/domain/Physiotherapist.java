package com.kine.app.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Physiotherapist.
 */
@Entity
@Table(name = "PHYSIOTHERAPIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Physiotherapist implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name")
    private String lastName;
    
    @Column(name = "street")
    private String street;
    
    @Column(name = "postal_code")
    private Integer postalCode;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "country")
    private String country;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "PHYSIOTHERAPIST_SPECIALTY",
               joinColumns = @JoinColumn(name="physiotherapists_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="specialtys_id", referencedColumnName="ID"))
    private Set<Specialty> specialtys = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Specialty> getSpecialtys() {
        return specialtys;
    }

    public void setSpecialtys(Set<Specialty> specialtys) {
        this.specialtys = specialtys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Physiotherapist physiotherapist = (Physiotherapist) o;

        if ( ! Objects.equals(id, physiotherapist.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Physiotherapist{" +
                "id=" + id +
                ", firstName='" + firstName + "'" +
                ", lastName='" + lastName + "'" +
                ", street='" + street + "'" +
                ", postalCode='" + postalCode + "'" +
                ", city='" + city + "'" +
                ", country='" + country + "'" +
                '}';
    }
}
