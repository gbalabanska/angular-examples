import { Injectable } from '@angular/core';
import { InvestmentInput, InvestmentResultRow } from '../model/result.model';

@Injectable({
  providedIn: 'root', // This ensures the service is singleton and available throughout the app
})
export class InvestmentService {
  private annualData: InvestmentResultRow[] = [];
  private showResult: boolean = false;

  // Method to calculate the investment results
  calculateInvestmentResults(input: InvestmentInput): InvestmentResultRow[] {
    this.annualData = [];
    // Explicitly access the fields of input
    const initialInvestment = input.initialInvestment;
    const annualInvestment = input.annualInvestment;
    const expectedReturn = input.expectedReturn;
    const duration = input.duration;

    let investmentValue = initialInvestment;

    // Loop through each year to calculate the results
    for (let i = 0; i < duration; i++) {
      const year = i + 1;

      // Calculate interest earned for the year
      const interestEarnedInYear = investmentValue * (expectedReturn / 100);

      // Update the investment value (interest earned + annual investment)
      investmentValue += interestEarnedInYear + annualInvestment;

      // Calculate the total interest (investment value minus the total invested capital)
      const totalInterest =
        investmentValue - annualInvestment * year - initialInvestment;

      // Push the calculated results for this year into the data array
      this.annualData.push({
        year: year,
        investmentValue: investmentValue,
        interestYear: interestEarnedInYear,
        totalInterest: totalInterest,
        investedCapital: initialInvestment + annualInvestment * year,
      });
    }
    console.log('Output sent from the service:', this.annualData);
    return this.annualData;
  }

  getInvestmentResults(): InvestmentResultRow[] {
    return this.annualData;
  }

  setShowResult(showResult: boolean) {
    this.showResult = showResult;
  }

  getShowResult(): boolean {
    return this.showResult;
  }
}
