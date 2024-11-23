import { Component } from '@angular/core';
import { TitleComponent } from './header/title/title.component';
import { CalculatorComponent } from './investments/calculator/calculator.component';
import { ResultComponent } from './investments/result/result.component';
import { InvestmentService } from './investments/investment.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [TitleComponent, CalculatorComponent, ResultComponent],
  templateUrl: './app.component.html',
})
export class AppComponent {
  constructor(private investmentService: InvestmentService) {}

  get showResult(): boolean {
    return this.investmentService.getShowResult();
  }
}
