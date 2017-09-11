import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.lang.Thread;


public class Servidor implements Donaciones {

class Usuario{
	String nombre;
	double TotalDonado=0;
	String causa;
	public Usuario(String nombre,String causa){
		this.nombre=nombre;
		this.causa=causa;
	}
	public double getTotalDonado(){
		return TotalDonado;
	}
	public void donar(double dinero){
		TotalDonado = TotalDonado + dinero;
	}
	public String getNombre(){
		return nombre;
	}
}

Usuario listaUsuarios[] = new Usuario[100];
Usuario listaUsuariosOtraCausa[] = new Usuario[100];
int numeroUsuarios = 0;
int numeroUsuariosOtraCausa=0;
String nombreReplica;
String otraReplica;

public Servidor(String nombreReplica) {
	super();
	this.nombreReplica=nombreReplica;
}

@Override
public int getTotalUsuarios(){
	return numeroUsuarios;
}

public int getTotalUsuariosOtraCausa(){
	return numeroUsuariosOtraCausa;
}

public void setOtraReplica(String otraReplica){
	this.otraReplica=otraReplica;

}
public boolean registrado(String nombreCliente,String causa){
	boolean registrado=false;
	if(causa.equals("1")){
	for(int i=0; i<numeroUsuarios;i++){
	if(listaUsuarios[i].getNombre().equals(nombreCliente)){
		System.out.println("Usuario ya registrado en causa 1");
		registrado=true;
	}}}
	if(causa.equals("2")){
		for(int i=0; i<numeroUsuariosOtraCausa;i++){
	if(listaUsuariosOtraCausa[i].getNombre().equals(nombreCliente)){
		System.out.println("Usuario ya registrado en causa 2");
		registrado=true;
	}}
	}
	return registrado;
}

public String registrar(String nombreCliente,String causa){
	String registrado=nombreReplica;
	boolean puede =true;
	if(causa.equals("1")){

	if(!registrado(nombreCliente,causa)){
	try{
		Registry registry2 = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local2 = (Donaciones) registry2.lookup(otraReplica);
	instancia_local2.getTotalUsuarios();
		if(!(instancia_local2.registrado(nombreCliente,causa))&&(instancia_local2.getTotalUsuarios()<this.getTotalUsuarios())){
		instancia_local2.registrar(nombreCliente,causa);
		registrado=otraReplica;
}else{
		
		listaUsuarios[numeroUsuarios]= new Usuario(nombreCliente,causa);
		numeroUsuarios++;
		registrado=nombreReplica;
		System.out.println("Registrado en la replica = " + nombreReplica+ "causa 1");}
	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("El usuario ya existe");
}}else if(causa.equals("2")){

	if(!registrado(nombreCliente,causa)){
	try{
		Registry registry2 = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local2 = (Donaciones) registry2.lookup(otraReplica);
	instancia_local2.getTotalUsuariosOtraCausa();
		if(!(instancia_local2.registrado(nombreCliente,causa))&&(instancia_local2.getTotalUsuariosOtraCausa()<this.getTotalUsuariosOtraCausa())){
		instancia_local2.registrar(nombreCliente,causa);
		registrado=otraReplica;
}else{
		
		listaUsuariosOtraCausa[numeroUsuariosOtraCausa]= new Usuario(nombreCliente,causa);
		numeroUsuariosOtraCausa++;
		registrado=nombreReplica;
		System.out.println("Registrado en la replica = " + nombreReplica+" causa 2");}
	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {
            System.out.println("El usuario ya existe");
}
}
	
	return registrado;
}

public int donar(String nombreCliente,double dinero,String causa){

	int registrado=0;
	if(causa.equals("1")){
	if(!registrado(nombreCliente,causa)){
	try{
		Registry registry = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local = (Donaciones) registry.lookup(otraReplica);
		
		if(instancia_local.registrado(nombreCliente,causa)){
			instancia_local.donar(nombreCliente,dinero,causa);
		}

	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {	
		for(int i=0; i<numeroUsuarios;i++){
		if(listaUsuarios[i].getNombre().equals(nombreCliente)){
		System.out.println("Dinero Ingresado en replica = " + nombreReplica + " con causa 1");
		listaUsuarios[i].donar(dinero);
		registrado=1;
		}}
		

		}}
		if(causa.equals("2")){
			if(!registrado(nombreCliente,causa)){
	try{
		Registry registry = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local = (Donaciones) registry.lookup(otraReplica);
		
		if(instancia_local.registrado(nombreCliente,causa)){
			instancia_local.donar(nombreCliente,dinero,causa);
		}

	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {	
		for(int i=0; i<numeroUsuariosOtraCausa;i++){
		if(listaUsuariosOtraCausa[i].getNombre().equals(nombreCliente)){
		System.out.println("Dinero Ingresado en replica = " + nombreReplica + " con causa 2");
		listaUsuariosOtraCausa[i].donar(dinero);
		registrado=1;
		}}
		

		}
		}

	return registrado;
}

public double consultarTotalDonado(String nombreCliente,String causa){
double dinero=0;
if(causa.equals("1")){
if(!registrado(nombreCliente,causa)){
	try{
		Registry registry = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local = (Donaciones) registry.lookup(otraReplica);
		
		if(instancia_local.registrado(nombreCliente,causa)){
			dinero=instancia_local.consultarTotalDonado(nombreCliente,causa);
		}

	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {	
for(int i=0; i<numeroUsuarios;i++){
	if(listaUsuarios[i].getNombre().equals(nombreCliente)){
		System.out.println("Dinero total encontradoen replica " + nombreReplica + " con causa 1");
		dinero=listaUsuarios[i].getTotalDonado();
	}
	}
}}if(causa.equals("2")){
	if(!registrado(nombreCliente,causa)){
	try{
		Registry registry = LocateRegistry.getRegistry(6080);
		Donaciones instancia_local = (Donaciones) registry.lookup(otraReplica);
		
		if(instancia_local.registrado(nombreCliente,causa)){
			dinero=instancia_local.consultarTotalDonado(nombreCliente,causa);
		}

	}catch (Exception e){
                e.printStackTrace();
            }
        }
        else {	
for(int i=0; i<numeroUsuariosOtraCausa;i++){
	if(listaUsuariosOtraCausa[i].getNombre().equals(nombreCliente)){
		System.out.println("Dinero total encontradoen replica " + nombreReplica + " con causa 2");
		dinero=listaUsuariosOtraCausa[i].getTotalDonado();
	}
	}
}
}
return dinero;
} 

public static void main(String[] args) {
if (System.getSecurityManager() == null) {
System.setSecurityManager(new SecurityManager());
}
try {

final Registry registry = LocateRegistry.createRegistry(6080);


String nombre_objeto_remoto = "Donaciones";
Donaciones prueba = new Servidor(nombre_objeto_remoto);
Donaciones stub =
(Donaciones) UnicastRemoteObject.exportObject(prueba, 0);
registry.rebind(nombre_objeto_remoto, stub);
System.out.println("Servidor1 Conectado");


String nombre_objeto_remoto2 = "Donaciones2";
Donaciones prueba2 = new Servidor(nombre_objeto_remoto2);
Donaciones stub2 =
(Donaciones) UnicastRemoteObject.exportObject(prueba2, 0);
registry.rebind(nombre_objeto_remoto2, stub2);
System.out.println("Servidor2 Conectado");


//Runtime.getRuntime().exec("gnome-terminal -x java -cp . -Djava.rmi.server.codebase=file:./ -Djava.rmi.server.hostname=localhost -Djava.security.policy=server.policy Servidor");

prueba.setOtraReplica(nombre_objeto_remoto2);
prueba2.setOtraReplica(nombre_objeto_remoto);


} catch (Exception e) {
System.err.println("Servidor exception:");
e.printStackTrace();
}
}


}
