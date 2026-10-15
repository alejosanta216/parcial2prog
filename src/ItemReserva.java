

public record ItemReserva(Equipo equipo, int horas) {

    public ItemReserva {
        if (equipo == null || horas <= 0) {
            throw new IllegalArgumentException("El equipo es obligatorio y las horas deben ser mayores a 0");
        }
    }

    public double calcularCosto() {
        return equipo.calcularCosto(horas);
    }
}
