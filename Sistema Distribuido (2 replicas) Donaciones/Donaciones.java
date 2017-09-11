import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Donaciones extends Remote {
    public String registrar(String nombreCliente,String causa) throws RemoteException;
    public int donar(String nombreCliente,double dinero,String causa) throws RemoteException;
    public double consultarTotalDonado(String nombreCliente,String causa) throws RemoteException;
    public int getTotalUsuarios() throws RemoteException;
    public int getTotalUsuariosOtraCausa() throws RemoteException;
    public boolean registrado(String nombreCliente,String causa) throws RemoteException;
    public void setOtraReplica(String otraReplica) throws RemoteException;
}
