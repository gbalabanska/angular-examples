import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { InvestmentService } from '../investment.service';
import { InvestmentInput } from '../../model/result.model';

@Component({
  selector: 'app-calculator',
  standalone: true,
  imports: [FormsModule], // enable ngModel
  templateUrl: './calculator.component.html',
  styleUrls: ['./calculator.component.css'],
})
export class CalculatorComponent {
  initialInvestment: number = 0;
  annualInvestment: number = 0;
  expectedReturn: number = 0;
  duration: number = 0;

  constructor(private investmentService: InvestmentService) {}

  onCalculate() {
    const input: InvestmentInput = {
      initialInvestment: this.initialInvestment,
      annualInvestment: this.annualInvestment,
      expectedReturn: this.expectedReturn,
      duration: this.duration,
    };

    console.log('Input sent to service:', input);

    this.investmentService.calculateInvestmentResults(input);
    this.investmentService.setShowResult(true);
  }
}
