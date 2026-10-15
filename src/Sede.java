

import java.util.ArrayList;
import java.util.List;

public class Sede {
    private String codigo;
    private String nombre;
    private String direccion;
    private List<Equipo> listaEquipos;

    public Sede(String codigo, String nombre, String direccion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.direccion = direccion;
        this.listaEquipos = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public void setListaEquipos(List<Equipo> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }


    public int buscarEquipo(String codigoEquipo) {
        for (int i = 0; i < listaEquipos.size(); i++) {
            if (listaEquipos.get(i).getCodigo().equals(codigoEquipo)) {
                return i;
            }
        }
        return -1;
    }


    public boolean asignarEquipo(Equipo equipo) {
        if (equipo == null || buscarEquipo(equipo.getCodigo()) != -1) {
            return false;
        }
        listaEquipos.add(equipo);
        return true;
    }

    public boolean retirarEquipo(String codigoEquipo) {
        int index = buscarEquipo(codigoEquipo);
        if (index != -1) {
            listaEquipos.remove(index);
            return true;
        }
        return false;
    }

    public String mostrarEquipos() {
        String mensaje = "";
        for (Equipo equipo : listaEquipos) {
            mensaje += "\n" + equipo.mostrarInformacion() + "\n";
        }
        return mensaje;
    }

    public String mostrarInformacion() {
        return "Codigo: " + codigo +
                "\nNombre: " + nombre +
                "\nDireccion: " + direccion +
                "\nEquipos asignados: " + listaEquipos.size();
    }
}
