

public record Cliente(String nombreCompleto, String identificacion, String telefono, String correo) {

    public Cliente {
        if (nombreCompleto == null || nombreCompleto.isBlank()
                || identificacion == null || identificacion.isBlank()) {
            throw new IllegalArgumentException("El nombre y la identificacion son obligatorios");
        }
    }
}
