package spsi;

import java.math.BigInteger;
import java.util.Random;

public class Prueba {
	
	static byte[] randomInput = new byte[600];
	static Block block  = null;
	
	
	private static void testIncreaseChain() {
		for( int i = 0; i < 300000; i++) {
			Block.increaseChanin(randomInput);
			
			if(i%254 == 0) {
				for(byte b:randomInput)
					System.out.print(b + " ");
				System.out.println();
			}
		}
	}
	
	private static void testCheckBits() {
		System.out.println(Block.checkBits(randomInput, 1));
		System.out.println(Block.checkBits(randomInput, 2));
		System.out.println(Block.checkBits(randomInput, 10) + "\n\n");
		randomInput[0] = 32;
		System.out.println(Block.checkBits(randomInput, 0));
		System.out.println(Block.checkBits(randomInput, 1));
		System.out.println(Block.checkBits(randomInput, 2));
		System.out.println(Block.checkBits(randomInput, 3));
		System.out.println(Block.checkBits(randomInput, 4) + "\n\n");
		
		randomInput[0] = 24;
		System.out.println(Block.checkBits(randomInput, 0));
		System.out.println(Block.checkBits(randomInput, 1));
		System.out.println(Block.checkBits(randomInput, 2));
		System.out.println(Block.checkBits(randomInput, 3));
		System.out.println(Block.checkBits(randomInput, 7) + "\n\n");
	}
	
	private static void blockChain(int bParam, int repeticiones, boolean increase) {
		
		for(int b = 0; b <= bParam; b ++) {
			//Generamos el primer bloque, siempre con mi nombre como texto
			block = new Block("Pedro Luis Fuertes Moreno", b, randomInput, increase);
			//Iniciamos la variable para cada b, con el númro de intentos para generar el primer bloque
			int nintentos = block.getnIntentos(); 
			for(int i = 1; i < repeticiones; i++) {
				block.nextBlock(); //Llamamos a nextBlock 9 veces más
				nintentos += block.getnIntentos(); // Guardamos el número de intentos para generar cada bloque
				
			}
			//System.out.println(block);
			System.out.println(nintentos/repeticiones + "\t" + b); // Calculamos la media y la imprimimos. 
		}
	}
	
	public static void main(String[] args) {
		Random random = new Random();
		for(int i = 0;i < randomInput.length; i ++) {
			randomInput[i] = (byte) random.nextInt(); 
		}
		
		int b = 18; 
		
		System.out.println("Aleatorio");
		blockChain(b, 1000, false);
		System.out.println("\nIncremental");
		blockChain(b, 1000, true);
		
	}
	
}
