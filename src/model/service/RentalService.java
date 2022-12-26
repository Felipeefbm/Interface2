package model.service;

import model.entities.CarRental;
import model.entities.Invoice;

public class RentalService {   
		
	private double pricePerHour;
	private double pricePerDay;
	
	private TaxService taxService;  // foi feito uma classe do tipo interface generica (TaxService) e uma especifica (BrazilTaxService) 
											// pra ficar com acoplamento baixo. Sendo assim o objeto de dominio principal(RentalService)
											// nao conhece a dependência concreta.
	public RentalService() {
	}
	
	public RentalService(double pricePerDay, double pricePerHour, TaxService taxService) {   // upscasting de brazilTaxSvc e TaxSvc
		// lembrando que o parametro TaxService é uma interface generica. Quando for fazer a instanciação no programa principal
		this.pricePerDay = pricePerDay;		// deve ser feito com a classe especifica BrazilTaxService.
		this.pricePerHour = pricePerHour;
		this.taxService = taxService;
		
	}

	public TaxService getTaxService() {
		return taxService;
	}

	public void setTaxService(BrazilTaxService taxService) {
		this.taxService = taxService;
	}

	public double getPricePerHour() {
		return pricePerHour;
	}

	public void setPricePerHour(double pricePerHour) {
		this.pricePerHour = pricePerHour;
	}

	public double getPricePerDay() {
		return pricePerDay;
	}

	public void setPricePerDay(double pricePerDay) {
		this.pricePerDay = pricePerDay;
	}
		
		// diferença entre as datas
	public void processInvoice(CarRental carRental) {
		 // lembrando que as datas são armazenadas em miliseconds
		long t1 = carRental.getStart().getTime(); // getStart momento que o carro foi pego, getTime tempo em miliseconsd da data digitada
		long t2 = carRental.getFinish().getTime();       // salva em t1
		
			// diferença em miliseconds entre as datas / em segundos / em minutos / em horas 
		double hours = (double)(t2-t1) / 1000 / 60 / 60;  // casting em double para arredondar a hora!
		
		  // preço por hora arrendondado pra cima  
		double basicPayment;
		if (hours <= 12.00) {
			basicPayment = Math.ceil(hours) * pricePerHour;  //math.ceil vai arrendondar os minutos pra uma hora
		}													// (se for 5.01 horas arredonda para 6 e assim sucessivamente)
			// passou de 12 horas vai pagar por diaria
		else {
			basicPayment = Math.ceil(hours / 24) * pricePerDay;   // vai arredondar as horas pra mais um dia
		}
			// calcular o valor do imposto a partir do basicPayment
		double tax = taxService.tax(basicPayment);
		 
 		// adicionando basicpayment e a taxa no carRental por meio do setInvoice 
		carRental.setInvoice(new Invoice(basicPayment, tax));
		
		
	}
	
}
