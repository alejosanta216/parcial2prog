

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;

public class Main {

    private static final String TITULO = "PixelRent";
    private static final PixelRent empresa = new PixelRent("PixelRent");

    //Se lanza cuando el usuario cierra o cancela un cuadro de dialogo
    private static class OperacionCancelada extends RuntimeException {
    }

    public static void main(String[] args) {
        int opcion;
        do {
            opcion = leerOpcionMenu();
            try {
                ejecutarOpcion(opcion);
            } catch (OperacionCancelada e) {
                mensaje("Operacion cancelada");
            } catch (IllegalArgumentException e) {
                mensaje("Datos invalidos: " + e.getMessage());
            }
        } while (opcion != 0);
    }


    private static int leerOpcionMenu() {
        String texto = JOptionPane.showInputDialog(null,
                "MENU PIXELRENT\n" +
                        "\nREGISTROS" +
                        "\n 1. Registrar sede" +
                        "\n 2. Registrar equipo" +
                        "\n 3. Asignar equipo a una sede" +
                        "\n 4. Registrar cliente" +
                        "\n 5. Registrar operador" +
                        "\n 6. Registrar reserva" +
                        "\n\nOPERACIONES SOBRE RESERVAS" +
                        "\n 7. Agregar equipo a una reserva" +
                        "\n 8. Asignar operador a una reserva" +
                        "\n 9. Confirmar reserva" +
                        "\n 10. Cancelar reserva" +
                        "\n 11. Finalizar reserva" +
                        "\n\nCONSULTAS" +
                        "\n 12. Equipos disponibles por fecha y turno" +
                        "\n 13. Reservas confirmadas de un cliente" +
                        "\n 14. Valor total de una reserva" +
                        "\n\nLISTADOS" +
                        "\n 15. Mostrar sedes" +
                        "\n 16. Mostrar equipos" +
                        "\n 17. Mostrar reservas" +
                        "\n\nOTROS" +
                        "\n 18. Cambiar condicion de un equipo" +
                        "\n 19. Cargar datos de ejemplo" +
                        "\n 0. Salir del sistema",
                TITULO, JOptionPane.QUESTION_MESSAGE);

        if (texto == null) {
            return 0;
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> registrarSede();
            case 2 -> registrarEquipo();
            case 3 -> asignarEquipoASede();
            case 4 -> registrarCliente();
            case 5 -> registrarOperador();
            case 6 -> registrarReserva();
            case 7 -> agregarEquipoAReserva();
            case 8 -> asignarOperadorAReserva();
            case 9 -> confirmarReserva();
            case 10 -> cancelarReserva();
            case 11 -> finalizarReserva();
            case 12 -> consultarEquiposDisponibles();
            case 13 -> consultarReservasConfirmadasPorCliente();
            case 14 -> consultarValorReserva();
            case 15 -> mostrarTexto("Sedes", empresa.mostrarSedes());
            case 16 -> mostrarTexto("Equipos", empresa.mostrarEquipos());
            case 17 -> mostrarTexto("Reservas", empresa.mostrarReservas());
            case 18 -> cambiarCondicionEquipo();
            case 19 -> cargarDatosEjemplo();
            case 0 -> mensaje("El programa finalizo");
            default -> mensaje("La opcion no es valida");
        }
    }


    private static void registrarSede() {
        String codigo = pedirTexto("Codigo de la sede:");
        String nombre = pedirTexto("Nombre de la sede:");
        String direccion = pedirTexto("Direccion de la sede:");

        resultado(empresa.registrarSede(new Sede(codigo, nombre, direccion)),
                "Sede registrada correctamente",
                "No se registro la sede: ya existe una con ese codigo");
    }

    private static void registrarEquipo() {
        String tipo = (String) elegir("Seleccione el tipo de equipo:",
                new String[]{"Camara", "Dron", "Kit de iluminacion"});
        String codigo = pedirTexto("Codigo del equipo:");
        String marca = pedirTexto("Marca:");
        String modelo = pedirTexto("Modelo:");
        CondicionEquipo condicion = (CondicionEquipo) elegir("Condicion del equipo:", CondicionEquipo.values());

        Equipo equipo;
        switch (tipo) {
            case "Camara" -> {
                String resolucion = pedirTexto("Resolucion maxima (ej: 4K):");
                boolean estabilizador = pedirSiNo("¿La camara tiene estabilizador?");
                equipo = new Camara(codigo, marca, modelo, condicion, resolucion, estabilizador);
            }
            case "Dron" -> {
                int autonomia = pedirEntero("Autonomia de vuelo (minutos):");
                boolean bateria = pedirSiNo("¿Se solicita bateria adicional?");
                equipo = new Dron(codigo, marca, modelo, condicion, autonomia, bateria);
            }
            default -> {
                int luces = pedirEntero("Cantidad de luces del kit:");
                boolean control = pedirSiNo("¿El kit tiene control inalambrico?");
                equipo = new KitIluminacion(codigo, marca, modelo, condicion, luces, control);
            }
        }

        resultado(empresa.registrarEquipo(equipo),
                "Equipo registrado correctamente",
                "No se registro el equipo: ya existe uno con ese codigo");
    }

