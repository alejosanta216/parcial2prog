

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Reserva {
    private final String codigo;
    private final Cliente cliente;
    private final LocalDate fecha;   // no se modifica despues de crear la reserva
    private final Turno turno;       // no se modifica despues de crear la reserva
    private EstadoReserva estado;
    private Operador operador;       // 0..1
    private final List<ItemReserva> listaItems;

    public Reserva(String codigo, Cliente cliente, LocalDate fecha, Turno turno) {
        if (codigo == null || cliente == null || fecha == null || turno == null) {
            throw new IllegalArgumentException("Todos los datos de la reserva son obligatorios");
        }
        this.codigo = codigo;
        this.cliente = cliente;
        this.fecha = fecha;
        this.turno = turno;
        this.estado = EstadoReserva.SOLICITADA;
        this.operador = null;
        this.listaItems = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Turno getTurno() {
        return turno;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Operador getOperador() {
        return operador;
    }

    //Lista de solo lectura: los items solo se modifican con agregarEquipo
    public List<ItemReserva> getItems() {
        return Collections.unmodifiableList(listaItems);
    }

    public int buscarItem(String codigoEquipo) {
        for (int i = 0; i < listaItems.size(); i++) {
            if (listaItems.get(i).equipo().getCodigo().equals(codigoEquipo)) {
                return i;
            }
        }
        return -1;
    }

    public boolean usaEquipo(Equipo equipo) {
        return buscarItem(equipo.getCodigo()) != -1;
    }

    public boolean agregarEquipo(Equipo equipo, int horas) {
        if (estado != EstadoReserva.SOLICITADA || equipo == null || horas <= 0) {
            return false;
        }
        if (!equipo.estaDisponible() || usaEquipo(equipo)) {
            return false;
        }
        listaItems.add(new ItemReserva(equipo, horas));
        return true;
    }

    public boolean requiereOperador() {
        for (ItemReserva item : listaItems) {
            if (item.equipo().requiereOperador()) {
                return true;
            }
        }
        return false;
    }

    public boolean asignarOperador(Operador nuevoOperador) {
        if (estado != EstadoReserva.SOLICITADA || nuevoOperador == null) {
            return false;
        }
        this.operador = nuevoOperador;
        return true;
    }

    public double calcularValorTotal() {
        double total = 0;
        for (ItemReserva item : listaItems) {
            total += item.calcularCosto();
        }
        return total;
    }

    public boolean cumpleRequisitosBasicos() {
        if (listaItems.isEmpty()) {
            return false;
        }
        return !requiereOperador() || operador != null;
    }

    boolean confirmar() {
        if (estado != EstadoReserva.SOLICITADA || !cumpleRequisitosBasicos()) {
            return false;
        }
        estado = EstadoReserva.CONFIRMADA;
        return true;
    }

    public boolean cancelar() {
        if (estado == EstadoReserva.SOLICITADA || estado == EstadoReserva.CONFIRMADA) {
            estado = EstadoReserva.CANCELADA;
            return true;
        }
        return false;
    }

    public boolean finalizar() {
        if (estado == EstadoReserva.CONFIRMADA) {
            estado = EstadoReserva.FINALIZADA;
            return true;
        }
        return false;
    }

    public String mostrarInformacion() {
        String mensaje = "Codigo: " + codigo +
                "\nCliente: " + cliente.nombreCompleto() + " (" + cliente.identificacion() + ")" +
                "\nFecha: " + fecha +
                "\nTurno: " + turno +
                "\nEstado: " + estado +
                "\nOperador: " + (operador == null ? "Sin asignar" : operador.getNombreCompleto()) +
                "\nEquipos:";
        for (ItemReserva item : listaItems) {
            mensaje += "\n  - " + item.equipo().getCodigo() + " (" + item.horas() + " h) = " + item.calcularCosto();
        }
        return mensaje + "\nValor total: " + calcularValorTotal();
    }
}
