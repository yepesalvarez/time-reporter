package com.time.reporter;

public class TestFactorial {

	public static void main(String[] args) {
		
//		int contadorDecenas = 0;
//		int resultado = n;
//		while (resultado != 0) {
//			resultado = resultado / 10;
//			contadorDecenas++;
//		}
//		contadorDecenas -= 1;
		
		int factorialNum = 720;
		int divisor = 1;
		int contadorZeros = 0;
		boolean continuar = true;
		while(continuar) {
			divisor *= 10;
			if ( factorialNum % divisor == 0) {
				contadorZeros ++;
			} else {
				continuar = false;
			}
		}
		System.out.println("cantidad de zeros : " + contadorZeros);
		// TODO Auto-generated method stub

	}

}