    private static void asignarEquipoASede() {
        String codigoSede = pedirTexto("Codigo de la sede:");
        String codigoEquipo = pedirTexto("Codigo del equipo:");

        resultado(empresa.asignarEquipoASede(codigoSede, codigoEquipo),
                "Equipo asignado a la sede",
                "No se asigno: verifique que la sede y el equipo existan y que el equipo no pertenezca ya a una sede");
    }

    private static void registrarCliente() {
        String nombre = pedirTexto("Nombre completo del cliente:");
        String identificacion = pedirTexto("Numero de identificacion:");
        String telefono = pedirTexto("Telefono:");
        String correo = pedirTexto("Correo electronico:");

        resultado(empresa.registrarCliente(new Cliente(nombre, identificacion, telefono, correo)),
                "Cliente registrado correctamente",
                "No se registro el cliente: ya existe uno con esa identificacion");
    }

    private static void registrarOperador() {
        String identificacion = pedirTexto("Numero de identificacion del operador:");
        String nombre = pedirTexto("Nombre completo:");
        String certificacion = pedirTexto("Certificacion:");
        int anios = pedirEntero("Anios de experiencia:");

        resultado(empresa.registrarOperador(new Operador(identificacion, nombre, certificacion, anios)),
                "Operador registrado correctamente",
                "No se registro el operador: ya existe uno con esa identificacion");
    }

    private static void registrarReserva() {
        String codigo = pedirTexto("Codigo de la reserva:");
        String identificacion = pedirTexto("Identificacion del cliente que reserva:");

        int indexCliente = empresa.buscarCliente(identificacion);
        if (indexCliente == -1) {
            mensaje("El cliente no esta registrado. Registrelo primero (opcion 4)");
            return;
        }

        LocalDate fecha = elegirFecha("Seleccione la fecha para la cual necesita los equipos:");
        Turno turno = (Turno) elegir("Seleccione el turno:", Turno.values());

        Reserva reserva = new Reserva(codigo, empresa.getListaClientes().get(indexCliente), fecha, turno);

        resultado(empresa.registrarReserva(reserva),
                "Reserva registrada con estado SOLICITADA",
                "No se registro la reserva: ya existe una con ese codigo");
    }



    private static void agregarEquipoAReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva:");
        String codigoEquipo = pedirTexto("Codigo del equipo a agregar:");
        int horas = pedirEntero("Horas de uso del equipo:");

