

public abstract class Equipo {
    //protected: visibles para las clases hijas (y el paquete), pero no para el resto del programa
    protected String codigo;
    protected String marca;
    protected String modelo;
    protected CondicionEquipo condicion;

    public Equipo(String codigo, String marca, String modelo, CondicionEquipo condicion) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.condicion = condicion;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public CondicionEquipo getCondicion() {
        return condicion;
    }

    public void setCondicion(CondicionEquipo condicion) {
        this.condicion = condicion;
    }

    //Un equipo solo se puede usar si su condicion es DISPONIBLE
    public boolean estaDisponible() {
        return condicion == CondicionEquipo.DISPONIBLE;
    }

    //Por defecto un equipo no exige operador; los drones lo sobrescriben
    public boolean requiereOperador() {
        return false;
    }

    //Cada tipo de equipo calcula su costo de manera diferente
    public abstract double calcularCosto(int horas);

    //Los adicionales se cobran una sola vez sobre el total de horas
    public String mostrarInformacion() {
        return "Codigo: " + codigo +
                "\nMarca: " + marca +
                "\nModelo: " + modelo +
                "\nCondicion: " + condicion;
    }
}
