import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner; 

public class Cliente {

public static void main(String args[]) {

if (System.getSecurityManager() == null) {
System.setSecurityManager(new SecurityManager());
}
try {
String jj="Donaciones";
double dinero=0;
boolean salir=false; 
String nombreUsuario="s";
String causa="1";
Scanner sc = new Scanner(System.in);
String opcion;
String nombre_objeto_remoto = "Donaciones";
System.out.println("Buscando el objeto remoto");
Registry registry = LocateRegistry.getRegistry(6080);
Donaciones instancia_local = (Donaciones) registry.lookup(nombre_objeto_remoto);
System.out.println("Invocando el objeto remoto");

System.out.println("\nSeleciona \n 1:Registrarse \n 2:Donar \n 3:Consultar total donado \n 0:Salir \n");

while(!salir){
instancia_local = (Donaciones) registry.lookup(jj);
opcion = sc.nextLine();
switch(opcion){

	case "1":
	nombreUsuario="";
	System.out.println("Ingresa tu nombre ");
	nombreUsuario = sc.nextLine();
	System.out.println("Ingresa el numero de causa humanitaria 1 o 2 ");
	causa = sc.nextLine();
	System.out.println ("Te has registrado como = : \"" + nombreUsuario + " , en la causa "+ causa+"\"");
	jj=instancia_local.registrar(nombreUsuario,causa);
	if(jj=="1"){
	  System.out.println("Registrado correctamente ");
	}else{
	  //System.out.println("El usuario ya existe ");
	}
	System.out.println("Elige opcion ");
	break;

	case "2":
	System.out.println("caso 2");
	nombreUsuario="";
	System.out.println("Ingresa tu nombre ");
	nombreUsuario = sc.nextLine();
	System.out.println("Ingresa el numero de causa humanitaria 1 o 2 ");
	causa = sc.nextLine();
	System.out.println("Cantidad a donar ");
	double dineroIngresar = sc.nextInt();
	
	int correctoDonar = instancia_local.donar(nombreUsuario,dineroIngresar,causa);
	if(correctoDonar==1){
	  System.out.println("Cantidad donada ");
	}else{
	  System.out.println("El usuario no existe ");
	}
	System.out.println("Elige opcion ");
	break;

	case "3":
	nombreUsuario="";
	System.out.println("Ingresa tu nombre ");
	nombreUsuario = sc.nextLine();
	System.out.println("Ingresa el numero de causa humanitaria 1 o 2 ");
	causa = sc.nextLine();
	dinero=instancia_local.consultarTotalDonado(nombreUsuario,causa);
	System.out.println("El Dinero total donado por "+nombreUsuario+" es =  "+dinero);
	System.out.println("Elige opcion ");
	break;

	case "0":
	System.out.println("Cerrando");
	salir=true;
	break;
}}

} catch (Exception e) {
System.err.println("Cliente exception:");
e.printStackTrace();
}
}
}
