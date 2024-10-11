package com.example.techwash.Model;

import java.io.Serializable;

public class Auto implements Serializable {
    String hinh;
    String tenauto;
    String diachi;
    String gia;
    String gia2;
    String hinh2;
    String hinh3;
    String slphongdon;
    String hinh4;
    String diachiCT;
    String mota;
    String Sdtks;
    boolean trangthai;

    public Auto() {
    }

    public Auto(String hinh, String tenauto, String diachi, String gia, String hinh2, String hinh3, String hinh4, String diachiCT, String mota, String slphongdon, String Sdtks, boolean trangthai, String gia2) {
        this.hinh = hinh;
        this.tenauto = tenauto;
        this.diachi = diachi;
        this.gia = gia;
        this.hinh2 = hinh2;
        this.hinh3 = hinh3;
        this.hinh4 = hinh4;
        this.diachiCT = diachiCT;
        this.mota = mota;
        this.slphongdon = slphongdon;
        this.Sdtks = Sdtks;
        this.trangthai = trangthai;
        this.gia2 = gia2;
    }

    public String getGia2() {
        return gia2;
    }

    public void setGia2(String gia2) {
        this.gia2 = gia2;
    }

    public boolean isTrangthai() {
        return trangthai;
    }

    public void setTrangthai(boolean trangthai) {
        this.trangthai = trangthai;
    }

    public String getSdtks() {
        return Sdtks;
    }

    public void setSdtks(String sdtks) {
        Sdtks = sdtks;
    }

    public String getSlphongdon() {
        return slphongdon;
    }

    public void setSlphongdon(String slphongdon) {
        this.slphongdon = slphongdon;
    }

    public String getHinh() {
        return hinh;
    }

    public void setHinh(String hinh) {
        this.hinh = hinh;
    }

    public String getTenks() {
        return tenauto;
    }

    public void setTenks(String tenks) {
        this.tenauto = tenks;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getHinh2() {
        return hinh2;
    }

    public void setHinh2(String hinh2) {
        this.hinh2 = hinh2;
    }

    public String getHinh3() {
        return hinh3;
    }

    public void setHinh3(String hinh3) {
        this.hinh3 = hinh3;
    }

    public String getHinh4() {
        return hinh4;
    }

    public void setHinh4(String hinh4) {
        this.hinh4 = hinh4;
    }

    public String getDiachiCT() {
        return diachiCT;
    }

    public void setDiachiCT(String diachiCT) {
        this.diachiCT = diachiCT;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}