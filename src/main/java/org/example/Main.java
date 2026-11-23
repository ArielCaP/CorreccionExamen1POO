package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        HashMap<String, Envio> enTransito = new HashMap<>();
        ArrayList<Envio> entregados = new ArrayList<>();
        int contador = 1001;
        int opcion = 0;

        do {
            System.out.println("--- SERVIENTREGA ---");
            System.out.println("1. Registrar envio");
            System.out.println("2. Consultar envio");
            System.out.println("3. Entregar envio");
            System.out.println("4. Listar entregados");
            System.out.println("5. Salir");
            System.out.print("Elija una opcion: ");
            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1: {
                    System.out.print("Tipo de servicio (1 nacional, 2 express, 3 internacional): ");
                    int tipo = Integer.parseInt(sc.nextLine());
                    System.out.print("Nombre del destinatario: ");
                    String nombre = sc.nextLine();
                    System.out.print("Ciudad de destino: ");
                    String ciudad = sc.nextLine();
                    System.out.print("Peso en kilos: ");
                    double peso = Double.parseDouble(sc.nextLine());

                    if (peso <= 0) {
                        System.out.println("El peso debe ser mayor que cero.");
                        break;
                    }

                    Envio envio = null;

                    if (tipo == 1) {
                        System.out.print("Entrega a domicilio (S o N): ");
                        String dom = sc.nextLine();
                        boolean domicilio = dom.equalsIgnoreCase("S");
                        String guia = "NAC" + String.valueOf(contador);
                        envio = new EnvioNacional(guia, nombre, ciudad, peso, domicilio);
                    } else if (tipo == 2) {
                        System.out.print("Entrega a domicilio (S o N): ");
                        String dom = sc.nextLine();
                        boolean domicilio = dom.equalsIgnoreCase("S");
                        System.out.print("Entrega en 24 horas (S o N): ");
                        String r24 = sc.nextLine();
                        String guia = "EXP" + String.valueOf(contador);
                        if (r24.equalsIgnoreCase("S")) {
                            envio = new EnvioExpress(guia, nombre, ciudad, peso, domicilio, 24);
                        } else {
                            envio = new EnvioExpress(guia, nombre, ciudad, peso, domicilio);
                        }
                    } else if (tipo == 3) {
                        System.out.print("Valor de la compra en dolares: ");
                        double valor = Double.parseDouble(sc.nextLine());
                        String guia = "INT" + String.valueOf(contador);
                        EnvioInternacional inter = new EnvioInternacional(guia, nombre, ciudad, peso, valor);
                        if (!inter.cumple4x4()) {
                            System.out.println("Envio rechazado por la regla 4x4.");
                            break;
                        }
                        envio = inter;
                    }

                    if (envio != null) {
                        enTransito.put(envio.getGuia(), envio);
                        contador++;
                        System.out.println("Guia asignada: " + envio.getGuia());
                        System.out.println("Costo: " + envio.calcularCosto());
                    }
                    break;
                }
                case 2: {
                    System.out.print("Ingrese la guia: ");
                    String guia = sc.nextLine();
                    if (enTransito.containsKey(guia)) {
                        Envio e = enTransito.get(guia);
                        System.out.println(e.describir());
                        System.out.println("Costo: " + e.calcularCosto());
                    } else {
                        System.out.println("No existe un envio en transito con esa guia.");
                    }
                    break;
                }
                case 3: {
                    System.out.print("Ingrese la guia: ");
                    String guia = sc.nextLine();
                    if (enTransito.containsKey(guia)) {
                        Envio e = enTransito.remove(guia);
                        entregados.add(e);
                        System.out.println("Envio " + guia + " entregado.");
                    } else {
                        System.out.println("No existe un envio en transito con esa guia.");
                    }
                    break;
                }
                case 4: {
                    if (entregados.isEmpty()) {
                        System.out.println("Todavia no hay envios entregados.");
                    } else {
                        double total = 0;
                        for (Envio e : entregados) {
                            System.out.println(e.describir());
                            System.out.println("Costo: " + e.calcularCosto());
                            total = total + e.calcularCosto();
                        }
                        System.out.println("Total recaudado: " + total);
                    }
                    break;
                }
                case 5:
                    System.out.println("Hasta luego.");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } while (opcion != 5);

        sc.close();
    }
}