package br.com.nexusacademico.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class EncontroEvento {

    private int idEncontro;
    private int idEvento;

    private LocalDate dataEncontro;
    private LocalTime horaInicio;
    private LocalTime horaFim;

    public EncontroEvento(int idEvento, LocalDate dataEncontro,
                          LocalTime horaInicio, LocalTime horaFim) {

        validarHorario(horaInicio, horaFim);

        this.idEvento = idEvento;
        this.dataEncontro = dataEncontro;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    public EncontroEvento(int idEncontro, int idEvento,
                          LocalDate dataEncontro,
                          LocalTime horaInicio, LocalTime horaFim) {

        this(idEvento, dataEncontro, horaInicio, horaFim);
        this.idEncontro = idEncontro;
    }

    private static void validarHorario(LocalTime horaInicio, LocalTime horaFim) {
        if (horaInicio == null || horaFim == null
                || !horaFim.isAfter(horaInicio)) {
            throw new IllegalArgumentException(
                    "A hora de fim deve ser posterior à hora de início."
            );
        }
    }

    public int getIdEncontro() {
        return idEncontro;
    }

    public void setIdEncontro(int idEncontro) {
        this.idEncontro = idEncontro;
    }

    public int getIdEvento() {
        return idEvento;
    }

    public LocalDate getDataEncontro() {
        return dataEncontro;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void atualizar(LocalDate dataEncontro,
                          LocalTime horaInicio, LocalTime horaFim) {

        validarHorario(horaInicio, horaFim);

        this.dataEncontro = dataEncontro;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }
}