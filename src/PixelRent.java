

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

    public class PixelRent {
        private String nombre;
        private List<Sede> listaSedes;
        private List<Equipo> listaEquipos;
        private List<Cliente> listaClientes;
        private List<Operador> listaOperadores;
        private List<Reserva> listaReservas;

        public PixelRent(String nombre) {
            this.nombre = nombre;
            this.listaSedes = new ArrayList<>();
            this.listaEquipos = new ArrayList<>();
            this.listaClientes = new ArrayList<>();
            this.listaOperadores = new ArrayList<>();
            this.listaReservas = new ArrayList<>();
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public List<Sede> getListaSedes() {
            return listaSedes;
        }

        public List<Equipo> getListaEquipos() {
            return listaEquipos;
        }

        public List<Cliente> getListaClientes() {
            return listaClientes;
        }

        public List<Operador> getListaOperadores() {
            return listaOperadores;
        }

        public List<Reserva> getListaReservas() {
            return listaReservas;
        }



        public int buscarSede(String codigo) {
            for (int i = 0; i < listaSedes.size(); i++) {
                if (listaSedes.get(i).getCodigo().equals(codigo)) {
                    return i;
                }
            }
            return -1;
        }

        public int buscarEquipo(String codigo) {
            for (int i = 0; i < listaEquipos.size(); i++) {
                if (listaEquipos.get(i).getCodigo().equals(codigo)) {
                    return i;
                }
            }
            return -1;
        }

        public int buscarCliente(String identificacion) {
            for (int i = 0; i < listaClientes.size(); i++) {
                if (listaClientes.get(i).identificacion().equals(identificacion)) {
                    return i;
                }
            }
            return -1;
        }

        public int buscarOperador(String identificacion) {
            for (int i = 0; i < listaOperadores.size(); i++) {
                if (listaOperadores.get(i).getIdentificacion().equals(identificacion)) {
                    return i;
                }
            }
            return -1;
        }

        public int buscarReserva(String codigo) {
            for (int i = 0; i < listaReservas.size(); i++) {
                if (listaReservas.get(i).getCodigo().equals(codigo)) {
                    return i;
                }
            }
            return -1;
        }



        public boolean registrarSede(Sede sede) {
            if (sede == null || buscarSede(sede.getCodigo()) != -1) {
                return false;
            }
            listaSedes.add(sede);
            return true;
        }


        public boolean registrarEquipo(Equipo equipo) {
            if (equipo == null || buscarEquipo(equipo.getCodigo()) != -1) {
                return false;
            }
            listaEquipos.add(equipo);
            return true;
        }

        public boolean registrarCliente(Cliente cliente) {
            if (cliente == null || buscarCliente(cliente.identificacion()) != -1) {
                return false;
            }
            listaClientes.add(cliente);
            return true;
        }

        public boolean registrarOperador(Operador operador) {
            if (operador == null || buscarOperador(operador.getIdentificacion()) != -1) {
                return false;
            }
            listaOperadores.add(operador);
            return true;
        }


        public boolean registrarReserva(Reserva reserva) {
            if (reserva == null || buscarReserva(reserva.getCodigo()) != -1) {
                return false;
            }
            if (buscarCliente(reserva.getCliente().identificacion()) == -1) {
                return false;
            }
            listaReservas.add(reserva);
            return true;
        }


        public boolean asignarEquipoASede(String codigoSede, String codigoEquipo) {
            int indexSede = buscarSede(codigoSede);
            int indexEquipo = buscarEquipo(codigoEquipo);
            if (indexSede == -1 || indexEquipo == -1) {
                return false;
            }
            for (Sede sede : listaSedes) {
                if (sede.buscarEquipo(codigoEquipo) != -1) {
                    return false;
                }
            }
            return listaSedes.get(indexSede).asignarEquipo(listaEquipos.get(indexEquipo));
        }



        public boolean agregarEquipoAReserva(String codigoReserva, String codigoEquipo, int horas) {
            int indexReserva = buscarReserva(codigoReserva);
            int indexEquipo = buscarEquipo(codigoEquipo);
            if (indexReserva == -1 || indexEquipo == -1) {
                return false;
            }
            return listaReservas.get(indexReserva).agregarEquipo(listaEquipos.get(indexEquipo), horas);
        }

        public boolean asignarOperadorAReserva(String codigoReserva, String identificacionOperador) {
            int indexReserva = buscarReserva(codigoReserva);
            int indexOperador = buscarOperador(identificacionOperador);
            if (indexReserva == -1 || indexOperador == -1) {
                return false;
            }
            return listaReservas.get(indexReserva).asignarOperador(listaOperadores.get(indexOperador));
        }

        //Un equipo esta ocupado si esta en otra reserva CONFIRMADA con la misma fecha y turno
        private boolean equipoOcupado(Equipo equipo, LocalDate fecha, Turno turno) {
            for (Reserva reserva : listaReservas) {
                if (reserva.getEstado() == EstadoReserva.CONFIRMADA
                        && reserva.getFecha().equals(fecha)
                        && reserva.getTurno() == turno
                        && reserva.usaEquipo(equipo)) {
                    return true;
                }
            }
            return false;
        }


        public boolean confirmarReserva(String codigoReserva) {
            int index = buscarReserva(codigoReserva);
            if (index == -1) {
                return false;
            }
            Reserva reserva = listaReservas.get(index);

            if (reserva.getEstado() != EstadoReserva.SOLICITADA || !reserva.cumpleRequisitosBasicos()) {
                return false;
            }
            for (ItemReserva item : reserva.getItems()) {
                Equipo equipo = item.equipo();
                if (!equipo.estaDisponible() || equipoOcupado(equipo, reserva.getFecha(), reserva.getTurno())) {
                    return false;
                }
            }
            return reserva.confirmar();
        }

        public boolean cancelarReserva(String codigoReserva) {
            int index = buscarReserva(codigoReserva);
            return index != -1 && listaReservas.get(index).cancelar();
        }

        public boolean finalizarReserva(String codigoReserva) {
            int index = buscarReserva(codigoReserva);
            return index != -1 && listaReservas.get(index).finalizar();
        }




        public List<Equipo> consultarEquiposDisponibles(LocalDate fecha, Turno turno) {
            List<Equipo> disponibles = new ArrayList<>();
            for (Equipo equipo : listaEquipos) {
                if (equipo.estaDisponible() && !equipoOcupado(equipo, fecha, turno)) {
                    disponibles.add(equipo);
                }
            }
            return disponibles;
        }

        public List<Reserva> consultarReservasConfirmadasPorCliente(String identificacionCliente) {
            List<Reserva> confirmadas = new ArrayList<>();
            for (Reserva reserva : listaReservas) {
                if (reserva.getEstado() == EstadoReserva.CONFIRMADA
                        && reserva.getCliente().identificacion().equals(identificacionCliente)) {
                    confirmadas.add(reserva);
                }
            }
            return confirmadas;
        }

        //Devuelve -1 si la reserva no existe
        public double consultarValorReserva(String codigoReserva) {
            int index = buscarReserva(codigoReserva);
            if (index == -1) {
                return -1;
            }
            return listaReservas.get(index).calcularValorTotal();
        }

        // ---------------------------- MOSTRAR ----------------------------

        public String mostrarSedes() {
            String mensaje = "";
            for (Sede sede : listaSedes) {
                mensaje += "\n" + sede.mostrarInformacion() + "\n";
            }
            return mensaje;
        }

        public String mostrarEquipos() {
            String mensaje = "";
            for (Equipo equipo : listaEquipos) {
                mensaje += "\n" + equipo.mostrarInformacion() + "\n";
            }
            return mensaje;
        }

        public String mostrarReservas() {
            String mensaje = "";
            for (Reserva reserva : listaReservas) {
                mensaje += "\n" + reserva.mostrarInformacion() + "\n";
            }
            return mensaje;
        }
    }

