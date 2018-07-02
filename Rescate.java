package rescatandoALaPrincesav2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

public class Rescate {
	
	private int cantClaros; //Nodos
	private int cantSenderos; //Aristas
	private int cantDragones;
	private int nodoPrincesa;
	private int nodoPrincipe;
	private int[][] nodos;
	private int[] nodoDragones;
	private static final int INF = 99999;
	
	public Rescate(String path) throws FileNotFoundException
	{
		Scanner sc = new Scanner(new File(path));
		
		this.cantClaros = sc.nextInt();
		this.cantSenderos=sc.nextInt();
		this.cantDragones = sc.nextInt();
		sc.nextLine();
		
		this.nodoPrincesa = sc.nextInt() -1;
		this.nodoPrincipe = sc.nextInt() -1;
		sc.nextLine();
		
		this.nodos = new int[this.cantClaros][this.cantClaros];
		for(int i=0;i<this.cantClaros;i++)
			Arrays.fill(this.nodos[i], INF);
		this.nodoDragones = new int[this.cantDragones];
		for(int i=0;i<this.cantDragones;i++)
			this.nodoDragones[i] = (sc.nextInt() -1);
		
		for(int i=0;i<this.cantSenderos;i++)
		{
			int origen = sc.nextInt() -1;
			int destino = sc.nextInt() -1;
			int costo = sc.nextInt();			
			this.nodos[origen][destino] = this.nodos[destino][origen] = costo; //Grafo NDP
		}
		sc.close();
	}
	
	
	public void resolver()
	{
		//Hago una copia de la matriz original para ir sacando los nodos peligrosos y tener el camino seguro
		int[][] caminoSeguro = new int[this.cantClaros][this.cantClaros];
		for(int i=0;i<this.cantClaros;i++)
			caminoSeguro[i] = Arrays.copyOf(this.nodos[i], this.cantClaros);
		
		LinkedList<Nodo> principe = new LinkedList<Nodo>();
		LinkedList<Nodo> dragon = new LinkedList<Nodo>();
		
		//Obtengo el camino más corto que haría el príncipe
		principe = dijkstra(this.nodoPrincipe, this.nodoPrincesa, this.nodos);
		
		if(principe == null || principe.isEmpty())
		{
			System.out.println("No hay camino");
			return;
		}
		
		for(int i=0;i<this.cantDragones;i++)
		{
			dragon = dijkstra(this.nodoDragones[i], this.nodoPrincesa, this.nodos);
			if(dragon == null)
				break;
			
			int tope = principe.size()>dragon.size()?dragon.size():principe.size();
			for(int j=0;j<tope;j++)
			{
				Nodo d = dragon.get(j);
				Nodo p = principe.get(j);
				if(j>0 && d!=null && p!= null && d.getNombre() == p.getNombre() && d.getCosto() <= p.getCosto())
				{
					caminoSeguro[principe.get(principe.indexOf(p)-1).getNombre()][d.getNombre()] = INF;
					caminoSeguro[d.getNombre()][principe.get(principe.indexOf(p)-1).getNombre()] = INF;
					break;
				}
			}
		}
		
		//Ahora busco el camino más corto con los nodos seguros
		principe = dijkstra(this.nodoPrincipe, this.nodoPrincesa, caminoSeguro);
		
		if(principe == null || principe.isEmpty())
			System.out.println("Interceptado");
		else
		{
			System.out.println("Camino seguro:");
			for(Nodo n : principe)
				System.out.print((n.getNombre() +1) + " ");
		}
	}
		
	public LinkedList<Nodo> dijkstra(int nodoOrigen, int nodoDestino, int[][] matriz)
	{
		int[] costos = new int[this.cantClaros];
		int[] camino = new int[this.cantClaros];
		int[] inf = new int[this.cantClaros];
		Arrays.fill(costos, INF);
		Arrays.fill(camino,  nodoOrigen);
		Arrays.fill(inf, INF);
		Set<Integer> nodosRestantes = new HashSet<Integer>();
		
		for(int i=0;i<this.cantClaros;i++)
			nodosRestantes.add(i);
		
		nodosRestantes.remove(nodoOrigen);
		
		for(int i=0;i<this.cantClaros;i++)
			costos[i] = matriz[nodoOrigen][i];

		for(int i=0;i<this.cantClaros;i++)
		{
			int flag=0, j=0;
			while(j<this.cantClaros && matriz[j][i] == INF)
			{
				j++;
				flag=1;
			}
			
			if(j==this.cantClaros && flag ==1 && Arrays.equals(matriz[i],inf))
			{
				nodosRestantes.remove(i);
				if(i == nodoOrigen || i == nodoDestino) //Si el nodo aislado es el nodo origen o el nodo destino
					return null;
			}
		}
		
		while(!nodosRestantes.isEmpty())
		{
			int nodoMenor=0, menorValor = INF;
			
			for(Integer i : nodosRestantes)
			{
				if(costos[i] < menorValor)
				{
					nodoMenor = i;
					menorValor = costos[i];
				}
			}
			
			//Busco si hay un menor costo
			for(Integer i : nodosRestantes)
			{
				if(costos[nodoMenor] + matriz[nodoMenor][i] < costos[i])
				{
					costos[i] = costos[nodoMenor] + matriz[nodoMenor][i];
					camino[i] = nodoMenor;
				}
			}
			
			nodosRestantes.remove(nodoMenor);
		}
		
		//Armo la lista con los nodos del recorrido más corto y sus costos hasta ese nodo
		LinkedList<Nodo> resultado = new LinkedList<Nodo>();
		Nodo n = new Nodo(nodoDestino, costos[nodoDestino]);
		resultado.add(n);
		int c = camino[nodoDestino];
		n = new Nodo(c, costos[c]);
		while(c != nodoOrigen)
		{
			resultado.add(n);
			c=camino[c];
			n = new Nodo(c, costos[c]);
		}		
		resultado.add(new Nodo(nodoOrigen, 0));
		Collections.reverse(resultado);
		
		return resultado;		
	}
}
