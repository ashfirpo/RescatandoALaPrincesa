package rescatandoALaPrincesav2;

import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		//Nota: la versión 2 (esta) es la posta. O por lo menos hasta ahora.
		Rescate r = new Rescate("grafos_enunciado.in");
		r.resolver();
	}

}
