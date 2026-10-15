

public class KitIluminacion extends Equipo {
    private static final double COSTO_HORA = 10000;
    private static final double ADICIONAL_CONTROL = 12000;

    protected int cantidadLuces;
    protected boolean controlInalambrico;

    public KitIluminacion(String codigo, String marca, String modelo, CondicionEquipo condicion,
                          int cantidadLuces, boolean controlInalambrico) {
        super(codigo, marca, modelo, condicion);
        this.cantidadLuces = cantidadLuces;
        this.controlInalambrico = controlInalambrico;
    }

    public int getCantidadLuces() {
        return cantidadLuces;
    }

    public void setCantidadLuces(int cantidadLuces) {
        this.cantidadLuces = cantidadLuces;
    }

    public boolean isControlInalambrico() {
        return controlInalambrico;
    }

    public void setControlInalambrico(boolean controlInalambrico) {
        this.controlInalambrico = controlInalambrico;
    }

    @Override
    public double calcularCosto(int horas) {
        double total = horas * COSTO_HORA;
        if (controlInalambrico) {
            total += ADICIONAL_CONTROL;
        }
        return total;
    }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Kit de iluminacion\n" + super.mostrarInformacion() +
                "\nCantidad de luces: " + cantidadLuces +
                "\nControl inalambrico: " + (controlInalambrico ? "Si" : "No");
    }
}
