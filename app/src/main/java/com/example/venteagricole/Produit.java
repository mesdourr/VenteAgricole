package com.example.venteagricole;

public class Produit {

    private String id,namp,imagep,quentiity,prix,tel,place,iduser;
    private Double latitude,longitude;

    public Produit(String id, String namp, String imagep, String quentiity, String prix, String tel, String place, String iduser, Double latitude, Double longitude) {
        this.id = id;
        this.namp = namp;
        this.imagep = imagep;
        this.quentiity = quentiity;
        this.prix = prix;
        this.tel = tel;
        this.place = place;
        this.iduser = iduser;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamp() {
        return namp;
    }

    public void setNamp(String namp) {
        this.namp = namp;
    }

    public String getImagep() {
        return imagep;
    }

    public void setImagep(String imagep) {
        this.imagep = imagep;
    }

    public String getQuentiity() {
        return quentiity;
    }

    public void setQuentiity(String quentiity) {
        this.quentiity = quentiity;
    }

    public String getPrix() {
        return prix;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
