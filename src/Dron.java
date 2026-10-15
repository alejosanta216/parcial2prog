

public class Dron extends Equipo {
    private static final double COSTO_HORA = 18000;
    private static final double ADICIONAL_BATERIA = 20000;

    protected int autonomiaMinutos;
    protected boolean bateriaAdicional; // true cuando se solicita la bateria adicional

    public Dron(String codigo, String marca, String modelo, CondicionEquipo condicion,
                int autonomiaMinutos, boolean bateriaAdicional) {
        super(codigo, marca, modelo, condicion);
        this.autonomiaMinutos = autonomiaMinutos;
        this.bateriaAdicional = bateriaAdicional;
    }

    public int getAutonomiaMinutos() {
        return autonomiaMinutos;
    }

    public void setAutonomiaMinutos(int autonomiaMinutos) {
        this.autonomiaMinutos = autonomiaMinutos;
    }

    public boolean isBateriaAdicional() {
        return bateriaAdicional;
    }

    public void setBateriaAdicional(boolean bateriaAdicional) {
        this.bateriaAdicional = bateriaAdicional;
    }

    //Los drones exigen un operador asignado en la reserva
    @Override
    public boolean requiereOperador() {
        return true;
    }

    @Override
    public double calcularCosto(int horas) {
        double total = horas * COSTO_HORA;
        if (bateriaAdicional) {
            total += ADICIONAL_BATERIA;
        }
        return total;
    }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Dron\n" + super.mostrarInformacion() +
                "\nAutonomia (min): " + autonomiaMinutos +
                "\nBateria adicional: " + (bateriaAdicional ? "Si" : "No");
    }
}
