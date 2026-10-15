

public class Camara extends Equipo {
    private static final double COSTO_HORA = 12000;
    private static final double ADICIONAL_ESTABILIZADOR = 15000;

    protected String resolucionMaxima;
    protected boolean tieneEstabilizador;

    public Camara(String codigo, String marca, String modelo, CondicionEquipo condicion,
                  String resolucionMaxima, boolean tieneEstabilizador) {
        super(codigo, marca, modelo, condicion);
        this.resolucionMaxima = resolucionMaxima;
        this.tieneEstabilizador = tieneEstabilizador;
    }

    public String getResolucionMaxima() {
        return resolucionMaxima;
    }

    public void setResolucionMaxima(String resolucionMaxima) {
        this.resolucionMaxima = resolucionMaxima;
    }

    public boolean isTieneEstabilizador() {
        return tieneEstabilizador;
    }

    public void setTieneEstabilizador(boolean tieneEstabilizador) {
        this.tieneEstabilizador = tieneEstabilizador;
    }

    @Override
    public double calcularCosto(int horas) {
        double total = horas * COSTO_HORA;
        if (tieneEstabilizador) {
            total += ADICIONAL_ESTABILIZADOR;
        }
        return total;
    }

    @Override
    public String mostrarInformacion() {
        return "Tipo: Camara\n" + super.mostrarInformacion() +
                "\nResolucion maxima: " + resolucionMaxima +
                "\nEstabilizador: " + (tieneEstabilizador ? "Si" : "No");
    }
}
