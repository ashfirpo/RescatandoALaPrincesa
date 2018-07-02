package rescatandoALaPrincesav2;

public class Nodo {
	
	private int nombre;
	private int costo;
	
	public Nodo(int nombre)
	{
		this.nombre=nombre;
	}
	
	public Nodo(int nombre, int costo)
	{
		this.nombre=nombre;
		this.costo=costo;
	}
	
	public int getNombre()
	{
		return this.nombre;
	}
	
	public int getCosto()
	{
		return this.costo;
	}

}