        resultado(empresa.agregarEquipoAReserva(codigoReserva, codigoEquipo, horas),
                "Equipo agregado a la reserva",
                "No se agrego el equipo. Verifique que la reserva este SOLICITADA, que el equipo exista, " +
                        "este DISPONIBLE y no este repetido en la reserva, y que las horas sean mayores a 0");
    }

    private static void asignarOperadorAReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva:");
        String identificacion = pedirTexto("Identificacion del operador:");

        resultado(empresa.asignarOperadorAReserva(codigoReserva, identificacion),
                "Operador asignado a la reserva",
                "No se asigno el operador. Verifique que la reserva y el operador existan " +
                        "y que la reserva este SOLICITADA");
    }

    private static void confirmarReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva a confirmar:");

        resultado(empresa.confirmarReserva(codigoReserva),
                "Reserva CONFIRMADA correctamente",
                "No se pudo confirmar. Verifique que la reserva exista y este SOLICITADA, tenga al menos un equipo, " +
                        "tenga operador si incluye un dron, y que ningun equipo este ya en otra " +
                        "reserva confirmada para la misma fecha y turno");
    }

    private static void cancelarReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva a cancelar:");

        resultado(empresa.cancelarReserva(codigoReserva),
                "Reserva CANCELADA",
                "No se pudo cancelar: la reserva no existe o ya esta cancelada o finalizada");
    }

    private static void finalizarReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva a finalizar:");

        resultado(empresa.finalizarReserva(codigoReserva),
                "Reserva FINALIZADA",
                "No se pudo finalizar: la reserva no existe o no esta CONFIRMADA");
    }



    private static void consultarEquiposDisponibles() {
        LocalDate fecha = elegirFecha("Seleccione la fecha a consultar:");
        Turno turno = (Turno) elegir("Seleccione el turno a consultar:", Turno.values());

        List<Equipo> disponibles = empresa.consultarEquiposDisponibles(fecha, turno);

        String texto = "";
        for (Equipo equipo : disponibles) {
            texto += "\n" + equipo.mostrarInformacion() + "\n";
        }
        mostrarTexto("Equipos disponibles el " + fecha + " (" + turno + ")", texto);
    }

    private static void consultarReservasConfirmadasPorCliente() {
        String identificacion = pedirTexto("Identificacion del cliente:");

        List<Reserva> confirmadas = empresa.consultarReservasConfirmadasPorCliente(identificacion);

        String texto = "";
        for (Reserva reserva : confirmadas) {
            texto += "\n" + reserva.mostrarInformacion() + "\n";
        }
        mostrarTexto("Reservas confirmadas del cliente " + identificacion, texto);
    }

    private static void consultarValorReserva() {
        String codigoReserva = pedirTexto("Codigo de la reserva:");

        double valor = empresa.consultarValorReserva(codigoReserva);

        if (valor == -1) {
            mensaje("La reserva no existe");
        } else {
            mensaje("Valor total de la reserva " + codigoReserva + ": $" + String.format("%,.0f", valor));
        }
    }



    private static void cambiarCondicionEquipo() {
        String codigoEquipo = pedirTexto("Codigo del equipo:");
        int index = empresa.buscarEquipo(codigoEquipo);

        if (index == -1) {
            mensaje("El equipo no existe");
            return;
        }

        CondicionEquipo nueva = (CondicionEquipo) elegir("Nueva condicion del equipo:", CondicionEquipo.values());
        empresa.getListaEquipos().get(index).setCondicion(nueva);
        mensaje("Condicion actualizada a " + nueva);
    }


    private static void cargarDatosEjemplo() {
        if (empresa.buscarSede("S-01") != -1) {
            mensaje("Los datos de ejemplo ya fueron cargados");
            return;
        }

        empresa.registrarSede(new Sede("S-01", "Sede Centro", "Calle 10 # 5-20"));

        empresa.registrarEquipo(new Camara("CAM-01", "Sony", "A7 IV", CondicionEquipo.DISPONIBLE, "4K", true));
        empresa.registrarEquipo(new Camara("CAM-02", "Canon", "R6", CondicionEquipo.DISPONIBLE, "4K", false));
        empresa.registrarEquipo(new Dron("DRN-01", "DJI", "Mavic 3", CondicionEquipo.DISPONIBLE, 46, true));
        empresa.registrarEquipo(new KitIluminacion("KIT-01", "Godox", "SL60", CondicionEquipo.DISPONIBLE, 4, true));
        empresa.registrarEquipo(new KitIluminacion("KIT-02", "Aputure", "Amaran", CondicionEquipo.MANTENIMIENTO, 3, false));

        for (String codigo : new String[]{"CAM-01", "CAM-02", "DRN-01", "KIT-01", "KIT-02"}) {
            empresa.asignarEquipoASede("S-01", codigo);
        }

        empresa.registrarOperador(new Operador("2001", "Carlos Ruiz", "Piloto certificado", 6));

        Cliente cliente = new Cliente("Laura Gomez", "1001", "3001234567", "laura@correo.com");
        empresa.registrarCliente(cliente);

        LocalDate manana = LocalDate.now().plusDays(1);
        empresa.registrarReserva(new Reserva("R-001", cliente, manana, Turno.MANANA));
        empresa.agregarEquipoAReserva("R-001", "CAM-01", 5);
        empresa.agregarEquipoAReserva("R-001", "KIT-01", 3);

        mensaje("Datos de ejemplo cargados:" +
                "\n- Sede S-01" +
                "\n- Equipos CAM-01, CAM-02, DRN-01, KIT-01 y KIT-02 (en mantenimiento)" +
                "\n- Operador 2001" +
                "\n- Cliente 1001" +
                "\n- Reserva R-001 (" + manana + ", MANANA) con CAM-01 y KIT-01");
    }




    private static String pedirTexto(String pregunta) {
        while (true) {
            String texto = JOptionPane.showInputDialog(null, pregunta, TITULO, JOptionPane.QUESTION_MESSAGE);
            if (texto == null) {
                throw new OperacionCancelada();
            }
            if (!texto.isBlank()) {
                return texto.trim();
            }
            mensaje("El campo no puede estar vacio");
        }
    }


    private static int pedirEntero(String pregunta) {
        while (true) {
            String texto = pedirTexto(pregunta);
            try {
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                mensaje("Debe ingresar un numero entero");
            }
        }
    }

    private static boolean pedirSiNo(String pregunta) {
        int respuesta = JOptionPane.showConfirmDialog(null, pregunta, TITULO, JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.CLOSED_OPTION) {
            throw new OperacionCancelada();
        }
        return respuesta == JOptionPane.YES_OPTION;
    }


    private static Object elegir(String pregunta, Object[] opciones) {
        Object seleccion = JOptionPane.showInputDialog(null, pregunta, TITULO,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (seleccion == null) {
            throw new OperacionCancelada();
        }
        return seleccion;
    }


    private static LocalDate elegirFecha(String pregunta) {
        LocalDate hoy = LocalDate.now();
        String[] fechas = new String[60];
        for (int i = 0; i < fechas.length; i++) {
            fechas[i] = hoy.plusDays(i).toString();
        }
        String seleccion = (String) elegir(pregunta, fechas);
        return LocalDate.parse(seleccion);
    }

    private static void resultado(boolean exito, String mensajeExito, String mensajeFallo) {
        mensaje(exito ? mensajeExito : mensajeFallo);
    }

    private static void mensaje(String texto) {
        JOptionPane.showMessageDialog(null, texto, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }


    private static void mostrarTexto(String titulo, String texto) {
        if (texto.isBlank()) {
            texto = "No hay registros para mostrar";
        }
        JTextArea area = new JTextArea(texto, 18, 40);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}