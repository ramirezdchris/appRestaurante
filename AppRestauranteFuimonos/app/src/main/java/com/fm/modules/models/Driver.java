package com.fm.modules.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.TimeZone;

public class Driver {

    private Long driverId;
    private String username;
    private String password;
    private String nombreDriver;
    private boolean habilitado;
    @JsonFormat(pattern = "HH:mm:ss")
    private Date horaDeEntrada;
    @JsonFormat(pattern = "HH:mm:ss")
    private Date horaDeSalida;
    private Date fechaCreado;
    private Boolean statusAsignado;

    public Driver() {
    }

    public Driver(Long driverId, String username, String password, String nombreDriver, boolean habilitado,
                  Date horaDeEntrada, Date horaDeSalida, Date fechaCreado, Boolean statusAsignado) {
        this.driverId = driverId;
        this.username = username;
        this.password = password;
        this.nombreDriver = nombreDriver;
        this.habilitado = habilitado;
        this.horaDeEntrada = horaDeEntrada;
        this.horaDeSalida = horaDeSalida;
        this.fechaCreado = fechaCreado;
        this.statusAsignado = statusAsignado;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombreDriver() {
        return nombreDriver;
    }

    public void setNombreDriver(String nombreDriver) {
        this.nombreDriver = nombreDriver;
    }

    public boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Date getHoraDeEntrada() {
        return horaDeEntrada;
    }

    public void setHoraDeEntrada(Date horaDeEntrada) {
        this.horaDeEntrada = horaDeEntrada;
    }

    public Date getHoraDeSalida() {
        return horaDeSalida;
    }

    public void setHoraDeSalida(Date horaDeSalida) {
        this.horaDeSalida = horaDeSalida;
    }

    public Date getFechaCreado() {
        return fechaCreado;
    }

    public void setFechaCreado(Date fechaCreado) {
        this.fechaCreado = fechaCreado;
    }

    public Boolean getStatusAsignado() {
        return statusAsignado;
    }

    public void setStatusAsignado(Boolean statusAsignado) {
        this.statusAsignado = statusAsignado;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{driverId:'");
        builder.append(driverId);
        builder.append("',username:'");
        builder.append(username);
        builder.append("',password:'");
        builder.append(password);
        builder.append("',nombreDriver:'");
        builder.append(nombreDriver);
        builder.append("',habilitado:'");
        builder.append(habilitado);
        builder.append("',horaDeEntrada:'");
        builder.append(horaDeEntrada);
        builder.append("',horaDeSalida:'");
        builder.append(horaDeSalida);
        builder.append("',fechaCreado:'");
        builder.append(fechaCreado);
        builder.append("',statusAsignado:'");
        builder.append(statusAsignado);
        builder.append("'}");
        return builder.toString();
    }

}
