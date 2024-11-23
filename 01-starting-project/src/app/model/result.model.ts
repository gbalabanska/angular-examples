export interface InvestmentInput {
  initialInvestment: number;
  annualInvestment: number;
  expectedReturn: number;
  duration: number;
}

export interface InvestmentResultRow {
  year: number;
  investmentValue: number;
  interestYear: number;
  totalInterest: number;
  investedCapital: number;
}
