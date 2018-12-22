package spsi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Block {
	
	byte[] id;
	byte[] x;
	byte[] hash;
	int nIntentos = 0;
	byte[] textBytes;
	int b;
	byte[] inputBitsChain;
	boolean increase;

	public Block(String text, int b, byte[] inputBytes, boolean increase) {
		this.textBytes = text.getBytes(StandardCharsets.UTF_8);	//Pasamos el texto a bits
		this.b = b;
		this.inputBitsChain = inputBytes;
		this.increase = increase;
			   
		create();
		
	}
	
	private void create() {
		
		MessageDigest sha256 = null;
		
		try {
			sha256 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
			System.err.println("El algoritmo de hash no se enuentra.");
		} 
		
		//creamos el id
		id = concatBytesArray(textBytes, inputBitsChain);
		
		//generamos una cadena aleatoria
		byte[] randomChanin = genRandomChanin(inputBitsChain.length);
		
		//Le añadimos al id una cadena aleatoria del tamaño n
		x = concatBytesArray(id, randomChanin);
		
		//Hacemos el hash
		hash = sha256.digest(x);
		
		nIntentos = 0; 
				
		while(!checkBits(hash, b)) { //Comprobamos los bits a 0
			if(increase) 
				x = concatBytesArray(id, increaseChanin(randomChanin)); // Función aleatoria
			else
				x = concatBytesArray(id, genRandomChanin(inputBitsChain.length)); // Función incremental
			
			//Hacemos el hash
			hash = sha256.digest(x);
			// Aumentamos el número de intentos
			nIntentos++;
		}
	}
	
	static byte[] increaseChanin(byte[] chain) {
		
		int i = 0; 
		while(i < chain.length && chain[i] == -1){i++;}
		if(i < chain.length) {
			chain[i]++;
			if(i > 0 && (chain[i]%2 == 0 || chain[i] == 1))
				for(int j = 0; j < i; j++)
					chain[j] = 0;
		}else {
			for(i = 0; i < chain.length; i++)
				chain[i] = 0;;
		}
	 
		return chain;
		
	}
	
	private byte[] genRandomChanin(int n) {
		
		byte[] rantomChanin = new byte[n];
		
		Random r = new Random();
		for(int i = 0; i < n; i++) {
			rantomChanin[i] = (byte) r.nextInt();
		}
		
		return rantomChanin;
	}
	
	static boolean checkBits(byte[] bytes, int n) {
		
		boolean salida = true; 
		// Si n > 8, los primeros bits tienen que estar a 0 ==> bytes[i] == 0
		int i;
		for(i = 0; i < n / 8 && salida; i++) {
			salida = bytes[i] == 0;
		}
		
		// Si salida sigue siendo true
		if(salida) {
			n = n % 8;
			
			// para los que nos queden
			if(n > 0 && bytes[i] >= 0) {
				salida = bytes[i] < Math.pow(2, 8 - n);
			}else {// Si es negativo, el numero de bits de ese bloque tiene que ser 0 
				salida = n == 0; 
			}
			
		}

		return salida;
	}
	
	private byte[] concatBytesArray(byte[] arr1, byte[] arr2) {
		byte[] ret = new byte[arr1.length + arr2.length];
		
		System.arraycopy(arr1, 0, ret, 0, arr1.length);
		System.arraycopy(arr2, 0, ret, arr1.length, arr2.length);
		
		return ret;
	}
	
	private String toStringBytesArray(byte[] arr) {
		String out = "";
		for(byte b:arr)
			out +=  "" + b + " ";
		
		return out;
	}
	
	public Block nextBlock() {
		inputBitsChain = hash;
		create();
		return this;
	}
	
	@Override
	public String toString() {
		
		String out = ("id:\n");
		out += toStringBytesArray(id);
		
		out += "\nx:\n";
		out += toStringBytesArray(x);
		
		out += "\nHash:\n";
		out += toStringBytesArray(hash);
		
		out += "\nNúmero de intentos: " + nIntentos + "\n";
		
		return out;
	}

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public byte[] getX() {
		return x;
	}

	public void setX(byte[] x) {
		this.x = x;
	}

	public byte[] getHash() {
		return hash;
	}
	
	public String getStringHash() {
		return toStringBytesArray(hash);
	}
	
	public void setHash(byte[] hash) {
		this.hash = hash;
	}

	public int getnIntentos() {
		return nIntentos;
	}

	public void setnIntentos(int nIntentos) {
		this.nIntentos = nIntentos;
	}
	
}
